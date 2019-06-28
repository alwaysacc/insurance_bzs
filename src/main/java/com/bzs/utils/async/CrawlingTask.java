package com.bzs.utils.async;

import com.alibaba.fastjson.JSONObject;
import com.bzs.service.CrawlingCarInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: insurance_bzs
 * @description:处理爬取接口的任务，每个任务类处理一种接口
 * @author: dengl
 * @create: 2019-06-27 14:59
 */
@Component
public class CrawlingTask extends  Thread{
    @Autowired
    private RequestQueue queue;
    @Resource
    private CrawlingCarInfoService crawlingCarInfoService;

    private boolean running = true;

    @Override
    public  void run() {
        //super.run();
        while (running) {
            try {
                AsyncVo<String, Object> vo = queue.getCrawlingQueue().take();
                System.out.println("[ CrawlingTask ]开始处理数据爬取");
                String params = vo.getParams();
              //  Thread.sleep(3000);
                Map<String, Object> map = new HashMap<>();
                map.put("params", params);
                map.put("time", System.currentTimeMillis());
                JSONObject j= JSONObject.parseObject(params);
                String seriesNo=j.getString("seriesNo");
                String loginName=j.getString("loginName");
                String logPwd=j.getString("logPwd");
                String flag=j.getString("flag");
                String no=j.getString("no");
                String result=  crawlingCarInfoService.httpCrawling(loginName, logPwd, flag, no);
                vo.getResult().setResult(result);
                System.out.println("[ CrawlingTask ]数据爬取处理完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
                running = false;
            }

        }
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
}
