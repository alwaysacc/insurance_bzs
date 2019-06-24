package com.bzs.utils.excelUtil;

import java.util.List;
import java.util.Map;

public interface ExcelInterface {
    /**
     *
     * @param filename
     * @return
     * @throws Exception
     */
    /**
     * 每个sheet存储的记录数 100W
     */
    public static final Integer PER_SHEET_ROW_COUNT = 1000000;

    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小) 20W
     */
    public static final Integer PER_WRITE_ROW_COUNT = 200000;
    /**
     * 每个sheet的写入次数 5
     */
    public static final Integer PER_SHEET_WRITE_COUNT = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT;

    public int process(String filename) throws Exception;
    public List<Map<String,Object>> getListMap();
}
