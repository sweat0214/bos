package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.base.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/28.
 */
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class FixedAreaAction extends BaseAction<FixedArea>{

    @Autowired
    private FixedAreaService fixedAreaService;
    @Action(value = "fixedArea_save",results = {@Result(name = "success",location = "./pages/base/fixed_area.html",type = "redirect")})
    public String  save(){
        fixedAreaService.save(model);
        return SUCCESS;
    }
    @Action(value = "fixedArea_pageQuery",results = {@Result(name = "success",type = "json")})
    /**  
    * @data 20:00 2017/12/29  
    * @param []  
    * @return java.lang.String    
    */  
    public String pageQuery(){
        Page pageData = fixedAreaService.findPageData(model, page, rows);
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }
    @Action(value = "fixedArea_findAll",results = {@Result(name = "success",type = "json")})
    public String findAll(){
        List<FixedArea> fixedAreas = fixedAreaService.findAll();
        ActionContext.getContext().getValueStack().push(fixedAreas);
        return SUCCESS;
    }
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //定区删方法
    @Action(value = "fixedArea_delete",results = {@Result(name = "success",location = "./pages/base/fixed_area.html",type = "redirect")})
    /**  
    * @data 19:54 2017/12/29  
    * @param []  
    * @return java.lang.String    
    */  
    public String delete(){
        String[] idArray = ids.split(",");
        System.out.println(idArray.toString());
        fixedAreaService.deleteById(idArray);
        return SUCCESS;
    }
    @Action(value = "fixedArea_findNoAssociationCustomers",results = {@Result(name = "success",type = "json")})
    public String findNoAssociationCustomers(){
        //使用webClient 调用webService接口
        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:9002/crm_management/services/customerService/noassociationcustomers")
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }
    @Action(value = "fixedArea_findHasAssociationFixedAreaCustomers",results = {@Result(name = "success",type = "json")})
    public String findHasAssociationFixedAreaCustomers(){
        //使用WebClient 调用webService接口
        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:9002/crm_management/services/customerService/associationfixedareacustomers/"
                        + model.getId()).accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }
    // 属性驱动
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    // 关联客户到定区
    @Action(value = "fixedArea_associationCustomersToFixedArea", results = { @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
    public String associationCustomersToFixedArea() {
        String customerIdStr = StringUtils.join(customerIds, ",");
        WebClient.create(
                "http://localhost:9002/crm_management/services/customerService"
                        + "/associationcustomerstofixedarea?customerIdStr="
                        + customerIdStr + "&fixedAreaId=" + model.getId()).put(
                null);
        return SUCCESS;
    }
    //属性驱动
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    @Action(value = "fixedArea_associationCourierToFixedArea",results = {@Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
    public String associationCourierToFixedArea(){
        //调用业务层,定区关联快递员
        fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
        return SUCCESS;
    }
}
