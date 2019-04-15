package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.PrivilegeInfo;
import com.bzs.service.PrivilegeInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/04/15.
*/
@RestController
@RequestMapping("/privilege/info")
public class PrivilegeInfoController {
    @Resource
    private PrivilegeInfoService privilegeInfoService;

    @PostMapping("/add")
    public Result add(PrivilegeInfo privilegeInfo) {
        privilegeInfoService.save(privilegeInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        privilegeInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(PrivilegeInfo privilegeInfo) {
        privilegeInfoService.update(privilegeInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        PrivilegeInfo privilegeInfo = privilegeInfoService.findById(id);
        return ResultGenerator.genSuccessResult(privilegeInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<PrivilegeInfo> list = privilegeInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
