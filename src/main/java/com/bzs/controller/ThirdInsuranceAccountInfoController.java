package com.bzs.controller;
import com.bzs.dao.ThirdInsuranceAccountInfoMapper;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.ThirdInsuranceAccountInfo;
import com.bzs.service.ThirdInsuranceAccountInfoService;
import com.bzs.utils.UUIDS;
import com.bzs.utils.aspect.AspectInterface;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by dl on 2019/05/06.
*/
@RestController
@RequestMapping("/thirdAccount")
public class ThirdInsuranceAccountInfoController {
    @Resource
    private ThirdInsuranceAccountInfoService thirdInsuranceAccountInfoService;
    @Resource
    private ThirdInsuranceAccountInfoMapper thirdInsuranceAccountInfoMapper;

    @PostMapping("/add")
    public Result add(ThirdInsuranceAccountInfo thirdInsuranceAccountInfo) {
        thirdInsuranceAccountInfo.setThirdInsuranceId(UUIDS.getUUID());
        thirdInsuranceAccountInfoService.save(thirdInsuranceAccountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(String accountId) {
        thirdInsuranceAccountInfoService.deleteAccount(accountId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ThirdInsuranceAccountInfo thirdInsuranceAccountInfo) {
        thirdInsuranceAccountInfoService.update(thirdInsuranceAccountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ThirdInsuranceAccountInfo thirdInsuranceAccountInfo = thirdInsuranceAccountInfoService.findById(id);
        return ResultGenerator.genSuccessResult(thirdInsuranceAccountInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,ThirdInsuranceAccountInfo thirdInsuranceAccountInfo) {
        PageHelper.startPage(page, size);
        List<ThirdInsuranceAccountInfo> list = thirdInsuranceAccountInfoService.select(thirdInsuranceAccountInfo);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation("添加")
    @PostMapping("/addAccount")
    public Result addAccount (@RequestParam String createBy,@RequestParam String accountName,@RequestParam String accountPwd,@RequestParam String accountId,@RequestParam(defaultValue = "0") String level){
        ThirdInsuranceAccountInfo accountInfo=new ThirdInsuranceAccountInfo();
        accountInfo.setAccountId(accountId);
        accountInfo.setAccountName(accountName);
        accountInfo.setAccountPwd(accountPwd);
        accountInfo.setCreateId(createBy);
        accountInfo.setLevel(level);
        return thirdInsuranceAccountInfoService.addOrUpdate(accountInfo,createBy);
    }

    @ApiOperation("添加或者修改，数据为空不修改")
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate (ThirdInsuranceAccountInfo accountInfo,String createBy){
        return thirdInsuranceAccountInfoService.addOrUpdate(accountInfo,createBy);
    }

    /**
     * 通过createBy获取分页列表
     * @param page
     * @param size
     * @param createBy
     * @return
     */
    @PostMapping("/listByCreateBy")
    public Result listByCreateBy(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,String  createBy) {
        PageHelper.startPage(page, size);
        List<ThirdInsuranceAccountInfo> list = thirdInsuranceAccountInfoService.selectByCreateBy(createBy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation("通过id修改信息，数据为空不修改")
    @PostMapping("/updateById")
    public Result updateById (ThirdInsuranceAccountInfo thirdInsuranceAccountInfo){
        return thirdInsuranceAccountInfoService.updateById(thirdInsuranceAccountInfo);
    }

    @ApiOperation("批量删除")
    @PostMapping("/deleteBatch")
    public Result deleteBatch(String ids,String createBy){
        return thirdInsuranceAccountInfoService.deleteBatch(ids,createBy);
    }


    @ApiOperation("查询账号下的所有保险账号")
    @PostMapping("/queryConditions")
    public Result queryConditionsPage(ThirdInsuranceAccountInfo accountInfo) {
        List<ThirdInsuranceAccountInfo> list = thirdInsuranceAccountInfoService.queryConditions(accountInfo);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     *
     * @param source 要查询的保司的枚举值 1太保2平安4人保
     * @param accountId 账号id
     * @return
     */
    @ApiOperation("查询账号下的指定保司的可用保险账号")
    @PostMapping("/findEnbaleAccount")
    public Map findEnbaleAccount(Long source,String accountId){
        return thirdInsuranceAccountInfoService.findEnbaleAccount(source,"1",accountId);
    }

    /**
     *
     * @param accountId
     * @return
     */
    @ApiOperation("获取指定账号下的每家保险公司的可用账号")
    @PostMapping("/findDifferSourceAccount")
    public Map findDifferSourceAccount(String accountId){
        return thirdInsuranceAccountInfoService.findDifferSourceAccount(accountId,"1");
    }

    @ApiOperation("获取爬取数据的账号")
    @PostMapping("/getCrawlingAdminList")
//    public Result getCrawlingAdminList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size,@RequestParam String createBy){
    public Result getCrawlingAdminList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        PageHelper.startPage(page, size);
        List list = thirdInsuranceAccountInfoService.getCrawlingAndAdminList();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation("根据角色获取爬取账号信息")
    @PostMapping("/getCrawlingAndAccountList")
    public Result getCrawlingAndAccountList(@RequestParam String createBy){
        List list = thirdInsuranceAccountInfoService.getCrawlingAndAdminList(createBy);
        return ResultGenerator.genSuccessResult(list);
    }
    @ApiOperation("根据角色获取爬取账号信息")
    @PostMapping("/getAccountById")
    public Result getAccountById(@RequestParam String id){
        List list = thirdInsuranceAccountInfoMapper.getAccountById(id);
        return ResultGenerator.genSuccessResult(list);
    }

}
