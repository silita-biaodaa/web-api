package com.silita.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.silita.controller.vo.Page;
import com.silita.dao.TbNtMianMapper;
import com.silita.utils.CommonUtil;
import com.silita.utils.HBaseUtils;
import com.silita.utils.PropertiesUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 公告service
 * Created by zhushuai on 2019/7/31.
 */
@Service
public class NoticeService {

    @Autowired
    TbNtMianMapper tbNtMianMapper;
    @Autowired
    ApiDetailService apiDetailService;

    /**
     * 公告列表
     *
     * @param param
     * @return
     */
    public Map<String, Object> listNotice(Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        //调用次数限制
        if (!apiDetailService.chechApiRemote(param, resultMap)) {
            return resultMap;
        }
        Integer pageNo = MapUtils.getInteger(param, "pageNo") == null ? 1 : MapUtils.getInteger(param, "pageNo");
        Integer pageSize = MapUtils.getInteger(param, "pageSize") == null ? 20 : MapUtils.getInteger(param, "pageSize");
        //解析地址
        analysisRegions(param);
        List<Map<String, Object>> list = new ArrayList<>();
        if ("zhongbiao".equals(param.get("type"))) {
            Page page = new Page();
            page.setCurrentPage(pageNo);
            page.setPageSize(pageSize);
            PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
            list = tbNtMianMapper.queryZhongbiaoList(param);
        } else {
            //取消第三方插件分页
            param.put("pdModeType", MapUtils.getString(param, "source") + "_pbmode");
            return pageZhaobiaoList(pageNo, pageSize, param, resultMap);
        }
        PageInfo pageInfo = new PageInfo(list);
        resultMap.put("data", pageInfo.getList());
        resultMap.put("pageNo", pageInfo.getPageNum());
        resultMap.put("pageSize", pageInfo.getPageSize());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pages", pageInfo.getPages());
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        return resultMap;
    }

    /**
     * 自定义分页(zhaobiao)
     *
     * @param pageNo
     * @param pageSize
     * @param param
     * @param resultMap
     * @return
     */
    private Map<String, Object> pageZhaobiaoList(Integer pageNo, Integer pageSize, Map<String, Object> param, Map<String, Object> resultMap) {
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        Integer total = tbNtMianMapper.queryZhaobiaoTotal(param);
        if (total <= 0) {
            resultMap.put("data", new ArrayList<>());
            resultMap.put("pageNo", pageNo);
            resultMap.put("pageSize", pageSize);
            resultMap.put("total", 0);
            resultMap.put("pages", 0);
            return resultMap;
        }
        Integer pages = CommonUtil.getPages(total, pageSize);
        param.put("start", (pageNo - 1) * pageSize);
        resultMap.put("data", tbNtMianMapper.queryZhaobiaoList(param));
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        resultMap.put("total", total);
        resultMap.put("pages", pages);
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        return resultMap;
    }

    /**
     * 公告详情
     */
    public Map<String, Object> detailNoitce(Map<String, Object> param) {
        //调用次数限制
        Map<String, Object> resultMap = new HashMap<>();
        if (!apiDetailService.chechApiRemote(param, resultMap)) {
            return resultMap;
        }
        //查询爬取id
        Map<String, Object> noticeParam = tbNtMianMapper.queryMianParam(param);
        param.put("province", noticeParam.get("proviceCn"));
        param.put("city", noticeParam.get("cityCn"));
        Map<String, Object> result = null;
        if ("zhongbiao".equals(param.get("type"))) {
            result = tbNtMianMapper.queryZhongbiaoDetail(param);
        } else {
            result = tbNtMianMapper.queryZhaobiaoDetail(param);
        }
        //查询内容
        if (MapUtils.isNotEmpty(result)) {
            result.put("content", getContent(MapUtils.getString(noticeParam,"snatchId")));
        }
        resultMap.put("code", 1);
        resultMap.put("msg", "操作成功!");
        resultMap.put("data", result);
        return resultMap;
    }

    /**
     * 地址解析
     *
     * @param param
     */
    private void analysisRegions(Map<String, Object> param) {
        String regions = MapUtils.getString(param, "regions");
        if (StringUtils.isEmpty(regions)) {
            param.put("regions", "hunan");
            return;
        }
        String[] region = regions.split("\\|\\|");
        param.put("regions", region[0]);
        if (region.length > 1) {
            param.put("citys", Arrays.asList(region[1].split(".")));
            return;
        }
    }

    private String getContent(String snatchId) {
        String ip = PropertiesUtils.getProperty("Hbase.ip");
        String port = PropertiesUtils.getProperty("Hbase.port");
        String master = PropertiesUtils.getProperty("Hbase.master");
        String hdfs = PropertiesUtils.getProperty("Hbase.hdfs");
        Connection connection = null;
        String content = "";
        try {
            connection = HBaseUtils.init(ip, port, master, hdfs);
            Table table = connection.getTable(TableName.valueOf("notice"));
            Get get = new Get(snatchId.getBytes());
            Result result = table.get(get);
            if (null != result.rawCells()) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    switch (key) {
                        case "content": //获取内容
                            content = value;
                            break;
                    }
                }
            }
        } catch (IOException e) {
            return content;
        } finally {
            HBaseUtils.close(connection);
        }
        return content;
    }
}
