package com.bzs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.model.CarInfo;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.QuoteInfo;
import com.bzs.service.QuoteInfoService;
import com.bzs.utils.jsontobean.InsurancesList;
import com.bzs.utils.jsontobean.ParamsData;
import com.bzs.utils.jsontobean.QuoteParmasBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        Map map = quoteInfoService.quoteDetails(carInfoId);
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

    /**
     * 获取报价信息,通过第三方接口
     * @param params
     * @param list
     * @return
     */
    @PostMapping("/getQuoteInfo")
    public Result getQuoteInfo(QuoteParmasBean params, @RequestBody(required = false)List<InsurancesList> list) {
        return quoteInfoService.getQuoteDetailsByApi(params,list);
    }
}
