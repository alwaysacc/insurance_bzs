package com.bzs.controller;

import com.bzs.model.AccountInfo;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.InsuredInfo;
import com.bzs.service.InsuredInfoService;
import com.bzs.utils.UUIDS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by denglei on 2019/04/10 17:09:11.
 * =======
 * import org.springframework.web.bind.annotation.PostMapping;
 * import org.springframework.web.bind.annotation.RequestMapping;
 * import org.springframework.web.bind.annotation.RequestParam;
 * import org.springframework.web.bind.annotation.RestController;
 * <p>
 * import javax.annotation.Resource;
 * import java.util.List;
 * <p>
 * /**
 * Created by alwaysacc on 2019/04/11.
 */
@RestController
@RequestMapping("/insuredinfo")
public class InsuredInfoController {
    @Resource
    private InsuredInfoService insuredInfoService;

    @PostMapping("/add")
    public Result add(InsuredInfo insuredInfo) {
        insuredInfoService.save(insuredInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        insuredInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * @return com.bzs.utils.Result
     * @Author 孙鹏程
     * @Description // 修改车主，投保人，被保险人信息
     * @Date 2019/4/18/018  14:00
     * @Param [insuredInfo]
     **/
    @PostMapping("/updateInsuredInfo")
    public Result updateInsuredInfo(InsuredInfo insuredInfo) {
        insuredInfoService.update(insuredInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InsuredInfo insuredInfo = insuredInfoService.findById(id);
        return ResultGenerator.genSuccessResult(insuredInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InsuredInfo> list = insuredInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * @param checkType      查询类型0车牌1车架
     * @param carNo          车牌号
     * @param idCard         身份证后6位
     * @param vinNo          车架号
     * @param engineNo       发动机号
     * @param lastYearSource 上年续保公司
     * @param insuredArea
     * @param request
     * @param createBy       创建人
     * @return
     */

    @PostMapping("/checkByCarNoOrVinNo")
    public Result list(@RequestParam String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, HttpServletRequest request, String createBy) {
        if (StringUtils.isNotBlank(createBy))
            return insuredInfoService.checkByCarNoOrVinNo2(checkType, carNo, idCard, vinNo, engineNo, lastYearSource, insuredArea, createBy,null);
        else return ResultGenerator.genFailResult("未获取到账号信息");
    }

    @ApiOperation("插入或更新")
    @PostMapping("/insertOrUpdate")
    public Result insertOrUpdate(InsuredInfo insuredInfo) {
        int result = insuredInfoService.insertOrUpdate(insuredInfo);
        if (result > 0) {
            return ResultGenerator.genSuccessResult(result, "成功");
        } else {
            return ResultGenerator.genFailResult("操作失败");
        }

    }

}
