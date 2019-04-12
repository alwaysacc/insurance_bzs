package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.TemporaryRelationshipInfo;
import com.bzs.service.TemporaryRelationshipInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/04/11.
*/
@RestController
@RequestMapping("/temporary/relationship/info")
public class TemporaryRelationshipInfoController {
    @Resource
    private TemporaryRelationshipInfoService temporaryRelationshipInfoService;
    /**
     * @Author 孙鹏程
     * @Description //TODO 添加临时公户或个人
     * @Date 2019/4/11/011  17:45
     * @Param [temporaryRelationshipInfo]
     * @return com.bzs.utils.Result
     **/
    @ApiOperation("添加临时公户或个人")
    @PostMapping("/addTemRelation")
    public Result add(TemporaryRelationshipInfo temporaryRelationshipInfo) {
        temporaryRelationshipInfoService.save(temporaryRelationshipInfo);
        return ResultGenerator.genSuccessResult();
    }
    /**
     * @Author 孙鹏程
     * @Description //TODO 获取临时公户和临时个人
     * @Date 2019/4/11/011  17:45
     * @Param [carInfoId]
     * @return com.bzs.utils.Result
     **/
    @ApiOperation("获取临时公户和临时个人")
    @PostMapping("/getTemRelation")
    public Result getTemRelation(String  carInfoId) {
        return ResultGenerator.genSuccessResult(temporaryRelationshipInfoService.findBy("carInfoId",carInfoId));
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        temporaryRelationshipInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(TemporaryRelationshipInfo temporaryRelationshipInfo) {
        temporaryRelationshipInfoService.update(temporaryRelationshipInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TemporaryRelationshipInfo temporaryRelationshipInfo = temporaryRelationshipInfoService.findById(id);
        return ResultGenerator.genSuccessResult(temporaryRelationshipInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TemporaryRelationshipInfo> list = temporaryRelationshipInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
