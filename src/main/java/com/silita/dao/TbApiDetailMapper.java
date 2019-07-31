package com.silita.dao;

import java.util.Map;

/**
 * tb_api_detail Mapper
 * Created by zhushuai on 2019/7/31.
 */
public interface TbApiDetailMapper {

    /**
     * 添加
     * @param param
     * @return
     */
    int insert(Map<String,Object> param);

    /**
     * 修改
     * @param param
     * @return
     */
    int update(Map<String,Object> param);

    /**
     * 查询
     * @return
     */
    Map<String,Object> queryApiDetail(Map<String,Object> param);
}
