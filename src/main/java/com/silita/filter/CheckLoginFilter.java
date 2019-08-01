package com.silita.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by zhangxiahui on 17/7/26.
 */
public class CheckLoginFilter implements Filter {

    private static Logger LOGGER = LoggerFactory.getLogger(CheckLoginFilter.class);

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
        LOGGER.info("--------------------------request_uri:" + request.getRequestURI() + "--------------------------");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
