package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/28.
 */
public interface FixedAreaService {
    void save(FixedArea fixedArea);
    List<FixedArea> findAll();
    Page findPageData(FixedArea fixedArea,int page,int rows);

    void deleteById(String[] idArray);

    void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId);
}
