package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CommissionEveryDay;
import com.bzs.service.CommissionEveryDayService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by dl on 2019/07/03.
*/
@RestController
@RequestMapping("/commission/every/day")
public class CommissionEveryDayController {
    @Resource
    private CommissionEveryDayService commissionEveryDayService;

    @PostMapping("/add")
    public Result add(CommissionEveryDay commissionEveryDay) {
        commissionEveryDayService.save(commissionEveryDay);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        commissionEveryDayService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(CommissionEveryDay commissionEveryDay) {
        commissionEveryDayService.update(commissionEveryDay);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        CommissionEveryDay commissionEveryDay = commissionEveryDayService.findById(id);
        return ResultGenerator.genSuccessResult(commissionEveryDay);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<CommissionEveryDay> list = commissionEveryDayService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
