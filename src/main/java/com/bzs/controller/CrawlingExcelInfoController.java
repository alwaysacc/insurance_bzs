package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.service.CrawlingExcelInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by dl on 2019/06/20.
*/
@RestController
@RequestMapping("/crawling/excelinfo")
public class CrawlingExcelInfoController {
    @Resource
    private CrawlingExcelInfoService crawlingExcelInfoService;

    @PostMapping("/add")
    public Result add(CrawlingExcelInfo crawlingExcelInfo) {
        crawlingExcelInfoService.save(crawlingExcelInfo);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/save")
    public Result save(CrawlingExcelInfo crawlingExcelInfo) {
        crawlingExcelInfoService.add(crawlingExcelInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        crawlingExcelInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(CrawlingExcelInfo crawlingExcelInfo) {
        crawlingExcelInfoService.update(crawlingExcelInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        CrawlingExcelInfo crawlingExcelInfo = crawlingExcelInfoService.findById(id);
        return ResultGenerator.genSuccessResult(crawlingExcelInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<CrawlingExcelInfo> list = crawlingExcelInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }



}
