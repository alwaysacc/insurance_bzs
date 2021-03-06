package com.bzs.controller;
import com.bzs.dao.VerificationMapper;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Verification;
import com.bzs.service.VerificationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
* Created by dl on 2019/06/14.
*/
@RestController
@RequestMapping("/verification")
public class VerificationController {
    @Resource
    private VerificationService verificationService;
    @Resource
    private VerificationMapper verificationMapper;
    @PostMapping("/add")
    public Result add(Verification verification) {
        verificationService.save(verification);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        verificationService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Verification verification) {
        verificationService.update(verification);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/updateVerification")
    @ApiOperation("修改字段为空时不修改此字段")
    public Result updateVerification(Verification verification) {
        if(null!=verification&&null!=verification.getId()){
            int result=verificationService.updateVerification(verification);
            if(1==result){
                return ResultGenerator.genSuccessResult(verification,result+"");
            }
            return ResultGenerator.genFailResult("修改失败，信息不存在");
        }else{
            return ResultGenerator.genFailResult("参数异常，修改失败");
        }

    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Verification verification = verificationService.findById(id);
        return ResultGenerator.genSuccessResult(verification);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Verification> list = verificationService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getVerificationList")
    public Result getVerificationList(String accountId,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Verification> list = verificationService.getVerificationList(accountId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getListByAdmin")
    public Result getListByAdmin(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "0") Integer size,
                                 String userName) {
        PageHelper.startPage(page, size);
        List<Verification> list = verificationMapper.getListByAdmin(userName);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/updateVerificationStatus")
    public Result updateVerificationStatus(String[] id, String status,String userName, String isPay) {
        return ResultGenerator.genSuccessResult(verificationService.updateVerificationStatus(id,status,userName,isPay));
    }
    @PostMapping("/getVeriftcationDetail")
    public Result getVeriftcationDetail(int id) {
        return ResultGenerator.genSuccessResult(verificationMapper.getVeriftcationDetail(id));
    }
}
