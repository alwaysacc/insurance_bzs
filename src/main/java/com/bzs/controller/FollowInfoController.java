package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.FollowInfo;
import com.bzs.service.FollowInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
* Created by dl on 2019/07/15.
*/
@RestController
@RequestMapping("/followInfo")
public class FollowInfoController {
    @Resource
    private FollowInfoService followInfoService;

    /**
     * 根据车辆id获取跟进信息
     * @param carInfoId
     * @return
     */
    @ApiOperation("根据车辆id获取跟进信息")
    @PostMapping("/getFollowInfoByCarInfoId")
    public Result getFollowInfoByCarInfoId(@RequestParam String carInfoId,
                                           @RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "0") Integer size
                                           ) {
        PageHelper.startPage(page, size);
        List<FollowInfo> list = followInfoService.getFollowInfoByCarInfoId(carInfoId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/add")
    public Result add(FollowInfo followInfo) {
        followInfoService.save(followInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        followInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(FollowInfo followInfo) {
        followInfoService.update(followInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        FollowInfo followInfo = followInfoService.findById(id);
        return ResultGenerator.genSuccessResult(followInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<FollowInfo> list = followInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
