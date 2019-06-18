package com.bzs.controller;

import com.bzs.model.router.VueRouter;
import com.bzs.utils.FebsException;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.TMenu;
import com.bzs.service.TMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dl on 2019/06/11.
 */
@RestController
@RequestMapping("/menu")
public class TMenuController {
    private static Logger log=LoggerFactory.getLogger(TMenuController.class);
    private String message="";
    @Resource
    private TMenuService tMenuService;

    @PostMapping("/add")
    public Result add(TMenu tMenu) {
        tMenuService.save(tMenu);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        tMenuService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(TMenu tMenu) {
        tMenuService.update(tMenu);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TMenu tMenu = tMenuService.findById(id);
        return ResultGenerator.genSuccessResult(tMenu);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TMenu> list = tMenuService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/findUserPermissions")
    public Result findUserPermissions(String accountId) {
        List<TMenu> list = tMenuService.findUserPermissions(accountId);
        if (CollectionUtils.isNotEmpty(list)) {
            return ResultGenerator.genSuccessResult(list, "成功");
        } else {
            return ResultGenerator.genFailResult("获取失败");
        }
    }
    @PostMapping("/findUserMenus")
    public Result findUserMenus(String accountId) {
        List<TMenu> list = tMenuService.findUserMenus(accountId);
        if (CollectionUtils.isNotEmpty(list)) {
            return ResultGenerator.genSuccessResult(list, "成功");
        } else {
            return ResultGenerator.genFailResult("获取失败");
        }
    }

    @GetMapping
    @RequiresPermissions("menu:view")
    public Map<String, Object> menuList(TMenu menu) {
        return this.tMenuService.findMenus(menu);
    }

   /* @GetMapping("/{username}")
    public ArrayList<VueRouter<TMenu>> getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userManager.getUserRouters(username);
    }*/


    @ApiOperation("新增菜单/按钮")
    @PostMapping
    @RequiresPermissions("menu:add")
    public void addMenu(@Valid TMenu menu) throws FebsException {
        try {
            this.tMenuService.createMenu(menu);
        } catch (Exception e) {
            message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation("删除菜单/按钮")
    @DeleteMapping("/{menuIds}")
    @RequiresPermissions("menu:delete")
    public void deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) throws FebsException {
        try {
            String[] ids = menuIds.split(",");
            this.tMenuService.deleteMeuns(ids);
        } catch (Exception e) {
            message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation("修改菜单/按钮")
    @PutMapping
    @RequiresPermissions("menu:update")
    public void updateMenu(@Valid TMenu menu) throws FebsException {
        try {
            this.tMenuService.updateMenu(menu);
        } catch (Exception e) {
            message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("menu:export")
    public void export(TMenu menu, HttpServletResponse response) throws FebsException {
        try {
            List<TMenu> menus = this.tMenuService.findMenuList(menu);
            ExcelKit.$Export(TMenu.class, response).downXlsx(menus, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
