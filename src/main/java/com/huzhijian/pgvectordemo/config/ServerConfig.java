package com.huzhijian.pgvectordemo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/27
 * 说明:
 */
@Component
public class ServerConfig {

    public String getUrl(){
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request= attributes.getRequest();
        return getDomain(request);
    }
    public static String getDomain(HttpServletRequest request){
//        request.getRequestURL();
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length()-request.getRequestURI().length(),url.length()).append(contextPath).toString();
    }
}
