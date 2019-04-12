package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CarInfo;
import com.bzs.service.CarInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/04/10.
*/
@RestController
@RequestMapping("/car/info")
public class CarInfoController {
    @Resource
    private CarInfoService carInfoService;

    @PostMapping("/add")
     public Result add(CarInfo carInfo) {
        carInfo.setCarInfoId("1213");
        carInfoService.save(carInfo);
        System.out.println(11);
        carInfoService.save(carInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        carInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    /**
     * @Author 孙鹏程
     * @Description //TODO 修改车辆信息
     * @Date 2019/4/11/011  15:58
     * @Param [carInfo]
     * @return com.bzs.utils.Result
     **/
    @ApiOperation("修改车辆信息")
    @PostMapping("/updateCarInfo")
    public Result update(CarInfo carInfo) {
        carInfoService.update(carInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam String carInfoId) {
        System.out.println(carInfoId);
        CarInfo carInfo = carInfoService.findBy("carInfoId",carInfoId);
        return ResultGenerator.genSuccessResult(carInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0")Integer page, @RequestParam(defaultValue = "0") Integer size,String accountId,String roleId) {
        PageHelper.startPage(page, size);
        List<CarInfo> list = carInfoService.getUserList(accountId,roleId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
