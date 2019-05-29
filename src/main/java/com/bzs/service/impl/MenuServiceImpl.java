package com.bzs.service.impl;

import com.bzs.dao.MenuMapper;
import com.bzs.model.Menu;
import com.bzs.service.MenuService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/05/22.
 */
@Service
@Transactional
public class MenuServiceImpl extends AbstractService<Menu> implements MenuService {
    @Resource
    private MenuMapper menuMapper;

}
