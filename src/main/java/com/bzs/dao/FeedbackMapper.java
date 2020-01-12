package com.bzs.dao;

import com.bzs.model.Feedback;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeedbackMapper extends Mapper<Feedback> {

    int updateImageUrl(@Param("imageUrl")String imageUrl,@Param("id")int id);

    List getList(@Param("status") String status);
}