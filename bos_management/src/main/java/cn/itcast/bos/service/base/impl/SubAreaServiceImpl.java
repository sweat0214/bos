package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.SubAreaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
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
public class SubAreaServiceImpl  implements SubAreaService {
    @Autowired
    private SubAreaRepository subAreaRepository;

    @Override
    public void save(SubArea subArea) {
        subAreaRepository.save(subArea);
    }

    @Override
    public Page findPageQuery(SubArea subArea, int page, int rows) {
        Pageable pageable = new PageRequest(page - 1, rows);
        return subAreaRepository.findAll(pageable);

    }

    @Override
    public void importXls(List<SubArea> subAreas) {
        subAreaRepository.save(subAreas);
    }

    /*@Override
    public void findById(String areaId) {
        subAreaRepository.findOne(areaId);
    }*/
}
