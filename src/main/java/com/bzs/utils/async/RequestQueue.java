package com.bzs.utils.async;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @program: insurance_bzs
 * @description: 存放所有异步处理接口请求队列的对象, 一个接口对应一个队列
 * @author: dengl
 * @create: 2019-06-27 14:56
 */
@Component
public class RequestQueue {

    /**
     * 处理爬取接口的队列，设置缓冲容量为50
     */

    private BlockingQueue<AsyncVo<String, Object>> crawlingQueue = new LinkedBlockingQueue<>(50);

    public BlockingQueue<AsyncVo<String, Object>> getCrawlingQueue() {
        return crawlingQueue;
    }
}
