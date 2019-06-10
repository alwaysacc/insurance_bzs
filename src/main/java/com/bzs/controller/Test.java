package com.bzs.controller;

import com.bzs.dao.OrderInfoMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @org.junit.Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        System.out.println(  orderInfoMapper.getOrderIdByQuoteId("20190610145906399430"));
    }
}
