package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Action(value = "courier_save",results = {@Result(name = "success",location = "./pages/base/courier.html",type = "redirect")})
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
        //根据查寻条件 构造specification 条件查寻对象 (类似Hibernate的QBC查询)
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            /**
             * 构造条件查询方法,如果方法返回null,代表无条件查寻
             * Root 参数 获取条件表达式 name=? age=?
             * CriteriaQuery 参数,构造简单查寻条件返回 提供where方法
             * CriteriaBuilder 参数 构造Predicate对象,条件对象,构造复杂查寻效果
             *
             */
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                ArrayList<Predicate> list = new ArrayList<Predicate>();
                //单表查寻
                if(StringUtils.isNoneBlank(courier.getCourierNum())){
                    //进行快递员 工号查寻
                    //courierNum = ?
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
                    list.add(p1);
                }
                if(StringUtils.isNotBlank(courier.getCompany())){
                    //进行公司查寻,模糊查寻
                    //company like %?%
                    Predicate p2 = cb.like(root.get("company").as(String.class),"%"+ courier.getCompany()+"%");
                    list.add(p2);
                }
                if(StringUtils.isNotBlank(courier.getType())){
                    //进行快递员类型查寻,等值查询
                    //type=?
                    Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
                    list.add(p3);
                }
                //多表查寻(查询当前对象 关联 对象对应数据表)
                //使用Courier(Root),关联Standard
                Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
                if(courier.getStandard()!=null &&StringUtils.isNotBlank(courier.getStandard().getName())){
                    //进行收派标准名称 模糊查询
                    //standard.name like %?%
                    Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%" + courier.getStandard().getName() + "%");
                    list.add(p4);

                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        //调用业务层,返回Page
        Page<Courier> pageData = courierService.findPageData(specification,pageable);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total",pageData.getTotalElements());
        result.put("rows",pageData.getContent());
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
    //属性驱动
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }
    //作废快递员
    @Action(value = "courier_delBatch",results = {@Result(name = "success",location = "./pages/base/courier.html",type = "redirect")})
    public String delBatch(){
        //按, 分割ids
        String[] idArray = ids.split(",");
        courierService.delBatch(idArray);
        return SUCCESS;
    }
    //还原快递员
    @Action(value = "courier_addBatch",results = {@Result(name = "success",location = "./pages/base/courier.html",type = "redirect")})
    public String addBatch(){
        //按, 分割ids
        String[] idArray = ids.split(",");
        courierService.addBatch(idArray);
        return SUCCESS;
    }
    @Action(value = "courier_findnoassociation",results = {@Result(name = "success",type = "json")})
    /**
    * @Description (查寻未关联定区的快递员)
    * @name findnoassociation
    * @param []
    * @return java.lang.String
    */
    public String findnoassociation(){
        //调用业务层,查寻未关联定区的快递员
        List<Courier> couriers = courierService.findNoAssociation();
        //压栈
        ActionContext.getContext().getValueStack().push(couriers);
        return SUCCESS;
    }
}
