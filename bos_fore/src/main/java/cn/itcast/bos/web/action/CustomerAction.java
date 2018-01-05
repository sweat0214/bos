package cn.itcast.bos.web.action;

import cn.itcast.bos.utils.MailUtils;
import cn.itcast.bos.utils.SmsUtils;
import cn.itcast.crm.domain.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.Servlet;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * Created by 宝宝心里苦丶 on 2018/1/3.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;
    @Action(value = "customer_sendSms")
    public String sendSms() throws IOException {
        //手机号保存在Customer对象
        //生成短信验证码
        String randomCode = RandomStringUtils.randomNumeric(4);
        //将短信验证码保存到session
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(),randomCode);

        System.out.println("生成手机验证码为:"+randomCode);

        //编辑短信内容
        final String msg = "尊敬的用户您好,本次获得验证码为:"+randomCode+",服务电话:4006184000";

        //调用MQ服务,发送一条消息
        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",model.getTelephone());
                mapMessage.setString("msg",msg);
                return mapMessage;
            }
        });
        return NONE;

        /*//请用SMS服务发送短信

        //String result = SmsUtils.sendSmsByHTTP(model.getTelephone(), msg);
        String result = "000/xxxx";
        if (result.startsWith("000")){
            //发送成功
            return NONE;
        }else {
            //发送失败
            throw new RuntimeException("短信发送失败,信息码:"+result);
        }*/

    }

    //属性注入
    private String checkcode;
    public void setCheckcode(String checkcode){
        this.checkcode = checkcode;
    }
    @Autowired
    private RedisTemplate<String,String > redisTemplate;
    @Action(value = "customer_regist",results = {
            @Result(name = "success",type = "redirect",location = "signup-success.html"),
            @Result(name = "input",type = "redirect",location = "signup.html")})
    public String regist(){
        //先检验短信验证码,如果不通过,调回注册页面
        //从session中获取之前生成的验证码
        String checkcodeSession = (String)ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
        ServletActionContext.getRequest().getSession().invalidate();
        if (!checkcode.equals(checkcodeSession)||checkcodeSession==null){
            System.out.println("短信验证错误!");

            return INPUT;
        }
        //调用webService 连接crm保留客户信息
        WebClient.create("http://localhost:9002/crm_management/services/customerService/customer")
                .type(MediaType.APPLICATION_JSON_TYPE).post(model);
        System.out.println("客户注册成功!!!");
        //发送一封激活邮件
        //生成激活码
        String activecode = RandomStringUtils.randomNumeric(32);
        //将激活码保存到redis,设置24小时失效
        redisTemplate.opsForValue().set(model.getTelephone(),activecode,24, TimeUnit.HOURS);

        //调用MailUtils发送邮件
        String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"+ MailUtils.activeUrl + "?telephone=" + model.getTelephone()+ "&activecode=" + activecode + "'>速运快递邮箱绑定地址</a>";
        MailUtils.sendMail("速运快递激活邮件", content, model.getEmail());


        return SUCCESS;

    }

    //属性驱动
    private String activecode;

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }
    @Action("customer_activeMail")
    public  String activeMail() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        //判断激活码是否有效
        String activecodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
        if (activecodeRedis == null || !activecodeRedis.equals(activecode)){
            //激活码无效
            ServletActionContext.getResponse().getWriter().println("激活码无效,请登录系统,重新绑定邮箱!");

        }else{
            //激活码有效
            //防止重复绑定
            //调用CRM WebService 查询客户是否已经绑定
            Customer customer = WebClient.create("http://localhost:9002/crm_management/services/customerService/customer/telephone/" + model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON_TYPE).get(Customer.class);
            if (customer.getType()==null || customer.getType() !=1){
                //没有绑定,进行绑定
                WebClient.create("http://localhost:9002/crm_management/services/customerService/customer/updatetype/"+model.getTelephone()).get();
                ServletActionContext.getResponse().getWriter().println("邮箱绑定成功");
            }else{
                //已经绑定过
                ServletActionContext.getResponse().getWriter().println("邮箱已经绑定过,无需重复绑定");
            }


            //删除redis的激活吗
            redisTemplate.delete(model.getTelephone());
        }
            return NONE;
    }
}
