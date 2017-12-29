package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
public interface CourierRepository extends JpaRepository<Courier,Integer>,JpaSpecificationExecutor<Courier> {

    @Query(value = "update Courier set deltag='1' where id =?")
    @Modifying
    public void updateDelTag(Integer id);

    @Query(value = "update Courier set deltag=null where id =?")
    @Modifying
    public void updateDelTag2(Integer id);




}
