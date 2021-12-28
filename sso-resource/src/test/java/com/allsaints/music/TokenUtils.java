package com.allsaints.music;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {
	
	private static final String CLIENT_ID = "password";
	private static final String USERNAME = "test";
	private static final String PASSWORD = "test";
	private static final String CLIENT_SECRET = "1ef130b4-e4f8-4d30-8743-7cc09ce249d6";
	private static final String TOKEN_REQUEST_URI = "http://10.171.6.25:8080/oauth/token?grant_type=password&username=" + USERNAME + "&password=" + PASSWORD + "&scope=smsapi";
	
	private static ObjectMapper om = new ObjectMapper();
	private static HttpHeaders httpHeaders = new HttpHeaders();
	
	public static HttpHeaders obtainAccessToken() throws Exception {
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET, StandardCharsets.UTF_8);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<OAuth2AccessToken> resp = rest.postForEntity(TOKEN_REQUEST_URI, entity, OAuth2AccessToken.class);
		if (!resp.getStatusCode().equals(HttpStatus.OK)) {
			throw new RuntimeException(resp.toString());
		}
		OAuth2AccessToken t = resp.getBody();
		log.info("accessToken={}", om.writeValueAsString(t));
		log.info("the response, "
				+ "access_token: " + t.getValue() + "; "
				+ "token_type: " + t.getTokenType() + "; "
				+ "refresh_token: " + t.getRefreshToken() + "; "
				+ "expiration: " + t.getExpiresIn() + ", "
				+ "expired when:" + t.getExpiration());
		
		httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(t.getValue());
		
		return httpHeaders;
	}
	
}
