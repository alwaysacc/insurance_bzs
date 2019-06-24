package com.bzs.dao;

import com.bzs.model.CrawlingExcelInfo;
import com.bzs.utils.Mapper;

public interface CrawlingExcelInfoMapper extends Mapper<CrawlingExcelInfo> {
    int add(CrawlingExcelInfo crawlingExcelInfo);
}