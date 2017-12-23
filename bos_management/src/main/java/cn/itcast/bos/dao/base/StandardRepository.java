package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Standard;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
public interface StandardRepository extends JpaRepository<Standard,Integer> {
    //根据收派标准名称查询
    public List<Standard> findByName(String string);

    //nativeQuery 为false 配置JPQL,为true 配置sql
    @Query(nativeQuery = false,value = "form Standard where name = ?")
    public List<Standard> queryByName(String Stirng);

    @Query
    public List<Standard> queryByName2(String string);

    @Query(value = "update Standard set minLength=?2 where id =?1")
    @Modifying
    public void updateMinLength(Integer id, Integer minLength);



}
