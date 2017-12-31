package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.TakeTime;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/30.
 */
public interface TakeTimeService {
    List<TakeTime> findAll();
}
