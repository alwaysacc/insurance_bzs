package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.AdminMenu;
import com.bzs.service.AdminMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by dl on 2019/06/18.
*/
@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController {
    @Resource
    private AdminMenuService adminMenuService;

    @PostMapping("/add")
    public Result add(AdminMenu adminMenu) {
        adminMenuService.save(adminMenu);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        adminMenuService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(AdminMenu adminMenu) {
        adminMenuService.update(adminMenu);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        AdminMenu adminMenu = adminMenuService.findById(id);
        return ResultGenerator.genSuccessResult(adminMenu);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AdminMenu> list = adminMenuService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getMenuByAdminName")
    public Result getMenuByAdminName(@RequestParam String adminName) {
        return ResultGenerator.genSuccessResult(adminMenuService.getMenuByAdminName(adminName));
    }
}
