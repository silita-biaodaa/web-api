package base;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigTest extends AbstractJUnit4SpringContextTests {

}
