package com.bzs.service.impl;

import com.bzs.dao.FeedbackMapper;
import com.bzs.model.Feedback;
import com.bzs.service.FeedbackService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/12/15.
 */
@Service
@Transactional
public class FeedbackServiceImpl extends AbstractService<Feedback> implements FeedbackService {
    @Resource
    private FeedbackMapper feedbackMapper;

}
