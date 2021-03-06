package com.bzs.service.impl;

import com.bzs.dao.CrawlingExcelInfoMapper;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.service.CrawlingExcelInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.stringUtil.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.util.Date;


/**
 * Created by dl on 2019/06/20.
 */
@Service
@Transactional
public class CrawlingExcelInfoServiceImpl extends AbstractService<CrawlingExcelInfo> implements CrawlingExcelInfoService {
    @Resource
    private CrawlingExcelInfoMapper crawlingExcelInfoMapper;

    @Override
    public int add(CrawlingExcelInfo data) {
        if(null!=data){
            try {
             return   crawlingExcelInfoMapper.add(data);
            }catch (Exception e){
                return 0;
            }
        }
        return 0;
    }

    @Override
    public CrawlingExcelInfo findBy(String fieldName, Object value) throws TooManyResultsException {
        return super.findBy(fieldName, value);
    }

    @Override
    public int updateCrawlingFinish(CrawlingExcelInfo data) {
        return crawlingExcelInfoMapper.updateCrawlingFinish(data);
    }

//    @Transient
    @Override
    public int updateCrawlingFinish(String  seriesNo,Integer lastCrawling, String status, Integer finishTotal, Date finishDate) {
        CrawlingExcelInfo data=new CrawlingExcelInfo();
        if(StringUtils.isNotBlank(seriesNo)){
            data.setSeriesNo(seriesNo);
        }else{
            return 0;
        }
        if(null!=lastCrawling){
            data.setLastCrawling(lastCrawling);
        }
        if(StringUtils.isNotBlank(status)){
            data.setStatus(status);
        }
        if(null!=finishTotal){
            data.setFinishTotal(finishTotal);
        }
        if(null!=finishDate){
            data.setFinishDate(finishDate);
        }
        return updateCrawlingFinish(data);
    }
}
