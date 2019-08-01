package test;

import base.ConfigTest;
import com.silita.common.Constant;
import com.silita.dao.TbApiDetailMapper;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by zhushuai on 2019/8/1.
 */
public class RemoteApiTest extends ConfigTest {

    @Autowired
    TbApiDetailMapper tbApiDetailMapper;

    /**
     * 添加调用者信息
     */
    @Test
    public void addApiInfo() {
        Map<String, Object> parma = new HashedMap() {{
            put("appId", "A1001");
            put("requestUrl", Constant.REQUEST_URL_LIST_ZHONGBIAO);
            put("callTotal", 10);
            put("callCount", 0);
            put("residueCount", 0);
            put("price",0);
        }};
        tbApiDetailMapper.insert(parma);
        parma = new HashedMap() {{
            put("appId", "A1001");
            put("requestUrl", Constant.REQUEST_URL_DETAIL_ZHONGBIAO);
            put("callTotal", 10);
            put("callCount", 0);
            put("residueCount", 0);
            put("price",0);
        }};
        tbApiDetailMapper.insert(parma);
        parma = new HashedMap() {{
            put("appId", "A1001");
            put("requestUrl", Constant.REQUEST_URL_DETAIL_ZHAOBIAO);
            put("callTotal", 10);
            put("callCount", 0);
            put("residueCount", 0);
            put("price",0);
        }};
        tbApiDetailMapper.insert(parma);
        parma = new HashedMap() {{
            put("appId", "A1001");
            put("requestUrl", Constant.REQUEST_URL_LIST_ZHAOBIAO);
            put("callTotal", 10);
            put("callCount", 0);
            put("residueCount", 0);
            put("price",0);
        }};
        tbApiDetailMapper.insert(parma);
    }
}
