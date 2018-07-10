package com.silita.filter;

import com.alibaba.fastjson.JSONObject;
import com.silita.utils.PropertiesUtils;
import com.silita.utils.SignConvertUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/26.
 */
public class SecurityCheck {

    private static final Logger logger = Logger.getLogger(SecurityCheck.class);

    public static String getCookieValue(HttpServletRequest request, String name) {
        String value = null;
        if (request != null && request.getCookies() != null) {
            for (Cookie ck : request.getCookies()) {
                if (ck.getName().contains(name)) {
                    value = ck.getValue();
                }
            }
        }
        return value;
    }

    public static String getHeaderValue(HttpServletRequest request, String name) {
        String value = null;
        if (request != null) {
            value = request.getHeader(name);
        }
        return value;
    }

    public static boolean checkSigner(Map<String, String> parameters, String contentSign) {
        boolean alongBoo = false;
        String sign = "";
        String secret = PropertiesUtils.getProperty("CONTENT_SECRET");
        if(parameters != null && contentSign != null) {
            try {
                sign = SignConvertUtils.generateMD5Sign(secret, parameters);
            } catch(NoSuchAlgorithmException e) {
                logger.error(e.getMessage(), e);
            } catch(UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
            if(contentSign.equals(sign)) {
                //鉴权成功
                alongBoo = true;
            }
        }
        return alongBoo;
    }


    public static boolean checkUserSigner(String xToken) {
        boolean alongBoo = false;
        String sign = null;
        String clientId = null;
        String timestamp = null;
        Map<String, String> parameters = new HashedMap();
        if (xToken != null) {
            String[] token = xToken.split("\\.");
            if (token.length == 2) {
                sign = token[0];
                String json = null;
                try {
                    json = new String(Base64.getDecoder().decode(token[1]), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
                clientId = jsonObject.getString("clientId");
                timestamp = jsonObject.getString("timestamp");
                parameters.put("clientId", clientId);
                parameters.put("timestamp", timestamp);
                alongBoo = SecurityCheck.checkSigner(parameters, sign);
            }
        }
        return alongBoo;
    }
}
