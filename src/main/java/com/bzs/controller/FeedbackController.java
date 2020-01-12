package com.bzs.controller;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.bzs.dao.FeedbackMapper;
import com.bzs.utils.QiniuCloudUtil;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Feedback;
import com.bzs.service.FeedbackService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
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
* Created by alwaysacc on 2019/12/15.
*/
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Resource
    private FeedbackService feedbackService;
    @Resource
    private FeedbackMapper feedbackMapper;
    @PostMapping("/uploadImage")
    public Result uploadImage(MultipartFile file, int id) {
        String fileName= IdUtil.fastSimpleUUID();
        String path = QiniuCloudUtil.put64image(file, fileName);
        Feedback feedback=feedbackService.findById(id);
        if (StrUtil.isNotBlank(feedback.getImageUrl())){
            feedback.setImageUrl(feedback.getImageUrl()+path+",");
        }else{
            feedback.setImageUrl(path+",");
        }
        feedbackMapper.updateByPrimaryKeySelective(feedback);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/add")
    public Result add(Feedback feedback) {
        feedbackService.save(feedback);
        return ResultGenerator.genSuccessResult(feedback.getId());
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        feedbackService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Feedback feedback) {
        feedbackMapper.updateByPrimaryKeySelective(feedback);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Feedback feedback = feedbackService.findById(id);
        return ResultGenerator.genSuccessResult(feedback);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,String status) {
        PageHelper.startPage(page, size);
        List list = feedbackMapper.getList(status);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getList")
    public Result getList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                          String userId) {
        PageHelper.startPage(page, size);
        Condition condition=new Condition(Feedback.class);
        condition.createCriteria().andEqualTo("createUser",userId);
        List list = feedbackMapper.selectByCondition(condition);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
