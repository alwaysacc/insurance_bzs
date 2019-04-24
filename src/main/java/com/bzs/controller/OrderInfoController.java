package com.bzs.controller;
import com.bzs.model.OrderInfo;
import com.bzs.service.OrderInfoService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/04/12.
*/
@RestController
@RequestMapping("/orderinfo")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * @Author 孙鹏程
     * @Description  获取订单列表 payStatus订单支付状态
     * @Date 2019/4/12/012  15:52
     * @Param [accountId, payStatus, page, size]
     * @return com.bzs.utils.Result
     **/

    @ApiOperation("获取订单列表")
    @PostMapping("/getOrderList")
    public Result getOrderList(String accountId, @RequestParam(defaultValue = "0")int payStatus,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List list=orderInfoService.getOrderList(accountId,payStatus);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    /**
     * @Author 孙鹏程
     * @Description
     * @Date 2019/4/12/012  15:53
     * @Param [accountId, payStatus, page, size]
     * @return com.bzs.utils.Result
     **/
    @ApiOperation("搜索订单列表")
    @PostMapping("/searchOrderList")
    public Result searchOrderList(String accountId, @RequestParam(defaultValue = "0")int payStatus,String carNumber, String postedName, int deliveryWay, String insuranceCompany) {
        List list=orderInfoService.searchOrderList(accountId,payStatus,carNumber,postedName,deliveryWay,insuranceCompany);
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping("/add")
    public Result add(OrderInfo orderInfo) {
        orderInfoService.save(orderInfo);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        orderInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(OrderInfo orderInfo) {
        orderInfoService.update(orderInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OrderInfo orderInfo = orderInfoService.findById(id);
        return ResultGenerator.genSuccessResult(orderInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OrderInfo> list = orderInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
