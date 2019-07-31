package test;

import base.ConfigTest;
import com.silita.common.RedisUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dh on 2018/7/5.
 */

public class TestRedis extends ConfigTest{
    @Autowired
    public RedisUtils redisUtils;

    @Test
    public void test(){
        redisUtils.setString("caoliang","is pig",100);
    }

}
