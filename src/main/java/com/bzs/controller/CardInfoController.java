package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CardInfo;
import com.bzs.service.CardInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by dl on 2019/07/05.
*/
@RestController
@RequestMapping("/card/info")
public class CardInfoController {
    @Resource
    private CardInfoService cardInfoService;

    @PostMapping("/add")
    public Result add(CardInfo cardInfo) {
        cardInfoService.save(cardInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        cardInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(CardInfo cardInfo) {
        cardInfoService.update(cardInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        CardInfo cardInfo = cardInfoService.findById(id);
        return ResultGenerator.genSuccessResult(cardInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<CardInfo> list = cardInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
