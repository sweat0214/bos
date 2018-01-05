package cn.itcast.crm.dao;

import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/30.
 */
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);

    @Query("update Customer set fixedAreaId=? where id=?")
    @Modifying
    void updateFixedAreaId(String fixedAreaId, int id);
    @Query("update Customer set fixedAreaId=null where fixedAreaId=?")
    @Modifying
    void clearFixedAreaId(String fixedAreaId);

    Customer findByTelephone(String telephone);

    @Query("update Customer set type=1 where telephone=?")
    @Modifying
    void updateType(String telephone);
}
