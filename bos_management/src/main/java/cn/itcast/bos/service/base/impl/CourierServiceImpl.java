package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
import cn.itcast.bos.service.base.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);

    }

    @Override
    public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification, pageable);
    }

    @Override
    public List<Courier> findAll() {
        return courierRepository.findAll();
    }

    @Override
    public void delBatch(String[] idArray) {
        //调用Dao实现updateBatch修改操作,将deltag 修改为1
        for (String idstr : idArray) {
            int id = Integer.parseInt(idstr);
            courierRepository.updateDelTag(id);
        }
    }

    @Override
    public void addBatch(String[] idArray) {
        //调用Dao实现updateBatch修改操作,将deltag 修改为""
        for (String idstr : idArray) {
            int id = Integer.parseInt(idstr);
            courierRepository.updateDelTag2(id);
        }
    }

    @Override
    public List<Courier> findNoAssociation() {
        //封装specification
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //查寻条件,判定列表size为空
                Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));

                return p;
            }
        };
        return courierRepository.findAll(specification);
    }
}
