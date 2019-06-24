package com.bzs.utils.excelUtil;

import com.bzs.utils.dateUtil.DateUtil;
import com.wuwenze.poi.util.POIUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 导出excel
 * @author: dengl
 * @create: 2019-06-21 17:27
 */
@Slf4j
public class ExcelExportUtil {
    /**
     * 初始化EXCEL(sheet个数和标题)
     *
     * @param totalRowCount
     *            总记录数
     * @param titles
     *            标题集合
     * @return XSSFWorkbook对象
     */
    public static SXSSFWorkbook initExcel(Integer totalRowCount, String[] titles) {

        // 在内存当中保持 100 行 , 超过的数据放到硬盘中在内存当中保持 100 行 , 超过的数据放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);

        Integer sheetCount = ((totalRowCount
                % ExcelInterface.PER_SHEET_ROW_COUNT == 0) ? (totalRowCount / ExcelInterface.PER_SHEET_ROW_COUNT)
                : (totalRowCount / ExcelInterface.PER_SHEET_ROW_COUNT + 1));

        // 根据总记录数创建sheet并分配标题
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet sheet = wb.createSheet("sheet" + (i + 1));
            SXSSFRow headRow = sheet.createRow(0);
            for (int j = 0; j < titles.length; j++) {
                SXSSFCell headRowCell = headRow.createCell(j);
                headRowCell.setCellValue(titles[j]);
                //可在此处添加标题字段如licensePlate、userName等
            }
        }

        return wb;

    }

    /**
     * 下载EXCEL到本地指定的文件夹
     *
     * @param wb
     *            EXCEL对象SXSSFWorkbook
     * @param exportPath
     *            导出路径
     */
    public static void downLoadExcelToLocalPath(SXSSFWorkbook wb,
                                                String exportPath) {
        FileOutputStream fops = null;
        try {
            fops = new FileOutputStream(exportPath);
            wb.write(fops);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != fops) {
                try {
                    fops.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载EXCEL到浏览器
     *
     * @param wb
     *            EXCEL对象XSSFWorkbook
     * @param response
     * @param fileName
     *            文件名称
     * @throws IOException
     */
    public static void downLoadExcelToWebsite(SXSSFWorkbook wb,
                                              HttpServletResponse response, String fileName) throws IOException {

        response.setHeader("Content-disposition", "attachment; filename="
                + new String((fileName + ".xlsx").getBytes("utf-8"),
                "ISO8859-1"));// 设置下载的文件名

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出Excel到本地指定路径
     *
     * @param totalRowCount           总记录数
     * @param titles                  标题
     * @param exportPath              导出路径
     * @param writeExcelDataDelegated 向EXCEL写数据/处理格式的委托类 自行实现
     * @throws Exception
     */
    public static final void exportExcelToLocalPath(Integer totalRowCount, String[] titles, String exportPath, List list, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {

        log.info("开始导出：" + DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));

        // 初始化EXCEL
        SXSSFWorkbook wb = ExcelExportUtil.initExcel(totalRowCount, titles);
      /* if(totalRowCount>){

       }*/

        // 调用委托类分批写数据
        int sheetCount = wb.getNumberOfSheets();
        System.out.println("sheetCount"+sheetCount);
        log.info("sheetCount"+sheetCount);
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet eachSheet = wb.getSheetAt(i);
            for (int j = 1; j <= ExcelInterface.PER_SHEET_WRITE_COUNT; j++) {
                int currentPage = i * ExcelInterface.PER_SHEET_WRITE_COUNT + j;
                int pageSize = ExcelInterface.PER_WRITE_ROW_COUNT;
                int startRowCount = (j - 1) * ExcelInterface.PER_WRITE_ROW_COUNT + 1;
                int endRowCount = startRowCount + pageSize - 1;
                writeExcelDataDelegated.writeExcelData(eachSheet, startRowCount, endRowCount, currentPage, pageSize);
            }
        }


        // 下载EXCEL
        ExcelExportUtil.downLoadExcelToLocalPath(wb, exportPath);

        log.info("导出完成：" + DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 导出Excel到浏览器
     *
     * @param response
     * @param totalRowCount           总记录数
     * @param fileName                文件名称
     * @param titles                  标题
     * @param writeExcelDataDelegated 向EXCEL写数据/处理格式的委托类 自行实现
     * @throws Exception
     */
    public static final void exportExcelToWebsite(HttpServletResponse response, Integer totalRowCount, String fileName, String[] titles, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {

        log.info("开始导出：" + DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));

        // 初始化EXCEL
        SXSSFWorkbook wb = ExcelExportUtil.initExcel(totalRowCount, titles);
        int forCount=((totalRowCount%ExcelInterface.PER_WRITE_ROW_COUNT)==0)?(totalRowCount/ExcelInterface.PER_WRITE_ROW_COUNT):(totalRowCount/ExcelInterface.PER_WRITE_ROW_COUNT+1);
//https://blog.csdn.net/qq_35206261/article/details/82844159

        // 调用委托类分批写数据
        int sheetCount = wb.getNumberOfSheets();
        // System.out.println("sheetCount"+sheetCount);
        log.info("sheetCount"+sheetCount);
        int ss=forCount<ExcelInterface.PER_SHEET_WRITE_COUNT?forCount:ExcelInterface.PER_SHEET_WRITE_COUNT;
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet eachSheet = wb.getSheetAt(i);
            for (int j = 1; j <=forCount ; j++) {
                int currentPage = i * ss + j;
                int pageSize = ExcelInterface.PER_WRITE_ROW_COUNT;
                int startRowCount = (j - 1) * ExcelInterface.PER_WRITE_ROW_COUNT + 1;
                int endRowCount = startRowCount + pageSize - 1;
                if(endRowCount>totalRowCount){
                    endRowCount=startRowCount+totalRowCount%ExcelInterface.PER_WRITE_ROW_COUNT -1;
                }
                writeExcelDataDelegated.writeExcelData(eachSheet, startRowCount, endRowCount, currentPage, pageSize);
            }
        }
        // 下载EXCEL
        ExcelExportUtil.downLoadExcelToWebsite(wb, response, fileName);
        log.info("导出完成：" + DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }



}
