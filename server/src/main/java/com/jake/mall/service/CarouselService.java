
package com.jake.mall.service;

import com.jake.mall.api.mall.vo.IndexCarouselVO;
import com.jake.mall.entity.Carousel;
import com.jake.mall.util.PageQueryUtil;
import com.jake.mall.util.PageResult;

import java.util.List;

public interface CarouselService {

    /**
     * Returns a fixed number of boot map objects (home page call)
     *
     * @param number
     * @return
     */
    List<IndexCarouselVO> getCarouselsForIndex(int number);

    /**
     * pagination
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Long[] ids);
}
