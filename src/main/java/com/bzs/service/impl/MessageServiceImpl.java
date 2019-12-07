package com.bzs.service.impl;

import com.bzs.dao.MessageMapper;
import com.bzs.model.Message;
import com.bzs.service.MessageService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/12/08.
 */
@Service
@Transactional
public class MessageServiceImpl extends AbstractService<Message> implements MessageService {
    @Resource
    private MessageMapper messageMapper;

}
