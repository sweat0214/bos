package cn.itcast.crm.service.impl;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/30.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    //注入dao
    private CustomerRepository customerRepository;
    
    @Override
    /**
    * @Description 查询所有未关联客户列表
    * @name findNoAssociationCustomers
    * @param []
    * @return java.util.List<cn.itcast.crm.domain.Customer>
    */
    public List<Customer> findNoAssociationCustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    @Override
    /**
    * @Description (已经关联到指定定区的客户列表)
    * @name findHasAssociationFixedAreaCustomers
    * @param [fixedAreaId]
    * @return java.util.List<cn.itcast.crm.domain.Customer>
    */
    public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    @Override
    /**
    * @Description (将客户关联到定区,将所有客户id拼接成字符串1,2,3)
    * @name associationCustomersToFixedArea
    * @param [customerIdStr, fixedAreaId]
    * @return void
    */
    public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
        //解除关联动作
        customerRepository.clearFixedAreaId(fixedAreaId);
        //切割字符串
        if(StringUtils.isBlank(customerIdStr)){
            return;
        }
        String[] customerIdArray = customerIdStr.split(",");
        for (String idStr : customerIdArray) {
            int id = Integer.parseInt(idStr);
            customerRepository.updateFixedAreaId(fixedAreaId,id);
        }

    }
}
