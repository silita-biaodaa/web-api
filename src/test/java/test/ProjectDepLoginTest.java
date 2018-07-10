package test;/**
 * Created by zhangxiahui on 16/6/21.
 */

import com.alibaba.fastjson.JSONObject;
import com.silita.utils.PropertiesUtils;
import com.silita.utils.SignConvertUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 给项目部demo
 *
 * @author zhangxiahui
 * @version 1.0
 * @date 2016/06/21 下午3:49
 */
public class ProjectDepLoginTest {
    public static void main(String[] args) {
        String tenantId = "jindie";
        String secret = "9dF74HlkgsAckjQfLA9OXSc46#04*13tEsT";
        String sign = "";
        String xtoken = "";
        try {
            //注：Map需要有序
            Map<String, String> parameters = new TreeMap<>();
            //TODO: 生成签名
            parameters.put("clientId", tenantId);
            parameters.put("timestamp", Long.valueOf(System.currentTimeMillis()).toString());
            sign = ProjectDepLoginTest.generateMD5Sign(secret, parameters);
            //TODO: 生成Token
            String parameterJson = JSONObject.toJSONString(parameters);
            String asB64 = Base64.getEncoder().encodeToString(parameterJson.getBytes("utf-8"));
            xtoken = sign + "." + asB64;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(xtoken);

        //登录内容管理时必须有以下两个cookie
        //Cookie tenantId = new Cookie("tenantId", tenantId);
        //Cookie contentSign = new Cookie("contentSign", sign);
        //E796920E49A80AF9A57BB412ABC10CA0
        //FE62366E7903B19B880ECCE1B7356357
    }

    public static String generateMD5Sign(String secret, Map<String, String> parameters) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(generateConcatSign(secret, parameters).getBytes("utf-8"));
        return byteToHex(bytes);
    }

    private static String generateConcatSign(String secret, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder().append(secret);
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            sb.append(key).append(parameters.get(key));
        }
        return sb.append(secret).toString();
    }

    private static String byteToHex(byte[] bytesIn) {
        StringBuilder sb = new StringBuilder();
        for (byte byteIn : bytesIn) {
            String bt = Integer.toHexString(byteIn & 0xff);
            if (bt.length() == 1)
                sb.append(0).append(bt);
            else
                sb.append(bt);
        }
        return sb.toString().toUpperCase();
    }
}
