package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
    @Autowired
    private StandardRepository standardRepository;

    @Override
    public void save(Standard standard) {
        standardRepository.save(standard);
    }
}
