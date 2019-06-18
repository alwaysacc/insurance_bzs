package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bzs.dao.AccountInfoMapper;
import com.bzs.dao.DrawCashMapper;
import com.bzs.model.CommissionPercentage;
import com.bzs.model.DrawCash;
import com.bzs.model.QuoteInfo;
import com.bzs.model.query.SeveralAccount;
import com.bzs.service.CommissionPercentageService;
import com.bzs.service.DrawCashService;
import com.bzs.service.QuoteInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.UUIDS;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/13.
 */
@Service
@Transactional
public class DrawCashServiceImpl extends AbstractService<DrawCash> implements DrawCashService {
   private static Logger logger=LoggerFactory.getLogger(DrawCashServiceImpl.class);
    @Resource
    private DrawCashMapper drawCashMapper;
    @Resource
    private  CommissionPercentageService  commissionPercentageService;
    @Resource
    private QuoteInfoService quoteInfoService;
    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Override
    public Map insertBatch(String list, String createBy) {
        Map map=new HashMap();
        if(StringUtils.isNotBlank(list)&&StringUtils.isNotBlank(createBy)){
           Long commissionPercentageId=commissionPercentageService.getId();
            List<String>  idLists= JSONArray.parseArray(list,String.class);
            String serialNo=UUIDS.getDateUUID();
            List <DrawCash>listDrawCash=new ArrayList<>();
            // 使用 lambda 表达式以及函数操作(functional operation)
            idLists.forEach((id) -> {
                DrawCash drawCash=new DrawCash(id,commissionPercentageId.intValue(),serialNo,createBy);
                drawCash.setType(1);
                listDrawCash.add(drawCash);
            });
            drawCashMapper.insertBatch2(listDrawCash);
            map.put("code","200");
            map.put("msg","添加成功");
            map.put("data",listDrawCash);
        }else{
            map.put("code","400");
            map.put("msg","参数异常");
            map.put("data","");
        }
        return map;
    }

    @Override
    public List getDrawCashList(String incomePerson,int type) {
        return drawCashMapper.getDrawCashList(incomePerson,type);
    }

    @Override
    public Result addDrawCash(String orderId, String quoteId, String createBy) {
        QuoteInfo quoteInfo = quoteInfoService.findBy("quoteId", quoteId);
        BigDecimal biz= quoteInfo.getBizTotal();//商业险
        BigDecimal force=  quoteInfo.getForceTotal();//交强险
        if(null==biz)biz=new BigDecimal(0);
        if(null==force)force=new BigDecimal(0);
        SeveralAccount data = accountInfoMapper.getParentLevel(createBy);//获取父级两层id
        CommissionPercentage percentage = commissionPercentageService.getLastUpdateData();
        BigDecimal bp=new BigDecimal(15);
        BigDecimal fp =  new BigDecimal(4);
        BigDecimal po =new  BigDecimal(1);
        BigDecimal pw =new  BigDecimal(0.5);
        BigDecimal rate=new BigDecimal(1.06);
        Long percentageId=null;
        if(null==percentage){
            bp = new BigDecimal(percentage.getBizPercentage());//商业险百分比
            fp =  new BigDecimal(percentage.getForcePercentage());//交强险百分比
            po =  new BigDecimal(percentage.getLevelOne());//父一级提成
            pw =  new BigDecimal(percentage.getLevelTwo());//父二级提成
        }else{
            percentageId=percentage.getId();
        }

        int level=data.getLevel();
        int level1=data.getLevel1();
        int level2=data.getLevel2();
        if(level==1){//本人做单，只有佣金
            BigDecimal balance=  data.getBalanceTotal();//剩余余额
            BigDecimal commission=   data.getCommissionTotal();//已有佣金
            BigDecimal bizCommission=biz.divide(rate,2,BigDecimal.ROUND_DOWN).multiply(bp).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN);
            BigDecimal forceCommission=force.divide(rate,2,BigDecimal.ROUND_DOWN).multiply(fp).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN);
            balance= balance.add(bizCommission).add(forceCommission);
            commission=commission.add(bizCommission).add(forceCommission);
            String bizCommission2="0";
            if(biz.doubleValue()>0){
                bizCommission2=bizCommission+"";
            }
            String forceCommission2="0";
            if(force.doubleValue()>0){
                forceCommission2=forceCommission+"";
            }
           // DrawCash drawCash=new DrawCash();
            DrawCash  drawCash=new  DrawCash( orderId, percentageId.intValue(),UUIDS.getDateUUID(), createBy,0,  "0",  forceCommission2,  createBy,  bp+"",  fp+"",  "0",   bizCommission2);
            /*drawCash.setBizCash(bizCommission+"");//商业险佣金
            drawCash.setForceCash(forceCommission+"");//交强险佣金
            drawCash.setCash("0");//提成
            drawCash.setIncomePerson(createBy);//收益人
            drawCash.setBizPercentage(bp+"");//商业险比例
            drawCash.setForcePercentage(fp+"");//交强险比例
            //drawCash.setDrawPercentage("100");
            drawCash.setOrderId(orderId);//订单号
            drawCash.setSerialNo(UUIDS.getDateUUID());//流水号
            drawCash.setType(0);
            drawCash.setCreateBy(createBy);*/
            this.save(drawCash);//添加提现信息
            accountInfoMapper.updateMoney(balance,commission,null,createBy);//修改金额

        }
        if(level1==1){//本人做单，父一级只拿提成
            String parentLevelOne = data.getbAccountId();
            BigDecimal balance=    data.getbBalanceTotal();//剩余余额
            BigDecimal drawPer=  data.getbDrawPercentageTotal();//已有提成
            BigDecimal bizDrawPer=biz.divide(rate,2,BigDecimal.ROUND_DOWN).multiply(po).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN);
            balance= balance.add(bizDrawPer);
            drawPer=drawPer.add(bizDrawPer);
            String bizDrawPer2="0";
            if(bizDrawPer.doubleValue()>0){
                bizDrawPer2=bizDrawPer+"";
            }
            DrawCash  drawCash=new  DrawCash( orderId, percentageId.intValue(),UUIDS.getDateUUID(), createBy,1,  bizDrawPer2,  "0",  parentLevelOne,  bp+"",  fp+"",  po+"",   "0");
            this.save(drawCash);//添加提现信息
            accountInfoMapper.updateMoney(balance,null,drawPer,parentLevelOne);//修改金额
        }
        if(level2==2){//本人做单，父二级只拿提成
            String parentLevelTwo = data.getcAccountId();
            BigDecimal balance=   data.getcBalanceTotal();//剩余余额
            BigDecimal drawPer=   data.getcDrawPercentageTotal();//已有提成
            BigDecimal bizDrawPer=biz.divide(rate,2,BigDecimal.ROUND_DOWN).multiply(pw).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN);
            balance= balance.add(bizDrawPer);
            drawPer=drawPer.add(bizDrawPer);
            String bizDrawPer2="0";
            if(bizDrawPer.doubleValue()>0){
                bizDrawPer2=bizDrawPer+"";
            }
            DrawCash  drawCash=new  DrawCash( orderId, percentageId.intValue(),UUIDS.getDateUUID(), createBy,1,  bizDrawPer2,  "0",  parentLevelTwo,  bp+"",  fp+"",  pw+"",   "0");
            this.save(drawCash);//添加提现信息
            accountInfoMapper.updateMoney(balance,null,drawPer,parentLevelTwo);//修改金额
        }
        return ResultGenerator.genSuccessResult();
    }
}
