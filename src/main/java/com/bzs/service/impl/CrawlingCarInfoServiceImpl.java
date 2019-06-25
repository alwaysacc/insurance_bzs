package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.CrawlingCarInfoMapper;
import com.bzs.dao.CrawlingExcelInfoMapper;
import com.bzs.model.CommissionPercentage;
import com.bzs.model.CrawlingCarInfo;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.model.query.CrawlingQuery;
import com.bzs.service.CrawlingCarInfoService;
import com.bzs.service.CrawlingExcelInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.UUIDS;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.excelUtil.ExcelExportUtil;
import com.bzs.utils.excelUtil.WriteExcelDataDelegated;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.PCICResponseBean;
import com.bzs.utils.jsontobean.crawlingcar.CrawlingCarData;
import com.bzs.utils.jsontobean.crawlingcar.CrawlingCarRootBean;
import com.wuwenze.poi.util.POIUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
//            System.out.println("返回body值："+body);
//            log.info("返回body值："+body);
//            CrawlingCarRootBean bean = JSONObject.parseObject(body, CrawlingCarRootBean.class);
        } else {
            body = "{\"code\":" + code + ",\"data\":{},\"msg\":\"" + msg + "\"}";
        }
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
            crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null,"3",null,null);

            return ResultGenerator.genFailResult("请选择需要执行的文件");
        }
        CrawlingExcelInfo excelInfo = crawlingExcelInfoService.findBy("seriesNo", seriesNo);
        if (null == excelInfo) {
            crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null,"3",null,null);

            return ResultGenerator.genFailResult("请选择需要执行的文件");
        } else {
            String status = excelInfo.getStatus();
            String type = excelInfo.getType();
            int count=excelInfo.getTotal();
            if ("1".equals(status)) {//1完成
                crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null,"3",null,null);
                return ResultGenerator.genFailResult("文件已经爬取完毕");
            } else {//状态0未执行 2暂停 3执行中
                String username = "HAICzl01";
                String passWord = "ZLzl123";
                //开始执行修改为执行中
//                crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo,"3");
                int total = crawlingCarInfoMapper.crawlingDataCount(seriesNo);
                if (total > 0) {
                    Integer pageSize = 100;
                    Integer page = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
                    //Integer page = 1;
                    for (int i = 0; i < page; page--) {
                        Integer startRow = (page - 1) * pageSize;
                        List<CrawlingCarInfo> list = crawlingCarInfoMapper.crawlingDataList(seriesNo, startRow, pageSize);
                        if (CollectionUtils.isNotEmpty(list)) {
                            for (CrawlingCarInfo data : list) {
                                String carNo = data.getCarNo();
                                String vinNo = data.getVinNo();
                                String carOwner = data.getCarOwner();
                                String no = "";
                                Long id = data.getId();
                                int indexNo=data.getIndexNo();
                                if((indexNo+"").equals(count+"")){
                                    crawlingExcelInfoService.updateCrawlingFinish(seriesNo, id.intValue(),"1",total,new Date());
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

                                    CrawlingCarData resData = bean.getData();
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
                                        log.info("车辆和型号："+resData.getCar_type()+",品牌："+resData.getCn_type());
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
                                    data.setStatus("2");//失败
                                    if (resultMap.length() > 255) {
                                        resultMap = resultMap.substring(0, 255);
                                    }
                                    data.setResultMessage(resultMap);
                                }
                                crawlingCarInfoMapper.crawlingUpdate(data);//修改爬取的车辆信息
                            }
                        } else {
                            break;
                        }
                        // return ResultGenerator.genSuccessResult(list, "文件已经爬取完毕");
                    }
                    crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo,"1");
                    return ResultGenerator.genSuccessResult("文件已经爬取完毕");

                } else {
                    crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo,"1");
                    return ResultGenerator.genFailResult("文件已经爬取完毕");
                }
            }
        }
    }

    @Override
    public String exportCrawlingDataList(HttpServletResponse response, HttpServletRequest request, String seriesNo) {
        int totalRowCount = exportDataCountBySeriesNo(seriesNo);
        // 导出EXCEL文件名称
        String path= request.getSession().getServletContext().getRealPath("");
      //  path=request.getSession().getServletContext().getRealPath(request.getRequestURI());
        String filaName = UUIDS.getDateUUID();
        path+=filaName+".xlsx";
        // 标题
        String[] titles = {"车辆牌照","新车牌", "车主","新车主","车架号",
                "品牌","车辆型号","发动机号","登记日期","过户日期(没有过户没有)",
                "交强险承保公司", "交强险到期日期", "商业险承保公司", "商业险到期日期", "出险次数",
                "违章次数", "身份证号码","联系电话","序号"};
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
                                eachDataRow.createCell(0).setCellValue(
                                        eachUserVO.getCarNo() == null ? "" : eachUserVO.getCarNo());// 车辆牌照
                                eachDataRow.createCell(1).setCellValue(
                                        eachUserVO.getNewCarNo() == null ? "" : eachUserVO.getNewCarNo());// 新车牌
                                eachDataRow.createCell(2).setCellValue(
                                        eachUserVO.getCarOwner() == null ? "" : eachUserVO.getCarOwner());// 车主
                                eachDataRow.createCell(3).setCellValue(
                                        eachUserVO.getNewCarOwner() == null ? "" : eachUserVO.getNewCarOwner());// 新车主
                                eachDataRow.createCell(4).setCellValue(
                                        eachUserVO.getVinNo() == null ? "" : eachUserVO.getVinNo());// 车架号
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
                                        eachUserVO.getBizCompany()== null ? "" : eachUserVO.getBizCompany());// 商业险承保公司
                                eachDataRow.createCell(13).setCellValue(
                                        eachUserVO.getBizEndDate()== null ? "" : eachUserVO.getBizEndDate());// 商业险到期日期

                                eachDataRow.createCell(14).setCellValue(
                                        eachUserVO.getOutDangerCount()== null ? "" : eachUserVO.getOutDangerCount());// 出险次数
                                eachDataRow.createCell(15).setCellValue(
                                        eachUserVO.getBreakRelesCount()== null ? "" : eachUserVO.getBreakRelesCount());// 违章次数
                                eachDataRow.createCell(16).setCellValue(
                                        eachUserVO.getIdCard()== null ? "" : eachUserVO.getIdCard());// 证件号
                                eachDataRow.createCell(17).setCellValue(
                                        eachUserVO.getMobile()== null ? "" : eachUserVO.getMobile());// 电话
                                eachDataRow.createCell(18).setCellValue(
                                        eachUserVO.getIndexNo()== null ? 0 : eachUserVO.getIndexNo());// 电话
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
}
