package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 宝宝心里苦丶 on 2017/12/28.
 */
public interface SubAreaRepository extends JpaRepository<SubArea,String>,JpaSpecificationExecutor<SubArea> {
}
