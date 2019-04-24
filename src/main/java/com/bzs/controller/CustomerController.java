package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Customer;
import com.bzs.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.record.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by alwaysacc on 2019/04/11.
*/
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private CustomerService customerService;

    @PostMapping("/add")
    public Result add(Customer customer) {
        System.out.println(ResultGenerator.genSuccessResult(customer));
        customerService.save(customer);
        return ResultGenerator.genSuccessResult(customer);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        customerService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    /**
     * @Author 孙鹏程
     * @Description //TODO 修改客户信息
     * @Date 2019/4/11/011  15:55 
     * @Param [customer]
     * @return com.bzs.utils.Result
     **/
    @ApiOperation("修改客户信息")
    @PostMapping("/updateCustomer")
    public Result update(String customer , String carInfoId) {
        JSONObject jsonObject= (JSONObject) JSONObject.parse(customer);
        System.out.println(customer);
        Customer c=JSONObject.toJavaObject(jsonObject,Customer.class);
        customerService.updateCustomer(c,carInfoId);
        return ResultGenerator.genSuccessResult(customer);
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Customer customer = customerService.findById(id);
        return ResultGenerator.genSuccessResult(customer);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Customer> list = customerService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
