package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CarInfo;
import com.bzs.service.CarInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.subject.Subject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* Created by alwaysacc on 2019/04/10.
*/
@RestController
@RequestMapping("/carinfo")
public class CarInfoController {
    @Resource
    private CarInfoService carInfoService;

    @PostMapping("/add")
     public Result add(CarInfo carInfo) {
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
     * @Description  修改车辆信息
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
    /**
     * @Author 孙鹏程
     * @Description  获取客户列表，salesman是否分配0未分配，customerStatus客户状态，0未回访
     * @Date 2019/4/12/012  11:15
     * @Param [page, size, accountId, roleId, salesman, customerStatus]
     * @return com.bzs.utils.Result
     **/
    @ApiOperation("获取客户列表")
    @PostMapping("/getUserList")
    public Result getUserList(@RequestParam(defaultValue = "0")Integer page, @RequestParam(defaultValue = "0") Integer size,String accountId,
                              String roleId,String salesman,String customerStatus,
                              String plan
                              ) {
        PageHelper.startPage(page, size);
        List<CarInfo> list = carInfoService.getUserList(accountId,roleId,salesman,customerStatus,plan);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    /**
     * @Author 孙鹏程
     * @Description  搜索客户列表，查询什么条件就传什么条件
     * @Date 2019/4/12/012  11:17
     * @Param [accountId, roleId, carNumber, frameNumber, customerName, customerTel]
     * @return com.bzs.utils.Result
     **/
    @ApiOperation("搜索客户列表")
    @PostMapping("/searchUserList")
    public Result searchUserList(String accountId, String roleId, String carNumber, String frameNumber,String customerName, String customerTel,String lincenseOwner) {
        List<CarInfo> list = carInfoService.searchUserList(
                accountId,roleId,carNumber,frameNumber,customerName,customerTel,lincenseOwner);
        return ResultGenerator.genSuccessResult(list);
    }
    /**
     * 通过车牌或者车架号和添加人查询，车牌号和车架号必须有一个不为空
     * @param carNo 车牌
     * @param vinNo 车架
     * @param operatorId 添加者
     * @return
     */
    @PostMapping("/getCarInfoIdInfo")
    public Result getCarInfoIdInfo(String carNo,String vinNo,String operatorId){
      return  carInfoService.getCarInfoIdInfo(carNo,vinNo,operatorId);
    }
    @ApiOperation("回收客户")
    @PostMapping("/recoverUser")
    public Result recoverUser(String[] carInfoIds,int status) {
        //carInfoService.recoverUser(carInfoId);
        //System.out.println(carInfoId);
        //System.out.println(ResultGenerator.genSuccessResult(id));
        return ResultGenerator.genSuccessResult( carInfoService.recoverUser(carInfoIds,status));
    }
    @ApiOperation("获取回收客户")
    @PostMapping("/getRecoverUser")
    public Result getRecoverUser(String accountId, String roleId) {
        return ResultGenerator.genSuccessResult( carInfoService.getRecoverUser(accountId,roleId));
    }

    /**
     * 插入或者更新
     * @param carInfo
     * @return
     */
    @PostMapping("/insertOrUpdate")
    public Result insertOrUpdate(CarInfo carInfo){
       Map<String,Object> result=carInfoService.insertOrUpdate(carInfo);
        return ResultGenerator.genSuccessResult("成功");
    }
    @PostMapping("/getCarInfoIdByCarNoOrVinNo")
    public Result getCarInfoIdByCarNoOrVinNo(String carNo,String  vinNo,String createBy){

        Map<String,Object> result=carInfoService.getCarInfoIdByCarNoOrVinNo(carNo,vinNo,createBy);
        return ResultGenerator.genSuccessResult(result,"成功");
    }
    @ApiOperation("获取车辆信息、续保险种、报价信息，")
    @PostMapping("/getCarInfoAndInsurance")
    public Result getCarInfoAndInsurance(String carInfoId, String createBy,String carNo,String vinNo,String isEnable,String isRenewSuccess){
        Map<String,Object> result=carInfoService.getCarInfoAndInsurance(carInfoId,createBy,carNo,vinNo,isEnable,isRenewSuccess);
        return ResultGenerator.genSuccessResult(result,"成功");
    }
    @ApiOperation("*批量修改 isEnable")
    @PostMapping("/updateBatchIsEnable")
    public Result updateBatchIsEnable(String isEnable){
        List list=new ArrayList();
        list.add("20190513181648237458");
        list.add("20190514092846860906");
        Map<String,Object> result=carInfoService.updateBatchIsEnable(list,isEnable);
        return ResultGenerator.genSuccessResult(result,"成功");
    }
    @PostMapping("/WX_GetNewVehicleInfo")
    public Result WX_GetNewVehicleInfo(@RequestParam String LicenseNo, String EngineNo, String CarVin, int IsNeedCarVin,
                                       @RequestParam String MoldName){
        return carInfoService.WX_GetNewVehicleInfo(LicenseNo,EngineNo,CarVin,IsNeedCarVin,MoldName);
    }


}
