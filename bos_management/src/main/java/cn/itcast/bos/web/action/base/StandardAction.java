package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
@ParentPackage("struts-default")
@Namespace("/")
@Actions
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{
   //注入service
    @Autowired
    private StandardService standardService;
    //模型驱动
    private Standard standard = new Standard();
    @Override
    public Standard getModel() {
        return standard;
    }
    @Action(value = "standard_save",results = {@Result(name = "success",type = "redirect",location = "/pages/base/standard.html")})
    public String save(){
        System.out.println("添加收派标准!!!!!!!!!");
        standardService.save(standard);
        return SUCCESS;
    }
}
