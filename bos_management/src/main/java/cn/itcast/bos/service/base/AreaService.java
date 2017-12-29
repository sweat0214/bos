package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/27.
 */
public interface AreaService {
    void saveBatch(List<Area> areas);
    void save(Area area);
    //条件分页查询
    Page<Area> findPageData(Specification<Area> specification, Pageable pageable);

    Page  findPageData(Area area,int page, int rows);
    List<Area> findAll();
    // void delete(Area area);
    void deleteAreaById(String[] idArray);
}
