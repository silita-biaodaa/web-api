package com.silita.dao;

import java.util.List;
import java.util.Map;

/**
 * 公告mapper
 * Created by zhushuai on 2019/7/31.
 */
public interface TbNtMianMapper {

    /**
     * 查询中标列表
     * @param param
     * @return
     */
    List<Map<String,Object>> queryZhongbiaoList(Map<String,Object> param);

    /**
     * 查询招标总数
     * @return
     */
    int queryZhaobiaoTotal(Map<String,Object> param);

    /**
     * 查询招标列表
     * @param param
     * @return
     */
    List<Map<String,Object>> queryZhaobiaoList(Map<String,Object> param);

    /**
     * 查询公告主表相关字段
     * @return
     */
    Map<String,Object> queryMianParam(Map<String,Object> param);

    /**
     * 查询招标公告详情
     * @param param
     * @return
     */
    Map<String,Object> queryZhaobiaoDetail(Map<String,Object> param);

    /**
     * 查询中标公告详情
     * @param param
     * @return
     */
    Map<String,Object> queryZhongbiaoDetail(Map<String,Object> param);
}
