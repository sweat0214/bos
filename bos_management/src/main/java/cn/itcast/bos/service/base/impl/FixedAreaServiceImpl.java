package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
    public Page findPageData(final FixedArea fixedArea, int page, int rows) {
        //分页查寻对象
        Pageable pageable = new PageRequest(page-1,rows);
        //构建条件查询对象
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                //构建查询条件
                if(StringUtils.isNoneBlank(fixedArea.getId())){
                    //根据定区编号查寻等值
                    Predicate p1 = cb.equal(root.get("id").as(String.class), fixedArea.getId());
                    list.add(p1);
                }
                if(StringUtils.isNotBlank(fixedArea.getCompany())){
                    //根据公司查寻 模糊like
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + fixedArea.getCompany() + "%");
                    list.add(p2);

                }
                return cb.and(list.toArray(new Predicate[0]));

            }
        };

        Page<FixedArea> fixedAreas = fixedAreaRepository.findAll(specification,pageable);
        return fixedAreas;
    }

    @Override
    public void deleteById(String[] idArray) {
        for (String id : idArray) {
            fixedAreaRepository.delete(id);
        }
    }
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    @Override
    public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
        FixedArea persistFixedArea = fixedAreaRepository.findOne(model.getId());
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        //快递员关联到定区上
        persistFixedArea.getCouriers().add(courier);
        //将收派时间关联到快递员上
        courier.setTakeTime(takeTime);

    }
}
