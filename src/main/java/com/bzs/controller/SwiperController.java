package com.bzs.controller;
import com.bzs.utils.QiniuCloudUtil;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Swiper;
import com.bzs.service.SwiperService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/07/23.
*/
@RestController
@RequestMapping("/swiper")
public class SwiperController {
    @Resource
    private SwiperService swiperService;

    @PostMapping("/add")
    public Result add(Swiper swiper,@RequestParam(value = "file", required = false) MultipartFile file) {
        return ResultGenerator.genSuccessResult(swiperService.addSwiper(swiper,file));
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id,String imgUrl) {
        try {
            QiniuCloudUtil.delete(imgUrl);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        swiperService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Swiper swiper) {
        swiperService.update(swiper);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Swiper swiper = swiperService.findById(id);
        return ResultGenerator.genSuccessResult(swiper);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Swiper> list = swiperService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getListByOrderNum")
    public Result getListByOrderNum() {
        return ResultGenerator.genSuccessResult(swiperService.getListByOrderNum());
    }

}
