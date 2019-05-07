package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.ThirdInsuranceAccountDateInfo;
import com.bzs.service.ThirdInsuranceAccountDateInfoService;
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
@RequestMapping("/thirdAccount")
public class ThirdInsuranceAccountDateInfoController {
    @Resource
    private ThirdInsuranceAccountDateInfoService thirdInsuranceAccountDateInfoService;

    @PostMapping("/add")
    public Result add(ThirdInsuranceAccountDateInfo thirdInsuranceAccountDateInfo) {
        thirdInsuranceAccountDateInfoService.save(thirdInsuranceAccountDateInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        thirdInsuranceAccountDateInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ThirdInsuranceAccountDateInfo thirdInsuranceAccountDateInfo) {
        thirdInsuranceAccountDateInfoService.update(thirdInsuranceAccountDateInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ThirdInsuranceAccountDateInfo thirdInsuranceAccountDateInfo = thirdInsuranceAccountDateInfoService.findById(id);
        return ResultGenerator.genSuccessResult(thirdInsuranceAccountDateInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ThirdInsuranceAccountDateInfo> list = thirdInsuranceAccountDateInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/savebyProcedure")
    public Result save(ThirdInsuranceAccountDateInfo info){
        return thirdInsuranceAccountDateInfoService.saveByProcedure(info);
    }
}
