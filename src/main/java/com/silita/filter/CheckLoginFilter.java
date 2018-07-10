package com.silita.filter;

import com.alibaba.fastjson.JSONObject;
import com.silita.utils.PropertiesUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/26.
 */
public class CheckLoginFilter implements Filter {

    private static Logger LOGGER = LoggerFactory.getLogger(CheckLoginFilter.class);

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String xToken = null;
        xToken = SecurityCheck.getHeaderValue(request, "X-TOKEN");
        String sign = null;
        String timestamp = null;
        String clientId = null;
        Map<String, String> parameters = new HashedMap();
        if (xToken != null) {
            String[] token = xToken.split("\\.");
            if (token.length == 2) {
                sign = token[0];
                String json = null;
                try {
                    json = new String(Base64.getDecoder().decode(token[1]), "utf-8");
                    JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
                    clientId = jsonObject.getString("clientId");
                    timestamp = jsonObject.getString("timestamp");
                    parameters.put("clientId", clientId);
//                    parameters.put("timestamp", timestamp);
                } catch (Exception e) {
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json; charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.print("{\"code\":0,\"msg\":\"没权限\"}");
                    return;
                }
            }
        }

        //TODO: 判断签名
        if (SecurityCheck.checkSigner(parameters, sign)) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            //TODO: 是否金蝶调用
            if (!clientId.equals(PropertiesUtils.getProperty("CLIENT_ID"))) {
                PrintWriter out = response.getWriter();
                out.print("{\"code\":0,\"msg\":\"没权限\"}");
                return;
            }
            //TODO: 是否超时
            Long systimestamp = System.currentTimeMillis();
            Long patimestamp = Long.valueOf(timestamp);
            Long sutimetamp = systimestamp - patimestamp;
            if ((sutimetamp / (1000 * 60)) > 5) {
                PrintWriter out = response.getWriter();
                out.print("{\"code\":0,\"msg\":\"X-TOKEN超时,请重新获取\"}");
                return;
            }
            LOGGER.debug("进入系统Filter,开始API调用");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print("{\"code\":0,\"msg\":\"没权限\"}");
        }
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
