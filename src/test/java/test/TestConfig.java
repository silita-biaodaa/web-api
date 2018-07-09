package test;

import base.ConfigTest;
import com.silita.common.config.CustomizedPropertyConfigurer;
import org.junit.Test;

/**
 * Created by dh on 2018/7/9.
 */
public class TestConfig extends ConfigTest{

    @Test
    public void testConfig(){
        System.out.println(CustomizedPropertyConfigurer.getContextProperty("test"));
    }
}
