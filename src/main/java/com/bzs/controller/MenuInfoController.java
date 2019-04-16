package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.MenuInfo;
import com.bzs.service.MenuInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/menu/info")
public class MenuInfoController {
    @Resource
    private MenuInfoService menuInfoService;

    @PostMapping("/asd")
    public Result asd(String username) {

        return ResultGenerator.genSuccessResult(menuInfoService.getUserPermissions(username));
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        menuInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(MenuInfo menuInfo) {
        menuInfoService.update(menuInfo);
        return ResultGenerator.genSuccessResult();
    }
    @RequiresPermissions("dept:aa")
    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        MenuInfo menuInfo = menuInfoService.findById(id);
        return ResultGenerator.genSuccessResult(menuInfo);
    }
    @RequiresPermissions("dept:list")
    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MenuInfo> list = menuInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
