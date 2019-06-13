package com.bzs.controller;
import com.bzs.model.AccountInfo;
import com.bzs.model.OrderInfo;
import com.bzs.service.OrderInfoService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
        List<OrderInfo> list=orderInfoService.getOrderList(accountId,payStatus);
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
    @ApiOperation("获取订单详情")
    @PostMapping("/getOrderDetail")
    public Result getOrderDetail(String orderId,String quoteId) {
        return ResultGenerator.genSuccessResult(orderInfoService.orderDetails(orderId,quoteId));
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

    /**
     *
     * @param createBy
     * @return
     */
    @PostMapping("/getNextLevelOrder")
    @ApiOperation("获取下两级订单，包括自己的订单，一共三级订单")
    public Map<String,Object> getNextLevelOrder(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,String createBy,String orderStatus){
        Map map=orderInfoService.getNextLevelOrder(createBy);
        PageHelper.startPage(page, size);
        PageInfo pageInfo=null;
        if ( ((String)map.get("code")).equals("200")){
            pageInfo = new PageInfo((List) map.get("data"));
        }
        map.put("data",pageInfo);
        return map;
    }
}
