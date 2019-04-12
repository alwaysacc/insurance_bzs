package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.InsuranceFollowInfo;
import com.bzs.service.InsuranceFollowInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/04/11.
*/
@RestController
@RequestMapping("/insurance/follow/info")
public class InsuranceFollowInfoController {
    @Resource
    private InsuranceFollowInfoService insuranceFollowInfoService;

    @PostMapping("/add")
    public Result add(InsuranceFollowInfo insuranceFollowInfo) {
        insuranceFollowInfoService.save(insuranceFollowInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        insuranceFollowInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(InsuranceFollowInfo insuranceFollowInfo) {
        insuranceFollowInfoService.update(insuranceFollowInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InsuranceFollowInfo insuranceFollowInfo = insuranceFollowInfoService.findById(id);
        return ResultGenerator.genSuccessResult(insuranceFollowInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InsuranceFollowInfo> list = insuranceFollowInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
