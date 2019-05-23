package com.bzs.service.impl;

import com.bzs.dao.CustomerMapper;
import com.bzs.model.CarInfo;
import com.bzs.model.Customer;
import com.bzs.service.CarInfoService;
import com.bzs.service.CustomerService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.UUIDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class CustomerServiceImpl extends AbstractService<Customer> implements CustomerService {
    @Resource
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CarInfoService CarInfoService;

    @Override
    public void updateCustomer(Customer customer, String carInfoId) {
        if (customer.getCustomerId()!=null && !customer.getCustomerId().equals("")){
            customerService.update(customer);
        }else{
            customer.setCustomerId(UUIDS.getUUID());
            customerService.save(customer);
            CarInfo carInfo=new CarInfo();
            carInfo.setCarInfoId(carInfoId);
            carInfo.setCustomerId(customer.getCustomerId());
            CarInfoService.update(carInfo);
        }
    }
}
