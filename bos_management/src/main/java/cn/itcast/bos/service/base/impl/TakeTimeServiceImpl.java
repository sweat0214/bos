package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/30.
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    @Override
    public List<TakeTime> findAll() {
        return takeTimeRepository.findAll();
    }
}
