package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.AccountRoleInfo;
import com.bzs.service.AccountRoleInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
@RequestMapping("/accountRole")
public class AccountRoleInfoController {
    @Resource
    private AccountRoleInfoService accountRoleInfoService;

    @PostMapping("/add")
    public Result add(AccountRoleInfo accountRoleInfo) {
        accountRoleInfoService.save(accountRoleInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        accountRoleInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(AccountRoleInfo accountRoleInfo) {
        accountRoleInfoService.update(accountRoleInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        AccountRoleInfo accountRoleInfo = accountRoleInfoService.findById(id);
        return ResultGenerator.genSuccessResult(accountRoleInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AccountRoleInfo> list = accountRoleInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
