package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.Delayed;


/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
public interface StandardService {
    //添加
    public void save(Standard standard);
    //分页查看
    public Page<Standard> findPageData(Pageable pageable);
    //获得所有收派标准方法
    List<Standard> findAll();

}
