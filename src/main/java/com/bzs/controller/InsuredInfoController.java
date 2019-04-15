package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.InsuredInfo;
import com.bzs.service.InsuredInfoService;
import com.bzs.utils.UUIDS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/insured/info")
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
    @PostMapping("/update")
    public Result update(InsuredInfo insuredInfo) {
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

    /**
     *
     * @param checkType 查询类型0车牌1车架
     * @param carNo 车牌号
     * @param idCard 身份证后6位
     * @param vinNo 车架号
     * @param engineNo 发动机号
     * @param lastYearSource 上年续保公司
     * @param insuredArea
     * @param request
     * @param createdBy 创建人
     * @return
     */

    @PostMapping("/checkByCarNoOrVinNo")
    public Result list(@RequestParam  String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, HttpServletRequest request,String createdBy) {
        return insuredInfoService.checkByCarNoOrVinNo(checkType, carNo, idCard, vinNo, engineNo, lastYearSource, insuredArea,createdBy );
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
