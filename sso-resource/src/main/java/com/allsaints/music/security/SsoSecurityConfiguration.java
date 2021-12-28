package com.allsaints.music.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableOAuth2Sso
public class SsoSecurityConfiguration extends WebSecurityConfigurerAdapter {

}
