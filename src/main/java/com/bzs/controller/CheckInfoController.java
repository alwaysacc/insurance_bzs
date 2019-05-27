package com.bzs.controller;

import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CheckInfo;
import com.bzs.service.CheckInfoService;
import com.bzs.utils.UUIDS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by alwaysacc on 2019/04/12.
 */
@RestController
@RequestMapping("/check/info")
public class CheckInfoController {
    @Resource
    private CheckInfoService checkInfoService;

    @PostMapping("/add")
    public Result add(CheckInfo checkInfo) {
        checkInfoService.save(checkInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        checkInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(CheckInfo checkInfo) {
        checkInfoService.update(checkInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        CheckInfo checkInfo = checkInfoService.findById(id);
        return ResultGenerator.genSuccessResult(checkInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<CheckInfo> list = checkInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @ApiOperation("更新或查询")
    @PostMapping("/updateOrAdd")
    public Result updateOrAdd(CheckInfo checkInfo) {
        if(null!=checkInfo){
            String id=checkInfo.getCheckInfoId();
            if(StringUtils.isBlank(id)){
                id=UUIDS.getDateUUID();
                checkInfo.setCheckInfoId(id);
            }
        }else{
            checkInfo=new CheckInfo();
            checkInfo.setCheckInfoId(UUIDS.getDateUUID());
        }
        Map maps = checkInfoService.updateOrAdd(checkInfo);
        String code = (String) maps.get("code");
        String msg = (String) maps.get("msg");
        if ("200".equals(code)) {
            return ResultGenerator.genSuccessResult(msg);
        } else {
            return ResultGenerator.genFailResult(msg);
        }
    }

    @ApiOperation("通过车辆id或者创建人查询")
    @PostMapping("/checkByCreateByOrCarInfoId")
    public Result checkByCreateByOrCarInfoId(String createBy,String carInfoId) {
        Map map=checkInfoService.checkByCreateByOrCarInfoId(createBy,carInfoId);
        String code=(String)map.get("code");
        String msg=(String)map.get("msg");
        if("200".equals(code)){
            return ResultGenerator.genSuccessResult(msg);
        }else{
            return ResultGenerator.genFailResult(msg);
        }
    }
}