package com.bzs.dao;

import com.bzs.model.Swiper;
import com.bzs.utils.Mapper;

import java.util.List;

public interface SwiperMapper extends Mapper<Swiper> {

    List<Swiper> getListByOrderNum();
}