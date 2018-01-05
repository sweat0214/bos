package cn.itcast.bos.mq;

import cn.itcast.bos.utils.SmsUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import javax.jms.MapMessage;

/**
 * Created by 宝宝心里苦丶 on 2018/1/5.
 */
@Service("smsConsumer")
public class SmsConsumer implements MessageListener{
    @Override
    public void onMessage(Message message, byte[] pattern) {
        MapMessage mapMessage = (MapMessage) message;

        //调用sms服务发送短信
        try {
            //String result = SmsUtils.sendSmsByHTTP(mapMessage.getString("telephone"), mapMessage.getString("msp"));
            String result = "000/xxxx";
            if (result.startsWith("000")){
                //发送成功
                System.out.println("发送短信成功,手机号:"+mapMessage.getString("telephone")+"验证码:"+mapMessage.getString("msg"));
            }else{
                //发送失败
                throw new RuntimeException("短信发送失败,信息码:"+result);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
