package com.bzs.service.impl;

import com.bzs.dao.SwiperMapper;
import com.bzs.model.Swiper;
import com.bzs.service.SwiperService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.QiniuCloudUtil;
import com.bzs.utils.UUIDS;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by alwaysacc on 2019/07/23.
 */
@Service
@Transactional
public class SwiperServiceImpl extends AbstractService<Swiper> implements SwiperService {
    @Resource
    private SwiperMapper swiperMapper;

    @Override
    public int addSwiper(Swiper swiper, MultipartFile file) {
        String path = QiniuCloudUtil.put64image(file, UUIDS.getUUID());
        swiper.setIsShow(0);
        swiper.setImgUrl(path);
        return swiperMapper.insert(swiper);
    }

    @Override
    public List<Swiper> getListByOrderNum() {
        return swiperMapper.getListByOrderNum();
    }
}
