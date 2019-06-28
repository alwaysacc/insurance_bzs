package com.bzs.controller;

import com.bzs.cache.RedisAnnotation;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.redis.RedisUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class Test {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private RedisUtil redisUtil;
    @org.junit.Test
    public void testSelect() {

    }

    public static void main(String[] args) {
        List<String> _first=new ArrayList<String>();
        _first.add("1");
        _first.add("2");
        _first.add("3");
        _first.add("4");
//集合二
        List<String> _second=new ArrayList<String>();
        _second.add("3");
        _second.add("4");
        _second.add("5");
        _second.add("6");

        Collection exists=new ArrayList<String>(_second);
        Collection notexists=new ArrayList<String>(_first);

        exists.removeAll(_first);
        notexists.removeAll(_second);
        System.out.println("_second中不存在于_set中的："+exists);

        System.out.println("_second中存在于_set中的："+notexists);
    }
}
