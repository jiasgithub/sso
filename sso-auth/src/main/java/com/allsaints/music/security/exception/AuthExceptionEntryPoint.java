package com.allsaints.music.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:自定义Token异常信息
 * <p>
 * date: 2021/9/27 18:29
 * <p>
 * Author: Mr.S
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Map map = new HashMap();
        map.put("error", "401");
        map.put("message", e.getMessage());
        map.put("path", httpServletRequest.getServletPath());
        map.put("timestamp", String.valueOf(new Date().getTime()));
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            mapper.writeValue(httpServletResponse.getOutputStream(), map);
        } catch (Exception e2) {
            throw new ServletException();
        }
    }
}
