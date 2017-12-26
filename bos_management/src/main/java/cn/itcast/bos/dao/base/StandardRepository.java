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




}
