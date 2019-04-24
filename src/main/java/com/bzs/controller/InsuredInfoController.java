package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.InsuredInfo;
import com.bzs.service.InsuredInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by denglei on 2019/04/10 17:09:11.
=======
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
@RequestMapping("/insuredinfo")
public class InsuredInfoController {
    @Resource
    private InsuredInfoService insuredInfoService;
    @PostMapping("/add")
    public Result add(InsuredInfo insuredInfo) {
        insuredInfoService.save(insuredInfo);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        insuredInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    /**
     * @Author 孙鹏程
     * @Description // 修改车主，投保人，被保险人信息
     * @Date 2019/4/18/018  14:00
     * @Param [insuredInfo]
     * @return com.bzs.utils.Result
     **/
    @PostMapping("/updateInsuredInfo")
    public Result updateInsuredInfo(InsuredInfo insuredInfo) {
        insuredInfoService.update(insuredInfo);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InsuredInfo insuredInfo = insuredInfoService.findById(id);
        return ResultGenerator.genSuccessResult(insuredInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InsuredInfo> list = insuredInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/checkByCarNoOrVinNo")
    public Result list(@RequestParam  String checkType,  String carNo,String idCard,String vinNo,String engineNo,String lastYearSource,String insuredArea ) {
        return insuredInfoService.checkByCarNoOrVinNo(checkType, carNo, idCard, vinNo, engineNo, lastYearSource, insuredArea);
    }
    @GetMapping("/httpGetTest")
    public Map<String,Object>result(String name,String age){
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("status",1);
        result.put("msg","成功");
        result.put("data",name);
        return result;

    }

}
