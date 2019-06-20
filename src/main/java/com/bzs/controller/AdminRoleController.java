package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.AdminRole;
import com.bzs.service.AdminRoleService;
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
@RequestMapping("/admin/role")
public class AdminRoleController {
    @Resource
    private AdminRoleService adminRoleService;

    @PostMapping("/addRoleAndMenu")
    public Result addRoleAndMenu(AdminRole adminRole,String menuId) {
        return ResultGenerator.genSuccessResult(adminRoleService.addRoleAndMenu(adminRole,menuId));
    }
    @PostMapping("/updateRoleAndMenu")
    public Result updateRoleAndMenu(AdminRole adminRole,String menuId,String beforeMenuId) {
        adminRoleService.updateRoleAndMenu(adminRole,menuId,beforeMenuId);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/deleteRole")
    public Result deleteRole(Long roleId) {
        adminRoleService.deleteRole(roleId);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/add")
    public Result add(AdminRole adminRole) {
        adminRoleService.save(adminRole);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        adminRoleService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(AdminRole adminRole) {
        adminRoleService.update(adminRole);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        AdminRole adminRole = adminRoleService.findById(id);
        return ResultGenerator.genSuccessResult(adminRole);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AdminRole> list = adminRoleService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
