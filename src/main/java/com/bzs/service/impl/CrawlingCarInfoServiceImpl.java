package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.CrawlingCarInfoMapper;
import com.bzs.dao.CrawlingExcelInfoMapper;
import com.bzs.model.CommissionPercentage;
import com.bzs.model.CrawlingCarInfo;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.model.query.CrawlingQuery;
import com.bzs.model.query.ThridAccountAndAdminDomain;
import com.bzs.redis.RedisUtil;
import com.bzs.service.CrawlingCarInfoService;
import com.bzs.service.CrawlingExcelInfoService;
import com.bzs.service.ThirdInsuranceAccountInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.UUIDS;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.excelUtil.ExcelExportUtil;
import com.bzs.utils.excelUtil.WriteExcelDataDelegated;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.PCICResponseBean;
import com.bzs.utils.jsontobean.crawlingcar.CrawlingCarData;
import com.bzs.utils.jsontobean.crawlingcar.CrawlingCarRootBean;
import com.bzs.utils.redisConstant.RedisConstant;
import com.wuwenze.poi.util.POIUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Created by dl on 2019/06/19.
 */
@Slf4j
@Service
@Transactional
public class CrawlingCarInfoServiceImpl extends AbstractService<CrawlingCarInfo> implements CrawlingCarInfoService {
    @Resource
    private CrawlingCarInfoMapper crawlingCarInfoMapper;
    @Resource
    private CrawlingExcelInfoService crawlingExcelInfoService;
    @Resource
    private CrawlingExcelInfoMapper crawlingExcelInfoMapper;
    @Resource
    private ThirdInsuranceAccountInfoService thirdInsuranceAccountInfoService;
    @Autowired
    private RedisUtil redisUtil;
    private static  final String PROGRESS="progress.";

    @Override
    public int batchInsertImport(List<CrawlingCarInfo> list) {
        return crawlingCarInfoMapper.batchInsertImport(list);
    }

    @Override
    public String httpCrawling(String username, String password, String flag, String no) {
        String url = "http://123.207.50.164:5000/query";
        String params = "username=" + username + "&password=" + password +
                "&flag=" + flag + "&no=" + no;
        String body = null;
        HttpResult httpResult = HttpClientUtil.doPostHttpURL(url, params);
        int code = httpResult.getCode();
        String msg = httpResult.getMessage();
        Map<String, Object> map = new HashMap<>();
        if (code == 200) {
            body = httpResult.getBody();
        } else {
            body = "{\"code\":" + code + ",\"data\":{},\"msg\":\"" + msg + "\"}";
        }
        log.info("爬取返回最终结果body=" + body);
        return body;


    }

    @Override
    public List crawlingDataList(String seriesNo, Integer startRow, Integer pageSize) {
        return crawlingCarInfoMapper.crawlingDataList(seriesNo, startRow, pageSize);
    }

    @Override
    public List<CrawlingCarInfo> exportDataListBySeriesNo(String seriesNo) {
        /*Condition condition =new Condition(CrawlingCarInfo.class);
        Example.Criteria  criteria= condition.createCriteria();
        criteria.andCondition("series_no=", seriesNo);
        return  this.findByCondition(condition);*/
        return crawlingCarInfoMapper.exportDataListBySeriesNo(seriesNo, 0, 10);
    }

