package com.bzs.controller;
import com.bzs.dao.SysParamMapper;
import com.bzs.model.Address;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.SysParam;
import com.bzs.service.SysParamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/12/02.
*/
@RestController
@RequestMapping("/sys/param")
public class SysParamController {
    @Resource
    private SysParamService sysParamService;
    @Resource
    private SysParamMapper sysParamMapper;

    @PostMapping("/add")
    public Result add(SysParam sysParam) {
        sysParamService.save(sysParam);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        sysParamService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/getShowToday")
    public Result getShowToday() {
        return ResultGenerator.genSuccessResult(sysParamService.getShowToday());
    }
    @PostMapping("/getRole")
    public Result getRole() {
        return ResultGenerator.genSuccessResult(sysParamService.getRole());
    }
    @PostMapping("/update")
    public Result update(SysParam sysParam) {
        Condition example=new Condition(SysParam.class);
        Example.Criteria  criteria= example.createCriteria();
        criteria.andCondition("param_key=", sysParam.getParamKey());
        sysParamMapper.updateByCondition(sysParam,example);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail( String paramKey) {
        return ResultGenerator.genSuccessResult(sysParamMapper.getParamValue(paramKey));
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysParam> list = sysParamService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
