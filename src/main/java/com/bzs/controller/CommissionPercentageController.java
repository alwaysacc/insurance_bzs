package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CommissionPercentage;
import com.bzs.service.CommissionPercentageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by dl on 2019/06/12.
*/
@RestController
@RequestMapping("/commission/percentage")
public class CommissionPercentageController {
    @Resource
    private CommissionPercentageService commissionPercentageService;

    @PostMapping("/add")
    public Result add(CommissionPercentage commissionPercentage) {
        commissionPercentageService.save(commissionPercentage);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        commissionPercentageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(CommissionPercentage commissionPercentage) {
        commissionPercentageService.update(commissionPercentage);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        CommissionPercentage commissionPercentage = commissionPercentageService.findById(id);
        return ResultGenerator.genSuccessResult(commissionPercentage);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<CommissionPercentage> list = commissionPercentageService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/addOrUpdate")
    @ApiOperation("添加或更新")
    public Map  addOrUpdate(CommissionPercentage domain){
        return commissionPercentageService.addOrUpdate(domain);
    }
    @PostMapping("/getLastUpdateData")
    @ApiOperation("获取最新的一条数据")
    public CommissionPercentage  getLastUpdateData(){
        return commissionPercentageService.getLastUpdateData();
    }
    @PostMapping("/select")
    @ApiOperation("根据条件查询")
    public List<CommissionPercentage>select(CommissionPercentage domain){
        return  commissionPercentageService.select(domain);
    }

}