    @Override
    public int exportDataCountBySeriesNo(String seriesNo) {
        Condition condition = new Condition(CrawlingCarInfo.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andCondition("series_no=", seriesNo);
        return mapper.selectCountByCondition(condition);
    }

    @Override
    public int crawlingDataCount(String seriesNo) {
        return crawlingCarInfoMapper.crawlingDataCount(seriesNo);
    }

    @Async
    @Override
    public Result startCrawling(String seriesNo) {
        if (StringUtils.isBlank(seriesNo)) {
            return ResultGenerator.genFailResult("请选择需要执行的文件");
        }
        CrawlingExcelInfo excelInfo = crawlingExcelInfoService.findBy("seriesNo", seriesNo);
        if (null == excelInfo) {
            return ResultGenerator.genFailResult("请选择需要执行的文件");
        } else {
            String status = excelInfo.getStatus();
            String type = excelInfo.getType();
            int count = excelInfo.getTotal();
            String createBy = excelInfo.getCreateBy();
            if ("1".equals(status)) {//1完成，修改状态信息为完成
                crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null, status, null, null);
                return ResultGenerator.genFailResult("文件已经爬取完毕");
            } else {//状态0未执行 2暂停 3执行中
                //获取爬取账号开始
                String username = "";
                String passWord = "";
                List<ThridAccountAndAdminDomain> lists = thirdInsuranceAccountInfoService.getCrawlingAndAdminList(createBy);
                if (CollectionUtils.isNotEmpty(lists)) {
                    int size = lists.size();
                    int random = new Random().nextInt(size);//随机一个账号
                    ThridAccountAndAdminDomain domain = lists.get(random);//获取随机账号信息
                    username = domain.getAccountName();//账号
                    passWord = domain.getAccountPwd();//密码
                    //账号不能为空
                    if (StringUtils.isBlank(username) || StringUtils.isBlank(passWord)) {
                        crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null, "-1", null, null);
                        return ResultGenerator.genFailResult("爬取的账号或者密码为空");
                    }

                } else {//未获取到账号信息,修改回原来的状态
                    crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null, "-1", null, null);
                    return ResultGenerator.genFailResult("请添加爬取的账号");
                }
                //获取爬取账号结束
                //查询表中所有数据数量，根据 seriesNo
                int total = crawlingCarInfoMapper.crawlingDataCount(seriesNo);
                if (total > 0) {
                    Integer pageSize = 10;
                    Integer page = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
                    //Integer page = 1;
                    for (int i = 0; i < page; page--) {
                        Integer startRow = (page - 1) * pageSize;
                        //分页获取数据库基础数据，然后将车牌或者车架组装，供爬取接口调用
                        List<CrawlingCarInfo> list = crawlingCarInfoMapper.crawlingDataList(seriesNo, startRow, pageSize);
                        //新建一个装爬取结果的集合
                        List<CrawlingCarInfo> resltDataList = new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(list)) {
                            String no = "";
                            for (CrawlingCarInfo crawlingCarInfo : list) {//遍历数据库基础数据
                                Long id = crawlingCarInfo.getId();
                                int indexNo = crawlingCarInfo.getIndexNo();
                                if ((indexNo + "").equals(count + "")) {//查询到本次上传的excel中最大的序列号，等于总数,则修改数据更新完毕
                                    crawlingExcelInfoService.updateCrawlingFinish(seriesNo, id.intValue(), "1", total, new Date());
                                }
                                CrawlingCarInfo crawling = new CrawlingCarInfo();//单个装爬取的数据对象
                                String carNo = crawlingCarInfo.getCarNo();
                                crawling.setCarNo(carNo);//原车牌
                                String vinNo = crawlingCarInfo.getVinNo();
                                crawling.setVinNo(vinNo);//原车架
                                String carOwner=crawlingCarInfo.getCarOwner();
                                crawling.setCarOwner(carOwner);//元车主
                                crawling.setId(id);
                                String isCrawling = "2";//默认根据车架爬取
                                if (!"1".equals(type)) {
                                    if (StringUtils.isNotBlank(no)) {
                                        no += "," + vinNo;
                                    } else {
                                        no = vinNo;
                                    }
                                } else {//等于1车牌  车牌爬取
                                    if (StringUtils.isNotBlank(no)) {
                                        no += "," + carNo;
                                    } else {
                                        no = carNo;
                                    }
                                    isCrawling = "1";
                                }
                                crawling.setIsDrawling(isCrawling);
                                resltDataList.add(crawling);//添加到爬取结果的结合
                            }
                            try {
                                //调用爬取接口 ,只有验证码验证错误,才会连续调用，最多执行5-count次，刚开始count=0；其他成功或者异常不会多次调用直接返回，
                                String resultMap = nodeCrawling(no, type, username, passWord, 0);
                                //将结果变成java对象
                                CrawlingCarRootBean bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                                int code = bean.getCode();
                                if (-2 == code) {//账号密码错误，爬去接口返回
                                    return ResultGenerator.genFailResult("表示用户名密码错误（可以换用户名密码之后再次请求）");
                                } else if (-1 == code) {//验证码错误，已验证5次
                                    log.info("验证码出险5次验证错误", "车牌号为" + no);
                                    for (CrawlingCarInfo resData : resltDataList) {
                                        resData.setStatus("2");
                                        resData.setResultMessage("验证码验证失败");
                                    }
                                    //return ResultGenerator.genFailResult("表示验证码识别出错,已识别5次");
                                } else if(0 == code){//成功
                                    List<CrawlingCarData> resDatas = bean.getData();//爬取返回的结果集合
                                    if (CollectionUtils.isNotEmpty(resDatas)) {
                                        for (int j=0;j<resDatas.size();j++) {//遍历爬取的返回结果
                                            CrawlingCarData resData=resDatas.get(j);//获取的数据
                                            CrawlingCarInfo  data=resltDataList.get(j);//数据源数据，【ye可改为list,即分页数据进行遍历】
                                            String  carNo=data.getCarNo();
                                            String vinNo=data.getVinNo();
                                            String carOwner=data.getCarOwner();
                                            data.setStatus("1");
                                            data.setBizCompany(resData.getShangye_comp());//商业险公司
                                            data.setBizEndDate(resData.getShangye_ed());//商业险到期
                                            data.setBizStartDate(resData.getShangye_st());//商业险起期
                                            data.setForceCompany(resData.getJiaoqiang_comp());//交强险公司
                                            data.setForceEndDate(resData.getJiaoqiang_et());//交强险到期
                                            data.setForceStartDate(resData.getJiaoqiang_st());//交强险起期
                                            data.setBrand(resData.getCn_type());//品牌
                                            data.setEngineNo(resData.getFadongji_no());//发送机号
                                            data.setIsDrawling(type);//2车架爬取1车牌爬取
                                            data.setModel(resData.getCar_type());//车辆型号
                                            log.info("车辆和型号：" + resData.getCar_type() + ",品牌：" + resData.getCn_type());
                                            data.setRegisterDate(resData.getStart_time());//初登日期
                                            String newCarNo = resData.getChepai_no();
                                            data.setNewCarNo(newCarNo);//车牌
                                            if (StringUtils.isNotBlank(carNo)) {
                                                if (!carNo.equals(newCarNo)) {
                                                    data.setIsNewCarNo("1");//新车牌
                                                }
                                            }
                                            String newCarOwner = resData.getName();
                                            data.setNewCarOwner(newCarOwner);//车主
                                            if (StringUtils.isNotBlank(carOwner)) {
                                                if (!carOwner.equals(newCarOwner)) {
                                                    data.setIsNewCarOwner("1");//新车主
                                                }
                                            }

                                            String newVinNo = resData.getChejia_no();
                                            data.setNewVinNo(newVinNo);//车架
                                            if (StringUtils.isNotBlank(vinNo)) {
                                                if (!vinNo.equals(newVinNo)) {
                                                    data.setIsNewVinNo("1");//新车架
                                                }
                                            }
                                            data.setTransferDate(resData.getTrans_time());//转户日期
                                        }

                                        }
                                    }else{
                                        log.error("爬取出现异常，爬取接口异常", "车牌号为" + no);
                                        for (CrawlingCarInfo resData : resltDataList) {
                                            resData.setStatus("2");
                                            resData.setResultMessage("爬取失败，状态码"+code);
                                        }
                                    }
                                }catch (Exception e) {
                                log.error("爬取出险异常,本地接口异常",e);
                            }
                            //遍历爬取结果并修改
                            if(CollectionUtils.isNotEmpty(resltDataList)){
                                for (CrawlingCarInfo result:resltDataList){
                                    crawlingCarInfoMapper.crawlingUpdate(result);//修改爬取的车辆信息
                                }
                            }

                        } else {
                            continue;
                        }
                        // return ResultGenerator.genSuccessResult(list, "文件已经爬取完毕");
                    }
                    crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo, "1");
                    return ResultGenerator.genSuccessResult("文件已经爬取完毕");

                } else {
                    crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo, status);
                    return ResultGenerator.genFailResult("文件已经爬取完毕");
                }
            }
        }
    }

    /**
     * 利用递归验证码错误次数，count=0初始值，，在校验5次
     * @param no
     * @param type
     * @param username
     * @param passWord
     * @param count 校验次数 起始为0则校验5次
     * @return
     */
    public  String nodeCrawling(String no, String type, String username, String passWord, int count) {
//        synchronized (this){

            //调用爬取接口
            String resultMap = httpCrawling(username, passWord, type, no);
            CrawlingCarRootBean bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
            int code = bean.getCode();
            log.info("第"+(count+1)+"次验证");
            log.info("爬取的返回值code="+code);
            if (-1 == code && count <5) {
                count++;
                resultMap= nodeCrawling(no, type, username, passWord, count);
            }
            return resultMap;
//        }
    }

