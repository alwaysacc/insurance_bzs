package com.bzs.quartz;

import com.bzs.model.CommissionEveryDay;
import com.bzs.model.CommissionPercentage;
import com.bzs.service.CommissionEveryDayService;
import com.bzs.service.CommissionPercentageService;
import com.bzs.utils.jsontobean.C;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Configuration
@EnableScheduling
public class CommissionQuartz {

    @Resource
    private CommissionPercentageService commissionPercentageService;
    @Resource
    private CommissionEveryDayService commissionEveryDayService;

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
}
