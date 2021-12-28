package com.allsaints.music.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private AuthExceptionHandler authExceptionHandler;
	
	@Override
    public void configure(final HttpSecurity http) throws Exception {
		RequestMatcher nonLogin = new NegatedRequestMatcher(new AntPathRequestMatcher("/**/login"));
		RequestMatcher nonLogout = new NegatedRequestMatcher(new AntPathRequestMatcher("/**/logout"));
        http
            .requestMatcher(new AndRequestMatcher(nonLogin, nonLogout)).authorizeRequests()
            .anyRequest().authenticated();
    }
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.accessDeniedHandler(authExceptionHandler).authenticationEntryPoint(authExceptionHandler);
	}
	
}
