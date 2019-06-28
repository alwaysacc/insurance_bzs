package com.bzs.controller;
import com.bzs.dao.AdminMenuMapper;
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
    @Resource
    private AdminMenuMapper adminMenuMapper;
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

    @PostMapping("/getMenuList")
    public Result getMenuList() {
        return ResultGenerator.genSuccessResult(adminMenuService.getMenu());
    }
    @PostMapping("/getMenuIdByRoleId")
    public Result getMenuIdByRoleId(Long id) {
        return ResultGenerator.genSuccessResult(adminMenuMapper.getMenuIdByRoleId(id));
    }
    @PostMapping("/getMenuByAdminName")
    public Result getMenuByAdminName(@RequestParam String adminName) {
        return ResultGenerator.genSuccessResult(adminMenuService.getMenuByAdminName(adminName));
    }
}
