package com.bzs.service.impl;

import com.bzs.dao.MenuInfoMapper;
import com.bzs.model.MenuInfo;
import com.bzs.service.MenuInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by alwaysacc on 2019/04/15.
 */
@Service
@Transactional
public class MenuInfoServiceImpl extends AbstractService<MenuInfo> implements MenuInfoService {
    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Override
    public List<MenuInfo> getUserPermissions(String username) {
        return menuInfoMapper.getUserPermissions(username);
    }
}
