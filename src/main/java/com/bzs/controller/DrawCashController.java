package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.DrawCash;
import com.bzs.service.DrawCashService;
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
* Created by dl on 2019/06/13.
*/
@RestController
@RequestMapping("/draw/cash")
public class DrawCashController {
    @Resource
    private DrawCashService drawCashService;

    @PostMapping("/add")
    public Result add(DrawCash drawCash) {
        drawCashService.save(drawCash);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        drawCashService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(DrawCash drawCash) {
        drawCashService.update(drawCash);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        DrawCash drawCash = drawCashService.findById(id);
        return ResultGenerator.genSuccessResult(drawCash);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DrawCash> list = drawCashService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/insertBatch")
    @ApiOperation("批量添加")
    public Map insertBatch(String list,String createBy){
        return drawCashService.insertBatch(list,createBy);
    }
}