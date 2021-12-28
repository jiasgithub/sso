package com.allsaints.music.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <p>
 * date: 2021/9/27 18:34
 * <p>
 * Author: Mr.S
 */
@Slf4j
@Component("customAccessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        Map map = new HashMap();
        map.put("error", "400");
        map.put("message", e.getMessage());
        map.put("path", httpServletRequest.getServletPath());
        map.put("timestamp", String.valueOf(new Date().getTime()));
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().write(mapper.writeValueAsString(map));
    }
}
