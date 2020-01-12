package com.bzs.dao;

import com.bzs.model.Address;
import com.bzs.utils.Mapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

public interface AddressMapper extends Mapper<Address> {

    Address getDefaultByUserId(@Param("userId")String userId);

    int updateDefault(@Param("userId")String userId, @Param("id") int id);
}