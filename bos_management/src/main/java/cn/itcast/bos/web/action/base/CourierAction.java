package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 宝宝心里苦丶 on 2017/12/25.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{
    //模型驱动
    private Courier courier = new Courier();



    @Override
    public Courier getModel() {
        return courier;
    }
    //注入service
    @Autowired
    private CourierService courierService;
    //添加快递员方法
    @Action(value = "courier_save",results = {@Result(name = "success",location = "./pages/base/courier.html",type = "json")})
    public String save(){
        courierService.save(courier);
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
    //分页查寻方法
    @Action(value = "courier_pageQuery",results = {@Result(name = "success",type = "json")})
    public String  pageQuery(){
        //封装Pageable
        Pageable pageable = new PageRequest(page - 1,rows);
        //调用业务层,返回Page
        Page<Courier> pageData = courierService.findPageData(pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("total",pageData.getTotalElements());
        result.put("rows",pageData.getContent());
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
