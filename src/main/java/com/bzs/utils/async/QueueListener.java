package com.bzs.utils.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @program: insurance_bzs
 * @description: 队列监听器，初始化启动所有监听任务
 * @author: dengl
 * @create: 2019-06-27 15:02
 */
@Component
public class QueueListener {
    @Autowired
    private CrawlingTask crawlingTask;

    /**
     * 初始化时启动监听请求队列
     */
    @PostConstruct
    public void init() {
        crawlingTask.start();
    }

    /**
     * 销毁容器时停止监听任务
     */
    @PreDestroy
    public void destory() {
        crawlingTask.setRunning(false);
    }
}
