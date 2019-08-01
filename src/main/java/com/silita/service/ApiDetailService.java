package com.silita.service;

import com.silita.dao.TbApiDetailMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 远程接口调用详细表 service
 * Created by zhushuai on 2019/7/31.
 */
@Service
public class ApiDetailService {

    @Autowired
    TbApiDetailMapper tbApiDetailMapper;

    /**
     * 校验api是否可以调用
     *
     * @param param
     * @param resultMap
     * @return
     */
    public Boolean chechApiRemote(Map<String, Object> param, Map<String, Object> resultMap) {
        Map<String, Object> apiMap = tbApiDetailMapper.queryApiDetail(param);
        if (MapUtils.isEmpty(apiMap)) {
            resultMap.put("code", 405);
            resultMap.put("msg", "请填入正确的appId！");
            return false;
        }
        Integer callTotal = MapUtils.getInteger(apiMap, "callTotal");
        Integer callCount = MapUtils.getInteger(apiMap, "callCount");
        Integer residueCount = MapUtils.getInteger(apiMap, "residueCount");
        if (callTotal - callCount <= 0) {
            resultMap.put("code", 405);
            resultMap.put("msg", "无权限调用！");
            return false;
        }
        callCount += 1;
        residueCount -= 1;
        apiMap.put("callCount", callCount);
        apiMap.put("residueCount", residueCount);
        resultMap.put("residueCount", residueCount);
        tbApiDetailMapper.update(apiMap);
        return true;
    }

}
