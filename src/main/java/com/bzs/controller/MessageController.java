package com.bzs.controller;
import cn.hutool.core.util.IdUtil;
import com.bzs.dao.MessageMapper;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Message;
import com.bzs.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/12/08.
*/
@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private AccountInfoService accountInfoService;

    @PostMapping("/add")
    public Result add(Message message) {
        List<AccountInfo> list=accountInfoService.findAll();
        for (AccountInfo a:list) {
            message.setId(IdUtil.fastSimpleUUID());
            message.setUserId(a.getAccountId());
            messageMapper.insertSelective(message);
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        messageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Message message) {
        messageService.update(message);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/updateStatus")
    public Result updateStatus(String id) {
        Message message=new Message();
        message.setId(id);
        message.setStatus(1);
        messageMapper.updateByPrimaryKeySelective(message);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Message message = messageService.findById(id);
        return ResultGenerator.genSuccessResult(message);
    }
    @PostMapping("/getListByUserId")
    public Result getListByUserId(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,String userId,
                                  Integer status) {
        PageHelper.startPage(page, size);
        List<Message> list = messageMapper.getListByUserId(userId,status);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Message> list = messageMapper.getList();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
