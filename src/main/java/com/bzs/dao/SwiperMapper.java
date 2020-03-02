package com.bzs.dao;

import com.bzs.model.Swiper;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SwiperMapper extends Mapper<Swiper> {

    List<Swiper> getListByOrderNum();

    List<Swiper> getListByType(@Param("type") Integer type);
}
