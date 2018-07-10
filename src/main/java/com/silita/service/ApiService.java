package com.silita.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.silita.controller.vo.Page;
import com.silita.dao.TbCompanyMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApiService {

    @Autowired
    TbCompanyMapper tbCompanyMapper;

    public Map<String, Object> getPerson(Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        Integer pageNo = MapUtils.getInteger(param, "pageNo");
        pageNo = pageNo == null ? 1 : pageNo;
        Integer pageSize = MapUtils.getInteger(param, "pageSize");
        pageSize = pageSize == null ? 20 : pageSize;

        // TODO: 获取公司id
        Map<String, Object> companyMap = tbCompanyMapper.queryCompanyInfo(param);
        if (null == companyMap) {
            resultMap.put("code", 1);
            resultMap.put("msg", "未查询到相关数据!");
            return resultMap;
        }

        if(companyMap.get("regisAddress").toString().contains("湖南")){
            companyMap.put("tabCode","hunan");
        }else {
            //TODO: 获取人员表
            companyMap.put("tabCode", tbCompanyMapper.queryProvinceCode(MapUtils.getString(companyMap, "regisAddress")));
        }



        Page page = new Page();
        page.setCurrentPage(pageNo);
        page.setPageSize(pageSize);

        PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
        List<Map<String, Object>> person = tbCompanyMapper.queryPersonList(companyMap);
        if (null == person || person.size() == 0) {
            person = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo(person);
        resultMap.put("data", pageInfo.getList());
        resultMap.put("pageNum", pageInfo.getPageNum());
        resultMap.put("pageSize", pageInfo.getPageSize());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pages", pageInfo.getPages());
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        return resultMap;
    }

    public Map<String, Object> getCompanyQual(Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        // TODO: 获取公司id
        Map<String, Object> companyMap = tbCompanyMapper.queryCompanyInfo(param);
        if (null == companyMap) {
            resultMap.put("code", 1);
            resultMap.put("msg", "未查询到相关数据!");
            return resultMap;
        }

        // TODO: 查询资质
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, List<Map<String, Object>>> qualMap = queryCompanyQualification(companyMap);
        Set<String> set = qualMap.keySet();
        for (String key : set) {
            Map<String, Object> map = new HashMap<>();
            map.put("qualType", key);
            map.put("list", qualMap.get(key));
            list.add(map);
        }
        resultMap.put("data",list);
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        return resultMap;
    }

    public Map<String, List<Map<String, Object>>> queryCompanyQualification(Map<String, Object> param) {
        Map<String, List<Map<String, Object>>> qualMap = new HashMap<>();
        // TODO: 18/4/10 需要关联标准资质表
        List<Map<String, Object>> list = tbCompanyMapper.queryCompanyQual(param);
        for (Map<String, Object> qual:list){
            if (null != qual.get("qualType") && null != qual.get("range")) {
                String key = MapUtils.getString(qual,"qualType");
                List<Map<String, Object>> qualList;
                if (qualMap.get(key) != null) {
                    qualList = qualMap.get(key);
                } else {
                    qualList = new ArrayList<>();

                }
                String[] range = MapUtils.getString(qual,"range").split("；");
                if (range.length < 2) {
                    range = MapUtils.getString(qual,"range").split(";");
                }
                if (range.length < 2) {
                    range = MapUtils.getString(qual,"range").split("\\|");
                }

                if (range.length >= 2) {
                    for (int i = 0; i < range.length; i++) {
                        Map<String, Object> qualT = qual;
                        qualT.put("qualName",range[i]);
                        qualList.add(qualT);
                    }
                } else {
                    qual.put("qualName",range[0]);
                    qualList.add(qual);
                }
                qualMap.put(key, qualList);
            }
        }
        return qualMap;
    }
}
