package com.bzs.controller;

import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.InsuranceTypeInfo;
import com.bzs.service.InsuranceTypeInfoService;
import com.bzs.utils.UUIDS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alwaysacc on 2019/04/12.
 */
@RestController
@RequestMapping("/insurance/type/info")
public class InsuranceTypeInfoController {
    @Resource
    private InsuranceTypeInfoService insuranceTypeInfoService;

    @PostMapping("/add")
    public Result add(InsuranceTypeInfo insuranceTypeInfo) {
        insuranceTypeInfoService.save(insuranceTypeInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        insuranceTypeInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(InsuranceTypeInfo insuranceTypeInfo) {
        insuranceTypeInfoService.update(insuranceTypeInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InsuranceTypeInfo insuranceTypeInfo = insuranceTypeInfoService.findById(id);
        return ResultGenerator.genSuccessResult(insuranceTypeInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InsuranceTypeInfo> list = insuranceTypeInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/batchinserttest")
    public Map<String, Object> insertBatchTest() {
        List<InsuranceTypeInfo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            InsuranceTypeInfo a = new InsuranceTypeInfo();
            list.add(a);
        }
        int count = insuranceTypeInfoService.insertBatch(list);
        Map<String, Object> result = new HashMap<>();
        result.put("status", 1);
        result.put("msg", "成功数量=" + count);
        return result;
    }

    @ApiOperation("批量插入或更新")
    @PostMapping("/batchInsertOrUpdateList")
    public Result batchInsertOrUpdateList() {
        List<InsuranceTypeInfo> info = new ArrayList<InsuranceTypeInfo>();
        InsuranceTypeInfo insuranceTypeInfo=new InsuranceTypeInfo();
        insuranceTypeInfo.setInsuranceName("ss"+UUIDS.getDateUUID());
        info.add(insuranceTypeInfo);
       /* for(int i=0;i<3;i++){
            insuranceTypeInfo.setInsuranceTypeId(UUIDS.getDateUUID());
        }*/
        boolean b = insuranceTypeInfoService.batchInsertOrUpdateList(info);
        if (b) {
            return ResultGenerator.genSuccessResult("成功");
        } else {
            return ResultGenerator.genFailResult("失败");
        }

    }

}
