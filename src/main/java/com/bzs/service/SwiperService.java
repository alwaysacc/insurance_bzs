package com.bzs.service;
import com.bzs.model.Swiper;
import com.bzs.utils.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Created by alwaysacc on 2019/07/23.
 */
public interface SwiperService extends Service<Swiper> {

    int addSwiper(Swiper swiper, MultipartFile file);

    List<Swiper> getListByOrderNum();
}
