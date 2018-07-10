package com.silita.dao;

import java.util.List;
import java.util.Map;

public interface TbCompanyMapper {

    /**
     * 获取人员信息
     * @param param
     * @return
     */
    List<Map<String,Object>> queryPersonList(Map<String,Object> param);

    /**
     * 查询公司信息
     * @param param
     * @return
     */
    Map<String,Object> queryCompanyInfo(Map<String,Object> param);

    /**
     * 获取公司code
     * @param provice
     * @return
     */
    String queryProvinceCode(String provice);

    /**
     * 查询资质
     * @param param
     * @return
     */
    List<Map<String,Object>> queryCompanyQual(Map<String,Object> param);

}
