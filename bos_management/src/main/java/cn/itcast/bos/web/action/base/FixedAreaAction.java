package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

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
    public String delete(){
        String[] idArray = ids.split(",");
        System.out.println(idArray.toString());
        fixedAreaService.deleteById(idArray);
        return SUCCESS;
    }
}
