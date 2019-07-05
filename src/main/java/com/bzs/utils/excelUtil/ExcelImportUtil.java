package com.bzs.utils.excelUtil;

import com.bzs.dao.CrawlingCarInfoMapper;
import com.bzs.model.CrawlingCarInfo;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.service.CrawlingCarInfoService;
import com.bzs.service.CrawlingExcelInfoService;
import com.bzs.service.impl.CrawlingCarInfoServiceImpl;
import com.bzs.utils.UUIDS;
import com.bzs.utils.encodeUtil.ConversionUtil;
import com.bzs.utils.reflactUtil.ReflactUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-19 16:09
 */
public class ExcelImportUtil {
    private static final Logger logger =LoggerFactory.getLogger(ExcelImportUtil.class);
    //excel2003扩展名
    public static final String EXCEL03_EXTENSION = ".xls";
    //excel2007扩展名
    public static final String EXCEL07_EXTENSION = ".xlsx";

    public static List<String> list=new ArrayList<String>();
    public static List<CrawlingCarInfo>listdata=new ArrayList<CrawlingCarInfo>();
    public static List<String>titleName=new ArrayList<String>();
    public static List<String>titleField=new ArrayList<String>();
    public static List<Map<String,Object>>listMap=new ArrayList<Map<String,Object>>();
    public static CrawlingCarInfoService crawlingCarInfoService;



    public static List<Map<String, Object>> getListMap() {
        return listMap;
    }

    public static void setListMap(List<Map<String, Object>> listMap) {
        ExcelImportUtil.listMap = listMap;
    }

