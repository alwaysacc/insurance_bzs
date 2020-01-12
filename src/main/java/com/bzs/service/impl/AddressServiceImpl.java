package com.bzs.service.impl;

import com.bzs.dao.AddressMapper;
import com.bzs.model.Address;
import com.bzs.service.AddressService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/12/15.
 */
@Service
@Transactional
public class AddressServiceImpl extends AbstractService<Address> implements AddressService {
    @Resource
    private AddressMapper addressMapper;

}
