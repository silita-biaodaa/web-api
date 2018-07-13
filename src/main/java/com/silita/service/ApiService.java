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
            resultMap.put("code", 204);
            resultMap.put("msg", "未查询到相关数据!");
            return resultMap;
        }

        if (companyMap.get("regisAddress").toString().contains("湖南")) {
            companyMap.put("tabCode", "hunan");
        } else {
            //TODO: 获取人员表
            companyMap.put("tabCode", tbCompanyMapper.queryProvinceCode(MapUtils.getString(companyMap, "regisAddress")));
        }


        Page page = new Page();
        page.setCurrentPage(pageNo);
        page.setPageSize(pageSize);

        PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
        List<Map<String, Object>> person = tbCompanyMapper.queryPersonList(companyMap);
        if (null == person || person.size() == 0) {
            resultMap.put("code", 204);
            resultMap.put("msg", "未查询到相关数据!");
            return resultMap;
        }
        PageInfo pageInfo = new PageInfo(person);
        resultMap.put("data", pageInfo.getList());
        resultMap.put("pageNum", pageInfo.getPageNum());
        resultMap.put("pageSize", pageInfo.getPageSize());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pages", pageInfo.getPages());
        if (pageNo > pageInfo.getPages()) {
            resultMap = new HashMap<>();
            resultMap.put("code", 416);
            resultMap.put("msg", "页数大于总页数，请重新输入!");
            return resultMap;
        }
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        return resultMap;
    }

    public Map<String, Object> getCompanyQual(Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        // TODO: 获取公司id
        Map<String, Object> companyMap = tbCompanyMapper.queryCompanyInfo(param);
        if (null == companyMap) {
            resultMap.put("code", 204);
            resultMap.put("msg", "未查询到相关数据!");
            return resultMap;
        }

        // TODO: 查询资质
        List<Map<String, Object>> list = new ArrayList<>();
        list = queryCompanyQualification(companyMap);
        if (null == list && list.size() == 0) {
            resultMap.put("code", 204);
            resultMap.put("msg", "未查询到相关数据!");
            return resultMap;
        }
        resultMap.put("data", list);
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        return resultMap;
    }

    public List<Map<String, Object>> queryCompanyQualification(Map<String, Object> param) {
        // TODO: 18/4/10 需要关联标准资质表
        List<Map<String, Object>> list = tbCompanyMapper.queryCompanyQual(param);
        List<Map<String, Object>> qualList = new ArrayList<>();
        for (Map<String, Object> qual : list) {
            if (null != qual.get("qualType") && null != qual.get("range")) {
                String[] range = MapUtils.getString(qual, "range").split(",");
                for (int i = 0; i < range.length; i++) {
                    Map<String, Object> map = initMap(qual);
                    map.put("qualName", range[i]);
                    qualList.add(map);
                }
            }
        }
        return qualList;
    }


    private Map<String, Object> initMap(Map<String, Object> param) {
        Map<String, Object> initMap = new HashMap<>();
        initMap.put("tab", param.get("tab"));
        initMap.put("certNo", param.get("certNo"));
        initMap.put("certOrg", param.get("certOrg"));
        initMap.put("certDate", param.get("certDate"));
        initMap.put("validDate", param.get("validDate"));
        initMap.put("url", param.get("url"));
        initMap.put("comName", param.get("comName"));
        initMap.put("qualType", param.get("qualType"));
        return initMap;
    }
}
