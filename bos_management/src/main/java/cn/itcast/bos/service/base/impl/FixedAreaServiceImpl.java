package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/28.
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Override
    public void save(FixedArea fixedArea) {
        fixedAreaRepository.save(fixedArea);
    }

    @Override
    public List<FixedArea> findAll() {
        return fixedAreaRepository.findAll();
    }

    @Override
    public Page findPageData(FixedArea fixedArea, int page, int rows) {
        //分页查寻对象
        Pageable pageable = new PageRequest(page-1,rows);

        Page<FixedArea> fixedAreas = fixedAreaRepository.findAll(pageable);
        return fixedAreas;
    }

    @Override
    public void deleteById(String[] idArray) {
        for (String id : idArray) {
            fixedAreaRepository.delete(id);
        }
    }
}
