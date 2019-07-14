package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.IdCardImg;
import com.bzs.service.IdCardImgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by dl on 2019/07/14.
*/
@RestController
@RequestMapping("/idCardImg")
public class IdCardImgController {
    @Resource
    private IdCardImgService idCardImgService;



    @PostMapping("/add")
    public Result add(IdCardImg idCardImg) {
        idCardImgService.save(idCardImg);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        idCardImgService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(IdCardImg idCardImg) {
        idCardImgService.update(idCardImg);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        IdCardImg idCardImg = idCardImgService.findById(id);
        return ResultGenerator.genSuccessResult(idCardImg);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<IdCardImg> list = idCardImgService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getWaitCheckList")
    public Result getWaitCheckList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                   int verifiedStat) {
        PageHelper.startPage(page, size);
        List list = idCardImgService.getWaitCheckList(verifiedStat);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getVerifiedStatById")
    public Result getVerifiedStatById(String accountId) {
        return ResultGenerator.genSuccessResult(idCardImgService.getVerifiedStatById(accountId));
    }

}
