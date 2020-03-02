package com.bzs.controller;
import com.bzs.dao.HeadlinesMapper;
import com.bzs.model.Address;
import com.bzs.utils.QiniuCloudUtil;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Headlines;
import com.bzs.service.HeadlinesService;
import com.bzs.utils.UUIDS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2020/01/31.
*/
@RestController
@RequestMapping("/headlines")
public class HeadlinesController {
    @Resource
    private HeadlinesService headlinesService;
    @Resource
    private HeadlinesMapper headlinesMapper;
    @PostMapping("/uploadImage")
    public Result uploadImage(MultipartFile file) {
        String path = QiniuCloudUtil.put64image(file, UUIDS.getUUID());
        return ResultGenerator.genSuccessResult(path);
    }
    @PostMapping("/add")
    public Result add(Headlines headlines) {
        headlinesService.save(headlines);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        headlinesService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Headlines headlines) {
        headlinesService.update(headlines);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Headlines headlines = headlinesService.findById(id);
        return ResultGenerator.genSuccessResult(headlines);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Headlines> list = headlinesService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getList")
    public Result list() {
        Condition example=new Condition(Headlines.class);
        example.orderBy("createTime").desc();
        Example.Criteria  criteria= example.createCriteria();
        criteria.andCondition("is_show=", 1);
        PageHelper.startPage(0, 10);
        List list=headlinesMapper.selectByCondition(example);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo.getList());
    }
}
