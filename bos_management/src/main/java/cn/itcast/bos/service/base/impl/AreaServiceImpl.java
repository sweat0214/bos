package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
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
 * Created by 宝宝心里苦丶 on 2017/12/27.
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService{

    @Autowired
    private AreaRepository areaRepository;
    @Override
    public void saveBatch(List<Area> areas) {
        areaRepository.save(areas);
    }

    @Override
    public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
        return areaRepository.findAll(specification,pageable);
    }

    @Override
    public Page findPageData( final Area area, int page, int rows) {
        //构造分页查寻对象
        Pageable pageable = new PageRequest(page - 1,rows);
        //构造条件查寻对象
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //创建保存条件集合对象
                ArrayList<Predicate> list = new ArrayList<Predicate>();

                //添加条件
                if (StringUtils.isNoneBlank(area.getProvince())){
                    //根据省份查询 模糊
                    Predicate p1 = cb.like(root.get("province").as(String.class),"%"+area.getProvince()+"%");
                    list.add(p1);
                }
                if(StringUtils.isNoneBlank(area.getCity())){
                    //根据城市查询 模糊
                    Predicate p2 = cb.like(root.get("city").as(String.class),"%"+area.getCity()+"%");
                    list.add(p2);
                }
                if(StringUtils.isNoneBlank(area.getDistrict())){
                    //根据区域查询 模糊
                    Predicate p3 = cb.like(root.get("district").as(String.class),"%"+area.getDistrict()+"%");
                    list.add(p3);

                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Area> p = areaRepository.findAll(specification, pageable);

        return p;
    }

    @Override
    public void save(Area area) {
        areaRepository.save(area);

    }

    @Override
    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    @Override
    public void deleteAreaById(String[] idArray) {
        for (String id : idArray) {
        areaRepository.delete(id);

        }
    }


}
