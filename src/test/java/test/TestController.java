package test;

import base.WebConfigTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TransactionConfiguration(transactionManager="txManager",defaultRollback=true)
public class TestController extends WebConfigTest {

    @Test
    public void testSearchList()throws Exception{
        String requestBody = "{\"pageNo\":1,\"pageSize\":20,\"type\":99}";
        String responseString = mockMvc.perform(get("/test/1").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)// contentType(MediaType.APPLICATION_FORM_URLENCODED)//ajax格式 //添加参数(可以添加多个)
                .content(requestBody.getBytes())//.param("id","3")   //添加参数(可以添加多个)
                )
                .andExpect(status().isOk())    //返回的状态是200
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }
}
