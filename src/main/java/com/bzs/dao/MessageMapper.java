package com.bzs.dao;

import com.bzs.model.Message;
import com.bzs.utils.Mapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper extends Mapper<Message> {
    List getList();
    List getListByUserId(@Param("userId")String userId, @Param("status")Integer status);

}