//原单条爬取--已废弃
    public Result paqu(List<CrawlingCarInfo> list, int count, String seriesNo, int total, String type, String username, String passWord) {
        for (CrawlingCarInfo data : list) {
            String carNo = data.getCarNo();
            String vinNo = data.getVinNo();
            String carOwner = data.getCarOwner();
            String no = "";
            Long id = data.getId();
            int indexNo = data.getIndexNo();
            if ((indexNo + "").equals(count + "")) {
                crawlingExcelInfoService.updateCrawlingFinish(seriesNo, id.intValue(), "1", total, new Date());
            }
            if (!"1".equals(type)) {
                no = vinNo;
            } else {//等于1车牌
                no = carNo;
            }
            String resultMap = httpCrawling(username, passWord, type, no);
            try {
                CrawlingCarRootBean bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                int code = bean.getCode();
                if (-1 == code) {
                    resultMap = httpCrawling(username, passWord, type, no);
                    bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                    code = bean.getCode();
                    if (-1 == code) {
                        resultMap = httpCrawling(username, passWord, type, no);
                        bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                        code = bean.getCode();
                        if (-1 == code) {
                            resultMap = httpCrawling(username, passWord, type, no);
                            bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                            code = bean.getCode();
                            if (-1 == code) {
                                resultMap = httpCrawling(username, passWord, type, no);
                                bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                                code = bean.getCode();
                                if (-1 == code) {
                                    resultMap = httpCrawling(username, passWord, type, no);
                                    bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                                    code = bean.getCode();
                                }
                            }
                        }
                    }

                }
                if (-2 == code) {
                    return ResultGenerator.genFailResult("表示用户名密码错误（可以换用户名密码之后再次请求）");
                } else if (-1 == code) {
                    return ResultGenerator.genFailResult("表示验证码识别出错,已识别5次");
                }

//                                    CrawlingCarData resData = bean.getData();
                CrawlingCarData resData = null;
                if (null != resData) {
                    data.setStatus("1");
                    data.setBizCompany(resData.getShangye_comp());//商业险公司
                    data.setBizEndDate(resData.getShangye_ed());//商业险到期
                    data.setBizStartDate(resData.getShangye_st());//商业险起期
                    data.setForceCompany(resData.getJiaoqiang_comp());//交强险公司
                    data.setForceEndDate(resData.getJiaoqiang_et());//交强险到期
                    data.setForceStartDate(resData.getJiaoqiang_st());//交强险起期
                    data.setBrand(resData.getCn_type());//品牌
                    data.setEngineNo(resData.getFadongji_no());//发送机号
                    data.setIsDrawling(type);//2车架爬取1车牌爬取
                    data.setModel(resData.getCar_type());//车辆型号
                    log.info("车辆和型号：" + resData.getCar_type() + ",品牌：" + resData.getCn_type());
                    data.setRegisterDate(resData.getStart_time());//初登日期
                    String newCarNo = resData.getChepai_no();
                    data.setNewCarNo(newCarNo);//车牌
                    if (StringUtils.isNotBlank(carNo)) {
                        if (!carNo.equals(newCarNo)) {
                            data.setIsNewCarNo("1");//新车牌
                        }
                    }
                    String newCarOwner = resData.getName();
                    data.setNewCarOwner(newCarOwner);//车主
                    if (StringUtils.isNotBlank(carOwner)) {
                        if (!carOwner.equals(newCarOwner)) {
                            data.setIsNewCarOwner("1");//新车主
                        }
                    }

                    String newVinNo = resData.getChejia_no();
                    data.setNewVinNo(newVinNo);//车架
                    if (StringUtils.isNotBlank(vinNo)) {
                        if (!vinNo.equals(newVinNo)) {
                            data.setIsNewVinNo("1");//新车架
                        }
                    }
                    data.setTransferDate(resData.getTrans_time());//转户日期
                } else {
                    data.setStatus("2");//失败
                    data.setResultMessage("失败");
                }
            } catch (Exception e) {
                log.error("报错", e);
                data.setStatus("2");//失败
                if (resultMap.length() > 255) {
                    resultMap = resultMap.substring(0, 255);
                }
                data.setResultMessage(resultMap);
            }
            crawlingCarInfoMapper.crawlingUpdate(data);//修改爬取的车辆信息
        }
        return null;
    }

    @Async
    @Override
    public void startCrawling1() {
        boolean stat=true;
        List redisList= (List) redisUtil.get(RedisConstant.CRAWLING_LIST);
        while (stat){
            log.info("oldList:"+redisList.toString());
            String seriesNo="";
            Iterator it = redisList.iterator();
            while(it.hasNext()) {
                seriesNo= (String) it.next();
                CrawlingExcelInfo excelInfo = crawlingExcelInfoService.findBy("seriesNo", seriesNo);
                if (null == excelInfo) {
//                    return ResultGenerator.genFailResult("请选择需要执行的文件");
                } else {
                    String status = excelInfo.getStatus();
                    String type = excelInfo.getType();
                    int count = excelInfo.getTotal();
                    String createBy = excelInfo.getCreateBy();
                    if ("1".equals(status)) {//1完成，修改状态信息为完成
                        crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null, status, null, null);
//                        return ResultGenerator.genFailResult("文件已经爬取完毕");
                    } else {//状态0未执行 2暂停 3执行中
                        //获取爬取账号开始
                        String username = "";
                        String passWord = "";
                        List<ThridAccountAndAdminDomain> lists = thirdInsuranceAccountInfoService.getCrawlingAndAdminList(createBy);
                        if (CollectionUtils.isNotEmpty(lists)) {
                            int size = lists.size();
                            int random = new Random().nextInt(size);//随机一个账号
                            ThridAccountAndAdminDomain domain = lists.get(random);//获取随机账号信息
                            username = domain.getAccountName();//账号
                            passWord = domain.getAccountPwd();//密码
                            //账号不能为空
                            if (StringUtils.isBlank(username) || StringUtils.isBlank(passWord)) {
                                crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null, status, null, null);
//                                return ResultGenerator.genFailResult("爬取的账号或者密码为空");
                            }

                        } else {//未获取到账号信息,修改回原来的状态
                            crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null, status, null, null);
//                            return ResultGenerator.genFailResult("请添加爬取的账号");
                        }
                        //获取爬取账号结束
                        //查询表中所有数据数量，根据 seriesNo
                        int total = crawlingCarInfoMapper.crawlingDataCount(seriesNo);
                        if (total > 0) {
                            Integer pageSize = 10;
                            Integer page = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
                            //Integer page = 1;
                            for (int i = 0; i < page; i++) {
                                Integer startRow = i * pageSize;
                                //分页获取数据库基础数据，然后将车牌或者车架组装，供爬取接口调用
                                List<CrawlingCarInfo> list = crawlingCarInfoMapper.crawlingDataList(seriesNo, startRow, pageSize);
                                //新建一个装爬取结果的集合
                                List<CrawlingCarInfo> resltDataList = new ArrayList<>();
                                if (CollectionUtils.isNotEmpty(list)) {
                                    String no = "";
                                    for (CrawlingCarInfo crawlingCarInfo : list) {//遍历数据库基础数据
                                        Long id = crawlingCarInfo.getId();
                                        int indexNo = crawlingCarInfo.getIndexNo();
                                        if ((indexNo + "").equals(count + "")) {//查询到本次上传的excel中最大的序列号，等于总数,则修改数据更新完毕
                                            crawlingExcelInfoService.updateCrawlingFinish(seriesNo, id.intValue(), "1", total, new Date());
                                        }
                                        CrawlingCarInfo crawling = new CrawlingCarInfo();//单个装爬取的数据对象
                                        String carNo = crawlingCarInfo.getCarNo();
                                        crawling.setCarNo(carNo);//原车牌
                                        String vinNo = crawlingCarInfo.getVinNo();
                                        crawling.setVinNo(vinNo);//原车架
                                        String carOwner=crawlingCarInfo.getCarOwner();
                                        crawling.setCarOwner(carOwner);//元车主
                                        crawling.setId(id);
                                        String isCrawling = "2";//默认根据车架爬取
                                        if (!"1".equals(type)) {
                                            if (StringUtils.isNotBlank(no)) {
                                                no += "," + vinNo;
                                            } else {
                                                no = vinNo;
                                            }
                                        } else {//等于1车牌  车牌爬取
                                            if (StringUtils.isNotBlank(no)) {
                                                no += "," + carNo;
                                            } else {
                                                no = carNo;
                                            }
                                            isCrawling = "1";
                                        }
                                        crawling.setIsDrawling(isCrawling);
                                        resltDataList.add(crawling);//添加到爬取结果的结合
                                    }
                                    try {
                                        //调用爬取接口 ,只有验证码验证错误,才会连续调用，最多执行5-count次，刚开始count=0；其他成功或者异常不会多次调用直接返回，
                                        String resultMap = nodeCrawling(no, type, username, passWord, 0);
                                        //将结果变成java对象
                                        CrawlingCarRootBean bean = JSONObject.parseObject(resultMap, CrawlingCarRootBean.class);
                                        int code = bean.getCode();
                                        if (-2 == code) {//账号密码错误，爬去接口返回
//                                            return ResultGenerator.genFailResult("表示用户名密码错误（可以换用户名密码之后再次请求）");
                                        } else if (-1 == code) {//验证码错误，已验证5次
                                            log.info("验证码出险5次验证错误", "车牌号为" + no);
                                            for (CrawlingCarInfo resData : resltDataList) {
                                                resData.setStatus("2");
                                                resData.setResultMessage("验证码验证失败");
                                            }
                                            //return ResultGenerator.genFailResult("表示验证码识别出错,已识别5次");
                                        } else if(0 == code){//成功
                                            List<CrawlingCarData> resDatas = bean.getData();//爬取返回的结果集合
                                            if (CollectionUtils.isNotEmpty(resDatas)) {
                                                for (int j=0;j<resDatas.size();j++) {//遍历爬取的返回结果
                                                    CrawlingCarData resData=resDatas.get(j);//获取的数据
                                                    CrawlingCarInfo  data=resltDataList.get(j);//数据源数据，【ye可改为list,即分页数据进行遍历】
                                                    String  carNo=data.getCarNo();
                                                    String vinNo=data.getVinNo();
                                                    String carOwner=data.getCarOwner();
                                                    data.setStatus("1");
                                                    data.setBizCompany(resData.getShangye_comp());//商业险公司
                                                    data.setBizEndDate(resData.getShangye_ed());//商业险到期
                                                    data.setBizStartDate(resData.getShangye_st());//商业险起期
                                                    data.setForceCompany(resData.getJiaoqiang_comp());//交强险公司
                                                    data.setForceEndDate(resData.getJiaoqiang_et());//交强险到期
                                                    data.setForceStartDate(resData.getJiaoqiang_st());//交强险起期
                                                    data.setBrand(resData.getCn_type());//品牌
                                                    data.setEngineNo(resData.getFadongji_no());//发送机号
                                                    data.setIsDrawling(type);//2车架爬取1车牌爬取
                                                    data.setModel(resData.getCar_type());//车辆型号
                                                    log.info("车辆和型号：" + resData.getCar_type() + ",品牌：" + resData.getCn_type());
                                                    data.setRegisterDate(resData.getStart_time());//初登日期
                                                    String newCarNo = resData.getChepai_no();
                                                    data.setNewCarNo(newCarNo);//车牌
                                                    if("2".equals(type)){//2车架爬取

                                                    }
                                                    if (StringUtils.isNotBlank(carNo)) {//原车牌号不为空
                                                        if (!carNo.equals(newCarNo)) {
                                                            data.setIsNewCarNo("1");//新车牌
                                                        }
                                                    }else{
                                                        if(StringUtils.isNotBlank(newCarNo)){
                                                            data.setCarNo(newCarNo);
                                                        }
                                                    }
                                                    String newCarOwner = resData.getName();
                                                    data.setNewCarOwner(newCarOwner);//车主
                                                    if (StringUtils.isNotBlank(carOwner)) {
                                                        if (!carOwner.equals(newCarOwner)) {
                                                            data.setIsNewCarOwner("1");//新车主
                                                        }
                                                    }else{
                                                        if(StringUtils.isNotBlank(newCarOwner)){
                                                            data.setCarOwner(newCarOwner);
                                                        }

                                                    }
                                                    String newVinNo = resData.getChejia_no();
                                                    data.setNewVinNo(newVinNo);//车架
                                                    if (StringUtils.isNotBlank(vinNo)) {
                                                        if (!vinNo.equals(newVinNo)) {
                                                            data.setIsNewVinNo("1");//新车架
                                                        }
                                                    }
                                                    data.setTransferDate(resData.getTrans_time());//转户日期
                                                    log.info(String.valueOf(resltDataList.size()));
                                                }
                                            }
                                        }else{
                                            log.error("爬取出现异常，爬取接口异常", "车牌号为" + no);
                                            for (CrawlingCarInfo resData : resltDataList) {
                                                resData.setStatus("2");
                                                resData.setResultMessage("爬取失败，状态码"+code);
                                            }
                                        }
                                        //进度条 存储已爬取的数量到redis
                                        if (redisUtil.hasKey(PROGRESS+seriesNo)){
                                            redisUtil.set(PROGRESS+seriesNo,
                                                    resltDataList.size()+(Integer) redisUtil.get(PROGRESS+seriesNo),
                                                    1800);
                                        }else{
                                            redisUtil.set(PROGRESS+seriesNo, resltDataList.size(), 1800);
                                        }
                                    }catch (Exception e) {
                                        log.error("爬取出险异常,本地接口异常",e);
                                    }
                                    //遍历爬取结果并修改
                                    if(CollectionUtils.isNotEmpty(resltDataList)){
                                        System.out.println("执行修改");
                                        for (CrawlingCarInfo result:resltDataList){
                                            crawlingCarInfoMapper.crawlingUpdate(result);//修改爬取的车辆信息
                                        }
                                    }

                                } else {
                                    continue;
                                }
                                // return ResultGenerator.genSuccessResult(list, "文件已经爬取完毕");
                            }
                            crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo, "1");
//                            return ResultGenerator.genSuccessResult("文件已经爬取完毕");
                        } else {
                            crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo, status);
//                            return ResultGenerator.genFailResult("文件已经爬取完毕");
                        }
                    }
                }
            }
            List newList=new ArrayList();
            newList= (List) redisUtil.get(RedisConstant.CRAWLING_LIST);
            newList.removeAll(redisList);
            if (newList==null || newList.size()==0){
                log.info(newList.toString());
                stat=false;
                redisUtil.del(RedisConstant.CRAWLING_LIST);
            }else{
                 redisList=newList;
                log.info("redisList:"+redisList.toString());
                redisUtil.set(RedisConstant.CRAWLING_LIST,redisList);
            }
            log.info("newList:"+newList.toString());
        }
    }
    @Override
    public String exportCrawlingDataList(HttpServletResponse response, HttpServletRequest request, String seriesNo) {
        int totalRowCount = exportDataCountBySeriesNo(seriesNo);
        // 导出EXCEL文件名称
        String path = request.getSession().getServletContext().getRealPath("");
        //  path=request.getSession().getServletContext().getRealPath(request.getRequestURI());
        String filaName = UUIDS.getDateUUID();
        path += filaName + ".xlsx";
        // 标题
        String[] titles = {"车辆牌照", "新车牌", "车主", "新车主", "车架号",
                "品牌", "车辆型号", "发动机号", "登记日期", "过户日期(没有过户没有)",
                "交强险承保公司", "交强险到期日期", "商业险承保公司", "商业险到期日期", "出险次数",
                "违章次数", "身份证号码", "联系电话", "序号","车牌是否一致","车主是否一致","爬取结果"};
        // 开始导入
        try {
//         ExcelExportUtil.exportExcelToLocalPath(totalRowCount,  titles,path, new WriteExcelDataDelegated() {
            ExcelExportUtil.exportExcelToWebsite(response, totalRowCount, filaName, titles, new WriteExcelDataDelegated() {
                @Override
                public void writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception {
                    int startRow = (currentPage - 1) * pageSize;
                    List<CrawlingCarInfo> userVOList = crawlingCarInfoMapper.exportDataListBySeriesNo(seriesNo, startRow, pageSize);
                    if (!CollectionUtils.isEmpty(userVOList)) {
                        if (userVOList.size() < pageSize) {
                            endRowCount = pageSize * currentPage + userVOList.size();
                        }
                        for (int i = startRowCount; i <= endRowCount; i++) {
                            SXSSFRow eachDataRow = eachSheet.createRow(i);
                            if ((i - startRowCount) < userVOList.size()) {
                                CrawlingCarInfo eachUserVO = (CrawlingCarInfo) userVOList.get(i - startRowCount);
                                String carNo = eachUserVO.getCarNo();//原车牌
                                String newCarNO=eachUserVO.getNewCarNo();//爬取的车牌
                                if (StringUtils.isBlank(carNo)) {//防止本身没有车架号，导出时车牌为空
                                    carNo = eachUserVO.getNewCarNo();
                                }
                                eachDataRow.createCell(0).setCellValue(
                                        carNo == null ? "" : carNo);// 车辆牌照
                                eachDataRow.createCell(1).setCellValue(
                                        eachUserVO.getNewCarNo() == null ? "" : newCarNO);// 新车牌

                                eachDataRow.createCell(2).setCellValue(
                                        eachUserVO.getCarOwner() == null ? "" : eachUserVO.getCarOwner());// 车主
                                eachDataRow.createCell(3).setCellValue(
                                        eachUserVO.getNewCarOwner() == null ? "" : eachUserVO.getNewCarOwner());// 新车主
                                String vinNo = eachUserVO.getVinNo();
                                if (StringUtils.isBlank(vinNo)) {//防止本身没有车架号，导出时车架为空
                                    vinNo = eachUserVO.getNewVinNo();
                                }
                                eachDataRow.createCell(4).setCellValue(
                                        vinNo == null ? "" : vinNo);// 车架号
                                eachDataRow.createCell(5).setCellValue(
                                        eachUserVO.getBrand() == null ? "" : eachUserVO.getBrand());// 品牌
                                eachDataRow.createCell(6).setCellValue(
                                        eachUserVO.getModel() == null ? "" : eachUserVO.getModel());// 型号
                                eachDataRow.createCell(7).setCellValue(
                                        eachUserVO.getEngineNo() == null ? "" : eachUserVO.getEngineNo());// 发动机号

                                eachDataRow.createCell(8).setCellValue(
                                        eachUserVO.getRegisterDate() == null ? "" : eachUserVO.getRegisterDate());// 登记日期
                                eachDataRow.createCell(9).setCellValue(
                                        eachUserVO.getTransferDate() == null ? "" : eachUserVO.getTransferDate());// 过户日期(没有过户没有)

                                eachDataRow.createCell(10).setCellValue(
                                        eachUserVO.getForceCompany() == null ? "" : eachUserVO.getForceCompany());// 交强险承保公司

                                eachDataRow.createCell(11).setCellValue(
                                        eachUserVO.getForceEndDate() == null ? "" : eachUserVO.getForceEndDate());// 交强险到期日期
                                eachDataRow.createCell(12).setCellValue(
                                        eachUserVO.getBizCompany() == null ? "" : eachUserVO.getBizCompany());// 商业险承保公司
                                eachDataRow.createCell(13).setCellValue(
                                        eachUserVO.getBizEndDate() == null ? "" : eachUserVO.getBizEndDate());// 商业险到期日期

                                eachDataRow.createCell(14).setCellValue(
                                        eachUserVO.getOutDangerCount() == null ? "" : eachUserVO.getOutDangerCount());// 出险次数
                                eachDataRow.createCell(15).setCellValue(
                                        eachUserVO.getBreakRelesCount() == null ? "" : eachUserVO.getBreakRelesCount());// 违章次数
                                eachDataRow.createCell(16).setCellValue(
                                        eachUserVO.getIdCard() == null ? "" : eachUserVO.getIdCard());// 证件号
                                eachDataRow.createCell(17).setCellValue(
                                        eachUserVO.getMobile() == null ? "" : eachUserVO.getMobile());// 电话
                                eachDataRow.createCell(18).setCellValue(
                                        eachUserVO.getIndexNo() == null ? 0 : eachUserVO.getIndexNo());// 序号
                                String status=eachUserVO.getStatus();
                                String isNewCarNo=eachUserVO.getIsNewCarNo();//是否车牌一致
                                String isNewCarOwner=eachUserVO.getIsNewCarOwner();//是否车主一致

                                String resultMessage=eachUserVO.getResultMessage();
                                if (StringUtils.isNotBlank(status)){
                                    if("1".equals(status)){
                                        resultMessage="成功";
                                        if(StringUtils.isNotBlank(isNewCarNo)){
                                            if("0".equals(isNewCarNo)){//车牌一致
                                                isNewCarNo="车牌一致";
                                            }else{//车牌一致
                                                isNewCarNo="车牌不一致";
                                            }
                                        }else{
                                            isNewCarNo="";
                                        }

                                        if(StringUtils.isNotBlank(isNewCarOwner)){
                                            if("0".equals(isNewCarOwner)){//车牌一致
                                                isNewCarOwner="车主一致";
                                            }else{//车牌一致
                                                isNewCarOwner="车主不一致";
                                            }
                                        }else{
                                            isNewCarOwner="";
                                        }

                                    }else if("0".equals(status)){
                                        resultMessage="未爬取";
                                    }else if("2".equals(status)){
                                        isNewCarOwner="获取失败";
                                        isNewCarNo="获取失败";
                                        resultMessage="获取失败";
                                    }
                                }
                                eachDataRow.createCell(18).setCellValue(
                                        eachUserVO.getIndexNo() == null ? 0 : eachUserVO.getIndexNo());// 序号
                                eachDataRow.createCell(19).setCellValue(
                                        isNewCarNo == null ? "" : isNewCarNo);// 车牌是否一致
                                eachDataRow.createCell(20).setCellValue(
                                        isNewCarOwner == null ? "" : isNewCarOwner);// 车主是否一致
                                eachDataRow.createCell(21).setCellValue(
                                        resultMessage == null ? "" : resultMessage);// 爬取结果
                            }
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "导出用户EXCEL成功";
    }

    @Override
    public int getProgress(String seriesNo) {
        int count=0;
        if (redisUtil.hasKey(PROGRESS+seriesNo)){
            return (int) redisUtil.get(PROGRESS+seriesNo);
        }else {
            redisUtil.set(PROGRESS+seriesNo,count);
            return count;
        }
    }

    @Override
    public String startCrawling() {
        return null;
    }
}
