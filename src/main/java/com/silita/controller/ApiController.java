package com.silita.controller;

import com.silita.service.ApiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/companyApi")
public class ApiController extends BaseController {

    private static Logger logger = Logger.getLogger(ApiController.class);

    @Autowired
    ApiService apiService;

    /**
     * 人员
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/person", method = RequestMethod.POST)
    public Map<String, Object> person(@RequestBody Map<String, Object> param) {
        try {
            return apiService.getPerson(param);
        } catch (Exception e) {
            logger.error("数据查询失败", e);
            return errorMsg();
        }
    }

    /**
     * 资质
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/qual", method = RequestMethod.POST)
    public Map<String, Object> qual(@RequestBody Map<String, Object> param) {
        try {
            return apiService.getCompanyQual(param);
        } catch (Exception e) {
            logger.error("数据查询失败", e);
            return errorMsg();
        }
    }
}
