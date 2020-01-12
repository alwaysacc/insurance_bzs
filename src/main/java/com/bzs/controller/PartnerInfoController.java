package com.bzs.controller;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.PartnerInfo;
import com.bzs.service.PartnerInfoService;
import com.bzs.utils.jsontobean.C;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by alwaysacc on 2020/01/04.
*/
@RestController
@RequestMapping("/partner/info")
public class PartnerInfoController {
    @Resource
    private PartnerInfoService partnerInfoService;

    @PostMapping("/add")
    public Result add(PartnerInfo partnerInfo) {
        partnerInfoService.save(partnerInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        partnerInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(PartnerInfo partnerInfo) {
        partnerInfoService.update(partnerInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        PartnerInfo partnerInfo = partnerInfoService.findById(id);
        return ResultGenerator.genSuccessResult(partnerInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,Integer type) {
        PageHelper.startPage(page, size);
        Condition condition=new Condition(PartnerInfo.class);
        condition.createCriteria().andCondition("type="+type);
        List<PartnerInfo> list = partnerInfoService.findByCondition(condition);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getPartnerList")
    public Result getPartnerList() {
        Condition condition=new Condition(PartnerInfo.class);
        condition.createCriteria().andCondition("type="+1);
        Map m=new HashMap<>();
        m.put("list1", partnerInfoService.findByCondition(condition));
        condition=new Condition(PartnerInfo.class);
        condition.createCriteria().andCondition("type="+2);
        m.put("list2", partnerInfoService.findByCondition(condition));
        return ResultGenerator.genSuccessResult(m);
    }
}
