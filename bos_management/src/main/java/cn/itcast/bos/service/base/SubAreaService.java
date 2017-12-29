package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.domain.Page;

/**
 * Created by 宝宝心里苦丶 on 2017/12/28.
 */
public interface SubAreaService {
    void save(SubArea subArea);
    Page findPageQuery(SubArea subArea,int page,int rows);

    //Area findById(String areaId);
}
