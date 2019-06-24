package com.bzs.controller;

import com.bzs.model.TMenu;
import com.bzs.service.TMenuService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Role;
import com.bzs.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dl on 2019/06/16.
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private TMenuService tMenuService;


    @PostMapping("/add")
    public Result add(Role role) {
        roleService.save(role);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        roleService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Role role) {
        roleService.update(role);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Role role = roleService.findById(id);
        return ResultGenerator.genSuccessResult(role);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Role> list = roleService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/findUserRole")
    public Result findUserRole(String accountId) {
        List<TMenu> list = tMenuService.findUserMenus(accountId);
        if (CollectionUtils.isNotEmpty(list)) {
            return ResultGenerator.genSuccessResult(list, "成功");
        } else {
            return ResultGenerator.genFailResult("获取失败");
        }
    }

    @PostMapping("/findUserRoleByAccountId")
    public Result findUserRoleByAccountId(String accountId) {
        List list = roleService.findUserRoleByAccountId(accountId);
        if (CollectionUtils.isNotEmpty(list)) {
            return ResultGenerator.genSuccessResult(list, "成功");
        } else {
            return ResultGenerator.genFailResult("获取失败");
        }
    }
}
