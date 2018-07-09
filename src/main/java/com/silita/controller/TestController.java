package com.silita.controller;

import com.silita.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * Created by zhangxiahui on 17/5/26.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> getTestID(@PathVariable String id) throws Exception{
        Map res = null;
        for(int i=0; i<5;i++){
            res = testService.getTestName(id);
        }
        return res;
    }



}
