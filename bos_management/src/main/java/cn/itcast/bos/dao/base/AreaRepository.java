package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 宝宝心里苦丶 on 2017/12/27.
 */
public interface AreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area>{

    //public void deleteAreaById(String id);

}
