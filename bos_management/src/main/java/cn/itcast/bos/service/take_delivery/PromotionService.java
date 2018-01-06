package cn.itcast.bos.service.take_delivery;

import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by 宝宝心里苦丶 on 2018/1/5.
 */
public interface PromotionService {
    void save(Promotion model);

    Page<Promotion> findPageData(Pageable pageable);
}
