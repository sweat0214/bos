package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
@ParentPackage("json-default")
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
    //属性驱动
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    //分页列表查寻
    @Action(value = "standard_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        //调用业务层,查寻数据结果
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> pageData = standardService.findPageData(pageable);

        //返回客户端数据需要total和rows
        HashMap<String, Object> result = new HashMap<String,Object>();
        result.put("total",pageData.getTotalElements());
        result.put("rows",pageData.getContent());

        //将map转换为json数据返回,使用struts2-json-plugin插件
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
    //查询所有收派标准方法
    @Action(value = "standard_findAll",results = {@Result(name = "success",type = "json")})
    public String findAll(){
        List<Standard> standards=  standardService.findAll();
        ActionContext.getContext().getValueStack().push(standards);
        return SUCCESS;
    }

}
