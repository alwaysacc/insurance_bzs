package com.bzs.quartz;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.model.CommissionEveryDay;
import com.bzs.model.CommissionPercentage;
import com.bzs.model.OrderInfo;
import com.bzs.service.CommissionEveryDayService;
import com.bzs.service.CommissionPercentageService;
import com.bzs.utils.jsontobean.C;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Configuration
@EnableScheduling
public class CommissionQuartz {

    @Resource
    private CommissionPercentageService commissionPercentageService;
    @Resource
    private CommissionEveryDayService commissionEveryDayService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    //    @Scheduled(cron = "1 * * * * ? ")
    @Scheduled(cron = "59 59 23 * * ? ")
    public void saveCommissionEveryDay() {
        List<CommissionPercentage> comList = commissionPercentageService.findAll();
        CommissionEveryDay c;
        for (int i = 0; i < comList.size(); i++) {
            c = new CommissionEveryDay();
            c.setBizPercentage(comList.get(i).getBizPercentage());
            c.setForcePercentage(comList.get(i).getForcePercentage());
            c.setInsuranceBizPercentage(comList.get(i).getInsuranceBizPercentage());
            c.setInsuranceForcePercentage(comList.get(i).getInsuranceForcePercentage());
            c.setLevelOne(comList.get(i).getLevelOne());
            c.setLevelTwo(comList.get(i).getLevelTwo());
            c.setSource(comList.get(i).getSource());
            c.setSubsidy(comList.get(i).getSubsidy());
            commissionEveryDayService.save(c);
        }
        log.info("每日佣金已保存");
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void dealWithExpiredOrder() {
        List<Map> list = orderInfoMapper.getListToQuartz();
        Date date=new Date();
        for (Map m : list) {
            if (stringToTime((String) m.get("pay_end_date"))<date.getTime()){
                orderInfoMapper.updatePayStatusByOderId((String) m.get("order_id"));
            }
        }
        log.info("过期订单已处理");
    }
    private static Long stringToTime(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
