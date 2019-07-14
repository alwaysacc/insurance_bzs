package com.bzs.service.impl;

import com.bzs.dao.IdCardImgMapper;
import com.bzs.model.IdCardImg;
import com.bzs.service.IdCardImgService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


/**
 * Created by dl on 2019/07/14.
 */
@Service
@Transactional
public class IdCardImgServiceImpl extends AbstractService<IdCardImg> implements IdCardImgService {
    @Resource
    private IdCardImgMapper idCardImgMapper;
    @Override
    public int saveIdCardImg(IdCardImg idCardImg) {
        return idCardImgMapper.saveIdCardImg(idCardImg);
    }
    @Override
    public List getWaitCheckList(int verifiedStat) {
        return idCardImgMapper.getWaitCheckList(verifiedStat);
    }

    @Override
    public HashMap getVerifiedStatById(String accountId) {
        return idCardImgMapper.getVerifiedStatById(accountId);
    }
}