    public static void getTitle(Boolean isTitle,String filePath, String sheetName, int sheetIndex, int curRow, List<String> cellList) {
        if(isTitle){
            titleName.clear();
            titleField.clear();
            for (String cell : cellList) {
                titleName.add(cell.trim());
            }
            if(titleName!=null&&titleName.size()>0){
                int count =1;
                for (String name : titleName) {
                    if(name.indexOf("车牌号")>-1||name.indexOf("车辆牌照")>-1){
                        titleField.add("carNo");
                    }else if(name.indexOf("车主")>-1){
                        titleField.add("carOwner");
                    }else if(name.indexOf("身份证")>-1||name.indexOf("证件号码")>-1){
                        titleField.add("idCard");
                    }else if(name.indexOf("联系电话")>-1||name.indexOf("手机号码")>-1){
                        titleField.add("mobile");
                    }else if(name.indexOf("引擎号")>-1||name.indexOf("发动机号")>-1){
                        titleField.add("engineNo");
                    }else if(name.indexOf("车架")>-1){
                        titleField.add("vinNo");
                    }else if(name.indexOf("型号")>-1){
                        titleField.add("model");
                    }else if(name.indexOf("品牌")>-1){
                        titleField.add("brand");
                    }else {
                        count++;
                        titleField.add(count+"");
                    }
                }
            }
        }
    }
    public static  Map<String,Object> readExcel(String suffix,String path, String fileName, CrawlingCarInfoService basedao, CrawlingExcelInfoService crawlingExcelInfoService, String seriesNo, String createBy,String type) throws Exception {
        crawlingCarInfoService=basedao;
        ExcelInterface excelInterface;
        int totalRows =0;
        suffix="."+suffix;
        if (suffix.endsWith(EXCEL03_EXTENSION)) { //处理excel2003文件
            excelInterface=new ExcelXlsReader() {

                @Override
                public void sendRow(Boolean isTitle, String filePath, String sheetName,
                                    int sheetIndex, int curRow,List<String>cellList, List<Map<String, Object>> listMap) {
                    System.out.println(Arrays.asList(cellList));
                    logger.info("打印导入数据》》》"+Arrays.asList(cellList));
                    if(isTitle){
                        titleName.clear();
                        titleField.clear();
                        for (String cell : cellList) {
                            titleName.add(cell.trim());
                        }
                        if(titleName!=null&&titleName.size()>0){
                            int count =1;
                            for (String name : titleName) {
                                if(name.indexOf("车牌号")>-1||name.indexOf("车辆牌照")>-1){
                                    titleField.add("carNo");
                                }else if(name.indexOf("车主")>-1){
                                    titleField.add("carOwner");
                                }else if(name.indexOf("身份证")>-1||name.indexOf("证件号码")>-1){
                                    titleField.add("idCard");
                                }else if(name.indexOf("联系电话")>-1||name.indexOf("手机号码")>-1){
                                    titleField.add("mobile");
                                }else if(name.indexOf("引擎号")>-1||name.indexOf("发动机号")>-1){
                                    titleField.add("engineNo");
                                }else if(name.indexOf("车架")>-1){
                                    titleField.add("vinNo");
                                }else if(name.indexOf("型号")>-1){
                                    titleField.add("model");
                                }else if(name.indexOf("品牌")>-1){
                                    titleField.add("brand");
                                }else {
                                    count++;
                                    titleField.add(count+"");
                                }
                            }
                        }
                    }else{

                        if(CollectionUtils.isNotEmpty(titleField)){
                            boolean f=false;
                            Map<String,Object>map=new HashMap<String,Object>();
                            for (int i = 0; i < cellList.size(); i++) {
                                String type=ReflactUtil.getFiledTypeByFiledName(titleField.get(i),new CrawlingCarInfo());//获取类型
                                map.put(titleField.get(i),ConversionUtil.StringToObject(type,cellList.get(i).trim()));
                                if("carNo".equals(titleField.get(i))||"vinNo".equals(titleField.get(i))){
                                    f=true;
                                }
                            }
                            if(f){
                                map.put("createBy",createBy);
                                map.put("seriesNo",seriesNo);
                                map.put("indexNo",curRow-1);
                                listMap.add(map);
                                if(listMap.size()==1000){
                                    List<CrawlingCarInfo> listData=new ArrayList<CrawlingCarInfo>();
                                    for (Map<String, Object> maps : listMap) {
                                        CrawlingCarInfo data= (CrawlingCarInfo) ReflactUtil.finalObject(maps, new CrawlingCarInfo());
                                        listData.add(data);
                                    }
                                    basedao.batchInsertImport(listData);
                                    listMap.clear();
                                }
                            }else{
                                logger.info("请补充标题行");
                            }

                        }else{
                            logger.info("请补充标题行");
                        }



                    }


                }
            };
            totalRows =excelInterface.process(path);
            List<Map<String, Object>> l=excelInterface.getListMap();
            if(CollectionUtils.isNotEmpty(l)){
                List<CrawlingCarInfo> listData=new ArrayList<CrawlingCarInfo>();
                for (Map<String, Object> maps : l) {
                    CrawlingCarInfo data= (CrawlingCarInfo) ReflactUtil.finalObject(maps, new CrawlingCarInfo());
                    listData.add(data);
                }
                basedao.batchInsertImport(listData);
                l.clear();
            }
        } else if (suffix.endsWith(EXCEL07_EXTENSION)) {//处理excel2007文件
            excelInterface = new ExcelXlsxReader() {

                @Override
                public void sendRow(Boolean isTitle, String filePath, String sheetName,
                                    int sheetIndex, int curRow, List<String> cellList,
                                    List<Map<String, Object>> listMap) {
                    if(isTitle){
                        titleName.clear();
                        titleField.clear();
                        for (String cell : cellList) {
                            titleName.add(cell.trim());
                        }
                        if(titleName!=null&&titleName.size()>0){
                            int count =0;
                            for (String name : titleName) {
                                if(name.indexOf("车牌号")>-1||name.indexOf("车辆牌照")>-1){
                                    titleField.add("carNo");
                                }else if(name.indexOf("车主")>-1){
                                    titleField.add("carOwner");
                                }else if(name.indexOf("身份证")>-1||name.indexOf("证件号码")>-1){
                                    titleField.add("idCard");
                                }else if(name.indexOf("联系电话")>-1||name.indexOf("手机号码")>-1){
                                    titleField.add("mobile");
                                }else if(name.indexOf("引擎号")>-1||name.indexOf("发动机号")>-1){
                                    titleField.add("engineNo");
                                }else if(name.indexOf("车架")>-1){
                                    titleField.add("vinNo");
                                }else if(name.indexOf("型号")>-1){
                                    titleField.add("model");
                                }else if(name.indexOf("品牌")>-1){
                                    titleField.add("brand");
                                }else {
                                    count++;
                                    titleField.add(count+"");
                                }
                            }
                        }
                    }else{

                        if(CollectionUtils.isNotEmpty(titleField)){
                            boolean f=false;
                            Map<String,Object>map=new HashMap<String,Object>();
                            for (int i = 0; i < cellList.size(); i++) {
                                String type=ReflactUtil.getFiledTypeByFiledName(titleField.get(i),new CrawlingCarInfo());//获取类型
                                map.put(titleField.get(i),ConversionUtil.StringToObject(type,cellList.get(i).trim()));
                                if("carNo".equals(titleField.get(i))||"vinNo".equals(titleField.get(i))){
                                    f=true;
                                }
                            }
                            if(f){
                                map.put("createBy",createBy);
                                map.put("seriesNo",seriesNo);
                                map.put("indexNo",curRow-1);
                                listMap.add(map);
                                if(listMap.size()==1000){
                                    List<CrawlingCarInfo> listData=new ArrayList<CrawlingCarInfo>();
                                    for (Map<String, Object> maps : listMap) {
                                        CrawlingCarInfo data= (CrawlingCarInfo) ReflactUtil.finalObject(maps, new CrawlingCarInfo());
                                        listData.add(data);
                                       // basedao.save(data);
                                    }
                                    basedao.batchInsertImport(listData);
                                    listMap.clear();
                                }
                            }else{
                                logger.info("请补充标题行");
                            }
                        }else{
                            logger.info("请补充标题行");
                        }
                    }

                }
            };
            totalRows = excelInterface.process(path);
            List<Map<String, Object>> l=excelInterface.getListMap();
            if(CollectionUtils.isNotEmpty(l)){
                List<CrawlingCarInfo> listData=new ArrayList<CrawlingCarInfo>();
                for (Map<String, Object> maps : listMap) {
                    CrawlingCarInfo data= (CrawlingCarInfo) ReflactUtil.finalObject(maps, new CrawlingCarInfo());
                    listData.add(data);
                  //  basedao.save(data);
                }
                basedao.batchInsertImport(listData);
                l.clear();
            }
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
        System.out.println("发送的总行数：" + totalRows);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("totalRows", totalRows);
        map.put("code", excelInterface);
        CrawlingExcelInfo data=new CrawlingExcelInfo(fileName,seriesNo,createBy,type,totalRows);
        crawlingExcelInfoService.add(data);
        return map;
    }

    public static void main(String[] args) throws Exception {
        String path="B:\\testExcelData\\模板不要删.xls";
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),ContentType.APPLICATION_OCTET_STREAM.toString(),fileInputStream);
        System.out.println(multipartFile.getName()); // 输出copytest.txt
        String fileName = new File(multipartFile.getOriginalFilename()).getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        File f = File.createTempFile(suffix, null);
        multipartFile.transferTo(f);
        f.deleteOnExit();
        String paths = f.getAbsolutePath();
        //readExcel(suffix, paths, new CrawlingCarInfoServiceImpl(),UUIDS.getDateUUID(),"1");
    }
}
