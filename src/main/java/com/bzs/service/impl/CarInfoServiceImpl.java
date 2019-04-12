package com.bzs.service.impl;

import com.bzs.dao.AccountInfoMapper;
import com.bzs.dao.CarInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.model.CarInfo;
import com.bzs.service.CarInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by alwaysacc on 2019/04/10.
 */
@Service
@Transactional
public class CarInfoServiceImpl extends AbstractService<CarInfo> implements CarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;


    @Override
    public List getUserList(String accountId,String roleId,String salesman,String customerStatus) {
        //roleId= accountInfoMapper.getRoleIdByAccountId(accountId);
        if (roleId.equals("管理员")){

        }else if (roleId.equals("管理员分账户")){

        }else if (roleId.equals("代理商")){

        }else{

        }
        return carInfoMapper.getUserList(accountId,roleId,salesman,customerStatus);
    }

    @Override
    public List searchUserList(String accountId, String roleId, String carNumber, String frameNumber, String customerName, String customerTel) {
        return carInfoMapper.searchUserList(
                accountId,roleId,carNumber,frameNumber,customerName,customerTel
        );
    }

    @Override
    public int recoverUser(String[] carInfoId) {
        if (carInfoId!=null){
            return carInfoMapper.recoverUser(carInfoId);
        }
        return 0;
    }
}
