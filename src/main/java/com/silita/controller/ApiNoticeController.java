package com.silita.controller;

import com.silita.common.Constant;
import com.silita.service.NoticeService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushuai on 2019/7/31.
 */
@Controller
@RequestMapping("/api/notice")
public class ApiNoticeController extends BaseController{

    private static Logger logger = Logger.getLogger(ApiNoticeController.class);

    @Autowired
    NoticeService noticeService;

    /**
     * 查询公告  -  中标
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zhongbiao/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Map listZhongbiao(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            //校验参数
            Map check = this.checkParam(param);
            if (MapUtils.isNotEmpty(check)){
                return check;
            }
            param.put("type", Constant.NOTICE_TYPE_ZHONGBIAO);
            param.put("requestUrl", Constant.REQUEST_URL_LIST_ZHONGBIAO);
            return noticeService.listNotice(param);
        } catch (Exception e) {
            logger.error("获取公告失败！",e);
            return errorMsg();
        }
    }

    /**
     * 查询公告  -  招标
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zhaobiao/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Map listZhaobiao(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            //校验参数
            Map check = this.checkParam(param);
            if (MapUtils.isNotEmpty(check)){
                return check;
            }
            param.put("type", Constant.NOTICE_TYPE_ZHAOBIAO);
            param.put("requestUrl", Constant.REQUEST_URL_LIST_ZHAOBIAO);
            return noticeService.listNotice(param);
        } catch (Exception e) {
            logger.error("获取公告失败！",e);
            return errorMsg();
        }
    }

    /**
     * 中标详情
     * @param id
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zhongbiao/{id}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Map zhongbiaoDetail(@PathVariable String id, @RequestBody Map<String, Object> param) {
        try {
            //校验参数
            Map check = this.checkParamDetail(param);
            if (MapUtils.isNotEmpty(check)){
                return check;
            }
            param.put("id",id);
            param.put("type",Constant.NOTICE_TYPE_ZHONGBIAO);
            param.put("requestUrl",Constant.REQUEST_URL_DETAIL_ZHONGBIAO);
            return noticeService.detailNoitce(param);
        }catch (Exception e){
            logger.error("中标详情查询失败！",e);
            return errorMsg();
        }
    }

    /**
     * 招标详情
     * @param id
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zhaobiao/{id}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Map zhaobiaoDetail(@PathVariable String id, @RequestBody Map<String, Object> param) {
        try {
            //校验参数
            Map check = this.checkParamDetail(param);
            if (MapUtils.isNotEmpty(check)){
                return check;
            }
            param.put("id",id);
            param.put("type",Constant.NOTICE_TYPE_ZHAOBIAO);
            param.put("requestUrl",Constant.REQUEST_URL_DETAIL_ZHAOBIAO);
            return noticeService.detailNoitce(param);
        }catch (Exception e){
            logger.error("中标详情查询失败！",e);
            return errorMsg();
        }
    }

    private Map<String,Object> checkParam(Map<String,Object> param){
        Map<String,Object> checkMap = new HashedMap(){{
            put("code",404);
        }};
        if (MapUtils.isEmpty(param)){
            checkMap.put("msg","参数不能为空！");
            return checkMap;
        }
        String appId = MapUtils.getString(param,"appId");
        if (StringUtils.isEmpty(appId)){
            checkMap.put("msg","参数appId不能为空！");
            return checkMap;
        }
        return null;
    }

    private Map<String,Object> checkParamDetail(Map<String,Object> param){
        //校验参数
        Map check = this.checkParam(param);
        if (MapUtils.isNotEmpty(check)){
            return check;
        }
        String source = MapUtils.getString(param,"source");
        if (StringUtils.isEmpty(source)){
            Map<String,Object> checkMap = new HashedMap(){{
                put("code",404);
            }};
            checkMap.put("msg","参数source不能为空！");
            return checkMap;
        }
        return null;
    }
}
