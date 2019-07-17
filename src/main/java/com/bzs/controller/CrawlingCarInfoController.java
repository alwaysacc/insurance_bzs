package com.bzs.controller;

import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.CrawlingExcelInfoMapper;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.model.query.CrawlingQuery;
import com.bzs.redis.RedisUtil;
import com.bzs.service.CrawlingExcelInfoService;
import com.bzs.service.impl.CrawlingCarInfoServiceImpl;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.CrawlingCarInfo;
import com.bzs.service.CrawlingCarInfoService;
import com.bzs.utils.UUIDS;
import com.bzs.utils.async.AsyncVo;
import com.bzs.utils.async.RequestQueue;
import com.bzs.utils.excelUtil.ExcelImportUtil;
import com.bzs.utils.redisConstant.RedisConstant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bzs.utils.excelUtil.ExcelImportUtil.readExcel;

/**
 * Created by dl on 2019/06/19.
 */
@RestController
@RequestMapping("/crawling/carinfo")
@EnableAsync
public class CrawlingCarInfoController {
    private static final Logger log = LoggerFactory.getLogger(CrawlingCarInfoController.class);
    @Resource
    private CrawlingCarInfoService crawlingCarInfoService;
    @Resource
    private CrawlingExcelInfoService crawlingExcelInfoService;
    @Resource
    private CrawlingExcelInfoMapper crawlingExcelInfoMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RequestQueue queue;


    @PostMapping("/add")
    public Result add(CrawlingCarInfo crawlingCarInfo) {
        crawlingCarInfoService.save(crawlingCarInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        crawlingCarInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(CrawlingCarInfo crawlingCarInfo) {
//        crawlingCarInfoService.update(crawlingCarInfo);
        return ResultGenerator.genSuccessResult(crawlingExcelInfoMapper.updateStatus(crawlingCarInfo.getSeriesNo(),"3"));
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        CrawlingCarInfo crawlingCarInfo = crawlingCarInfoService.findById(id);
        return ResultGenerator.genSuccessResult(crawlingCarInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<CrawlingCarInfo> list = crawlingCarInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @ApiOperation("上传需要导入的数据")
    @PostMapping("/import")
    public Result importExcel(@RequestParam(value = "file", required = false) MultipartFile file, String createBy, @RequestParam(defaultValue = "2") String type,String accountId) {
        String fileName = new File(file.getOriginalFilename()).getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String seriesNo = UUIDS.getDateUUID();
        if (suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx")) {
            long start = System.currentTimeMillis();
            try {
                File f = File.createTempFile(suffix, null);
                file.transferTo(f);
                f.deleteOnExit();
                String path = f.getAbsolutePath();
                readExcel(suffix, path, fileName, crawlingCarInfoService, crawlingExcelInfoService, seriesNo, createBy, type,accountId);
            } catch (Exception e) {
                return ResultGenerator.genFailResult("上传异常");
            }
            long end = System.currentTimeMillis();
            System.out.println((end - start) / 1000);
            return ResultGenerator.genSuccessResult("上传成功");
        } else {
            return ResultGenerator.genFailResult("文件格式错误，只能上传xls和xlsx文件");
        }

    }

    /**
     * 开始爬取
     *
     * @return
     */
    /*@ApiOperation("执行爬取")
    @PostMapping("/startCrawling")
    public Result startCrawling(String seriesNo){
        crawlingExcelInfoService.updateCrawlingFinish(seriesNo, null,"3",null,null);
        crawlingCarInfoService.startCrawling(seriesNo);
        return ResultGenerator.genSuccessResult();
    }*/
    @ApiOperation("执行爬取")
    @PostMapping("/startCrawling")
    public Result startCrawling(String seriesNo) {
        int result=0;
        synchronized (this){
            List list = new ArrayList();
            if (!redisUtil.hasKey(RedisConstant.CRAWLING_LIST)) {
                list.add(seriesNo);
                redisUtil.set(RedisConstant.CRAWLING_LIST, list);
                crawlingCarInfoService.startCrawling1();
            } else {
                list = (List) redisUtil.get(RedisConstant.CRAWLING_LIST);
                list.add(seriesNo);
                redisUtil.set(RedisConstant.CRAWLING_LIST, list);
            }
            result=crawlingExcelInfoMapper.updateCrawlingStatus(seriesNo, "3");
        }

        return ResultGenerator.genSuccessResult(result);
    }

    @ApiOperation("导出数据")
    @GetMapping("/exportCrawlingDataList")
    public void exportCrawlingDataList(HttpServletResponse response, HttpServletRequest request, String seriesNo) {
        crawlingCarInfoService.exportCrawlingDataList(response, request, seriesNo);
    }


    // @PostMapping("/batchInsertImportTest")
    public Result batchInsertImportTest() {
        List<CrawlingCarInfo> list = new ArrayList<>();
        CrawlingCarInfo carInfo = null;
        for (int i = 0; i < 150000; i++) {
            carInfo = new CrawlingCarInfo("carNo" + i, "carOwner" + i, "vinNo" + i, "idCard" + i, "mobile" + i, i + "", "seriesNo" + i, i);
            list.add(carInfo);
        }
        int result = crawlingCarInfoService.batchInsertImport(list);
        if (result > 0) {
            return ResultGenerator.genSuccessResult(result, "批量添加成功");
        } else {
            return ResultGenerator.genFailResult("批量添加失败");
        }
    }

    @ApiOperation("爬取测试")
    @PostMapping("/textss")
    public Result textss(String username, String password, String flag, String no) {
        String map = crawlingCarInfoService.httpCrawling(username, password, flag, no);
        return ResultGenerator.genSuccessResult(map);
    }
    @ApiOperation("获取已爬取数量，进度条")
    @PostMapping("/getProgress")
    public Result getProgress(String seriesNo) {
        return ResultGenerator.genSuccessResult(crawlingCarInfoService.getProgress(seriesNo));
    }
    @PostMapping("/exportDataListBySeriesNo")
    public Result exportDataListBySeriesNo(String seriesNo) {
        List list = crawlingCarInfoService.exportDataListBySeriesNo(seriesNo);
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping("/exportDataCountBySeriesNo")
    public int exportDataCountBySeriesNo(String seriesNo) {
        return crawlingCarInfoService.exportDataCountBySeriesNo(seriesNo);
    }

    @PostMapping("/crawlingDataList")
    public List crawlingDataList(String seriesNo, Integer startRow, Integer pageSize) {
        return crawlingCarInfoService.crawlingDataList(seriesNo, startRow, pageSize);
    }

    @PostMapping("/crawlingDataCount")
    public int crawlingDataCount(String seriesNo) {
        return crawlingCarInfoService.crawlingDataCount(seriesNo);
    }

    @PostMapping("/crawlingtest1")
    public DeferredResult<Object> order(String seriesNo, String loginName, String logPwd, String flag, String no) throws InterruptedException {
        System.out.println("[ OrderController ] 接到爬取的请求");
        System.out.println("当前待处理爬取的数： " + queue.getCrawlingQueue().size());
        AsyncVo<String, Object> vo = new AsyncVo<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seriesNo", seriesNo);
        jsonObject.put("loginName", loginName);
        jsonObject.put("logPwd", logPwd);
        jsonObject.put("flag", flag);
        jsonObject.put("no", no);
        DeferredResult<Object> result = new DeferredResult<>();
        vo.setParams(jsonObject.toJSONString());
        vo.setResult(result);
        queue.getCrawlingQueue().put(vo);
        System.out.println("[ OrderController ] 返回爬取结果");
        return result;
    }
}
