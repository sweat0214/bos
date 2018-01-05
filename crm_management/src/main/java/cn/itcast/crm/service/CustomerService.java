package cn.itcast.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/30.
 */
public interface CustomerService {
    // 查询所有未关联客户列表
    @Path("/noassociationcustomers")
    @GET
    @Produces({ "application/xml", "application/json" })
    public List<Customer> findNoAssociationCustomers();

    // 已经关联到指定定区的客户列表
    @Path("/associationfixedareacustomers/{fixedareaid}")
    @GET
    @Produces({ "application/xml", "application/json" })
    public List<Customer> findHasAssociationFixedAreaCustomers(
            @PathParam("fixedareaid") String fixedAreaId);

    // 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3
    @Path("/associationcustomerstofixedarea")
    @PUT
    public void associationCustomersToFixedArea(
            @QueryParam("customerIdStr") String customerIdStr,
            @QueryParam("fixedAreaId") String fixedAreaId);
    @Path("/customer")
    @POST
    @Consumes({"application/xml","application/json"})
    public void regist(Customer customer);

    @Path("/customer/telephone/{telephone}")
    @GET
    @Consumes({"application/xml","application/json"})
    public Customer findByTelephone(@PathParam("telephone") String telephone);

    @Path("customer/updatetype/{telephone}")
    @GET
    public void updateType(@PathParam("telephone") String telephone);
}
