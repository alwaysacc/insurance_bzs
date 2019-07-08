package com.bzs.dao;

import com.bzs.model.CardInfo;
import com.bzs.utils.Mapper;

public interface CardInfoMapper extends Mapper<CardInfo> {

    int saveCardInfo(CardInfo cardInfo);
}