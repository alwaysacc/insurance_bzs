package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.ThirdInsuranceAccountInfo;
import com.bzs.service.ThirdInsuranceAccountInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by dl on 2019/05/06.
*/
@RestController
@RequestMapping("/third/insurance/account/info")
public class ThirdInsuranceAccountInfoController {
    @Resource
    private ThirdInsuranceAccountInfoService thirdInsuranceAccountInfoService;

    @PostMapping("/add")
    public Result add(ThirdInsuranceAccountInfo thirdInsuranceAccountInfo) {
        thirdInsuranceAccountInfoService.save(thirdInsuranceAccountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        thirdInsuranceAccountInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ThirdInsuranceAccountInfo thirdInsuranceAccountInfo) {
        thirdInsuranceAccountInfoService.update(thirdInsuranceAccountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ThirdInsuranceAccountInfo thirdInsuranceAccountInfo = thirdInsuranceAccountInfoService.findById(id);
        return ResultGenerator.genSuccessResult(thirdInsuranceAccountInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ThirdInsuranceAccountInfo> list = thirdInsuranceAccountInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
