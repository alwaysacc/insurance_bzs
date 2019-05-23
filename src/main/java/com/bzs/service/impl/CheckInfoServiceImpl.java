package com.bzs.service.impl;

import com.bzs.dao.CheckInfoMapper;
import com.bzs.model.CheckInfo;
import com.bzs.service.CheckInfoService;
import com.bzs.utils.AbstractService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/12.
 */
@Service
@Transactional
public class CheckInfoServiceImpl extends AbstractService<CheckInfo> implements CheckInfoService {
    @Resource
    private CheckInfoMapper checkInfoMapper;

    @Override
    public Map updateOrAdd(CheckInfo checkInfo) {
        Map result = new HashedMap();
        int code = checkInfoMapper.updateOrAdd(checkInfo);
        String status = "400";
        String msg = "添加或者修改失败";
        if (code > 0) {
            status = "200";
            result.put("msg", "添加或者修改成功");
        }
        result.put("code", status);
        result.put("msg", msg);
        result.put("data", null);
        return result;
    }
}
