package com.bzs.controller;
import com.bzs.dao.AddressMapper;
import com.bzs.model.TMenu;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Address;
import com.bzs.service.AddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/12/15.
*/
@RestController
@RequestMapping("/address")
public class AddressController {
    @Resource
    private AddressService addressService;
    @Resource
    private AddressMapper addressMapper;

    @PostMapping("/add")
    public Result add(Address address) {
        int t =addressMapper.insert(address);
        if (t!=0 && address.getIsDefault()==1){
            addressMapper.updateDefault(address.getUserId(),address.getId());
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        addressService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Address address) {
        int t=addressMapper.updateByPrimaryKeySelective(address);
        if (t!=0 && address.getIsDefault()==1){
            addressMapper.updateDefault(address.getUserId(),address.getId());
        }
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/getDefaultByUserId")
    public Result detail(@RequestParam String id) {
        return ResultGenerator.genSuccessResult(addressMapper.getDefaultByUserId(id));
    }
    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Address address = addressService.findById(id);
        return ResultGenerator.genSuccessResult(address);
    }

    @PostMapping("/list")
    public Result list(String userId) {
        Condition example=new Condition(Address.class);
        Example.Criteria  criteria= example.createCriteria();
        criteria.andCondition("userId=", userId);
        return ResultGenerator.genSuccessResult(addressMapper.selectByCondition(example));
    }
}
