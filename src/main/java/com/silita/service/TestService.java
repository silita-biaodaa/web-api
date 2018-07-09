package com.silita.service;

import com.silita.dao.TestMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by zhangxiahui on 18/3/13.
 */
@Component
public class TestService {

    @Autowired
    TestMapper testMapper;

    Logger logger = Logger.getLogger(TestService.class);

    @Cacheable(value="testCache",key="#id")
    public Map getTestName(String id){
        logger.info("查询数据库："+id);
        return testMapper.getTestName(id);
    }




}
