package test;

import base.ConfigTest;
import com.silita.service.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by dh on 2018/7/9.
 */
@Transactional
@TransactionConfiguration(transactionManager="txManager",defaultRollback=true)
public class TestServiceLogic extends ConfigTest{

    @Autowired
    TestService testService;

    @Test
    public void test(){
        Map res = testService.getTestName("2");
        System.out.print(res);
    }
}
