package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.TakeTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 宝宝心里苦丶 on 2017/12/30.
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime,Integer> {
}
