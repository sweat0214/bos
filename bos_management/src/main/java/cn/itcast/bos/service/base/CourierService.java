package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
public interface CourierService {
    //添加
    public void save(Courier courier);
    //分页查看
    public Page<Courier> findPageData(Specification<Courier> specification,Pageable pageable);
    //获得所有收派标准方法
    List<Courier> findAll();

    //
    public void delBatch(String[] idArray);

    public void addBatch(String[] idArray);
}
