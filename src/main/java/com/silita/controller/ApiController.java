package com.silita.controller;

import com.silita.service.ApiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/companyApi")
public class ApiController {

    private static Logger logger = Logger.getLogger(ApiController.class);

    @Autowired
    ApiService apiService;

    /**
     * 人员
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/person", method = RequestMethod.POST)
    public Map<String,Object> person(@RequestBody Map<String,Object> param){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap = apiService.getPerson(param);
            return resultMap;
        }catch (Exception e){
            logger.error("数据查询失败",e);
            resultMap.put("code",500);
            resultMap.put("msg","数据查询失败!");
            return resultMap;
        }
    }

    /**
     * 资质
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/qual", method = RequestMethod.POST)
    public Map<String,Object> qual(@RequestBody Map<String,Object> param){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap = apiService.getCompanyQual(param);
            return resultMap;
        }catch (Exception e){
            logger.error("数据查询失败",e);
            resultMap.put("code",500);
            resultMap.put("msg","数据查询失败!");
            return resultMap;
        }
    }
}
