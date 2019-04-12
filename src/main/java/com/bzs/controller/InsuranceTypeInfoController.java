package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.InsuranceTypeInfo;
import com.bzs.service.InsuranceTypeInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/04/12.
*/
@RestController
@RequestMapping("/insurance/type/info")
public class InsuranceTypeInfoController {
    @Resource
    private InsuranceTypeInfoService insuranceTypeInfoService;

    @PostMapping("/add")
    public Result add(InsuranceTypeInfo insuranceTypeInfo) {
        insuranceTypeInfoService.save(insuranceTypeInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        insuranceTypeInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(InsuranceTypeInfo insuranceTypeInfo) {
        insuranceTypeInfoService.update(insuranceTypeInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InsuranceTypeInfo insuranceTypeInfo = insuranceTypeInfoService.findById(id);
        return ResultGenerator.genSuccessResult(insuranceTypeInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InsuranceTypeInfo> list = insuranceTypeInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
