package com.silita.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushuai on 2019/7/31.
 */
public class BaseController {

    protected Map<String,Object> errorMsg(){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",500);
        resultMap.put("msg","数据查询失败!");
        return resultMap;
    }

}
