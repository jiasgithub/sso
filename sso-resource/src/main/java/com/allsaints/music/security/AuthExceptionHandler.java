package com.allsaints.music.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthExceptionHandler extends OAuth2AccessDeniedHandler implements AuthenticationEntryPoint, AuthenticationFailureHandler {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
    private Environment env;
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException {
    	this.authException(request, response, authException);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    	this.authException(request, response, authException);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.authException(request, response, authException);
    }
    
    private void authException (HttpServletRequest request, HttpServletResponse response, Exception authException) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        PrintWriter printWriter = response.getWriter();
        if (log.isWarnEnabled()) {
        	log.warn("Handling error: " + authException.getClass().getSimpleName() + ", " + authException.getMessage());
		}
        printWriter.append(objectMapper.writeValueAsString(new Result(ResultCodeTemplate.NOT_AUTH, env.getProperty("spring.application.name") + " no auth")));
    }
    
}
