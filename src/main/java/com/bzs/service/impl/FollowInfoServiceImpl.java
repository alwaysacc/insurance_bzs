package com.bzs.service.impl;

import cn.hutool.core.date.DateUtil;
import com.bzs.dao.FollowInfoMapper;
import com.bzs.model.FollowInfo;
import com.bzs.model.router.FollowInfoTree;
import com.bzs.service.FollowInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.treeUtil.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * Created by dl on 2019/07/15.
 */
@Slf4j
@Service
@Transactional
public class FollowInfoServiceImpl extends AbstractService<FollowInfo> implements FollowInfoService {
    @Resource
    private FollowInfoMapper followInfoMapper;

    @Override
    public List getFollowInfoByCarInfoId(String carInfoId) {

        List<FollowInfoTree<FollowInfo>> list = new ArrayList<>();
        List<FollowInfoTree<FollowInfo>> followInfoTrees = new ArrayList<>();
        List<FollowInfo> followInfo = followInfoMapper.getFollowInfoByCarInfoId(carInfoId);
        HashSet hashSet = new HashSet();
        // 去重
        followInfo.forEach(f -> {
            String date = DateUtil.formatDate(f.getCreateTime());
            hashSet.add(date);
        });
        hashSet.forEach(h -> {
            FollowInfoTree followInfoTree = new FollowInfoTree();
            followInfoTree.setDate(h.toString());
            followInfoTrees.add(followInfoTree);
        });
        followInfoTrees.forEach(fo -> {
            followInfo.forEach(f -> {
                if (fo.getDate().equals(DateUtil.formatDate(f.getCreateTime()))) {
                    if (fo.getChildren() == null)
                        fo.initChildren();
                    fo.getChildren().add(f);
                }
            });
            list.add(fo);
        });
        return list;
    }
}
