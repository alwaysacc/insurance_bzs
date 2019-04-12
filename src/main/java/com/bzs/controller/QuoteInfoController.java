package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.QuoteInfo;
import com.bzs.service.QuoteInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by alwaysacc on 2019/04/11.
*/
@RestController
@RequestMapping("/quote/info")
public class QuoteInfoController {
    @Resource
    private QuoteInfoService quoteInfoService;

    @PostMapping("/quoteDetails")
    public Result quoteDetails(String carInfoId) {
       Map map= quoteInfoService.quoteDetails(carInfoId);
       return ResultGenerator.genSuccessResult(map);
    }
    @PostMapping("/add")
    public Result add(QuoteInfo quoteInfo) {
        quoteInfoService.save(quoteInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        quoteInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(QuoteInfo quoteInfo) {
        quoteInfoService.update(quoteInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        QuoteInfo quoteInfo = quoteInfoService.findById(id);
        return ResultGenerator.genSuccessResult(quoteInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<QuoteInfo> list = quoteInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
