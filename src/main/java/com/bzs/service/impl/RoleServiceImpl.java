package com.bzs.service.impl;

import com.bzs.dao.RoleMapper;
import com.bzs.model.Role;
import com.bzs.service.RoleService;
import com.bzs.utils.AbstractService;
import org.apache.commons.lang3.StringUtils;
import com.bzs.model.query.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dl on 2019/06/16.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
   private static Logger log=LoggerFactory.getLogger(RoleServiceImpl.class);
    @Resource
    private RoleMapper roleMapper;


    @Override
    public List<Role> findUserRoleByAccountId(String accountId) {
        if(StringUtils.isNotBlank(accountId)){
           return roleMapper.findUserRoleByAccountId(accountId);
        }else{
            log.info("获取角色信息失败,参数为空");
            return new ArrayList<>();
        }
    }
    @Override
    public List<Role> findAllRole(Role role){
        if(null!=role){
            try {
                Example example = new Example(Role.class);
                Example.Criteria criteria = example.createCriteria();
                if(StringUtils.isNotBlank(role.getName()))
                    criteria.andCondition("name=", role.getName());
                if(StringUtils.isNotBlank(role.getCode()))
                    criteria.andCondition("code=", role.getCode());
                if(null!=role.getLevel())
                    criteria.andCondition("level=", role.getLevel());
                if(StringUtils.isNotBlank(role.getDataScope()))
                    criteria.andCondition("data_scope=", role.getDataScope());
               /* if (StringUtils.isNotBlank(role.getCreateTimeFrom()) && StringUtils.isNotBlank(role.getCreateTimeTo())) {
                    criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') >=", role.getCreateTimeFrom());
                    criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') <=", role.getCreateTimeTo());
                }*/
                return  mapper.selectByCondition(example);
            } catch (Exception e) {
                log.error("获取角色信息失败", e);
                return new ArrayList<>();
            }
        }else{
            log.info("获取角色信息失败,参数为空");
            return new ArrayList<>();
        }


    }

}
