package base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(locations = {"classpath:config/spring/springmvc-servlet.xml","classpath:config/spring/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class WebConfigTest extends AbstractJUnit4SpringContextTests {
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象 .addFilter(new CheckLoginFilter(),"/*")
    }



}
