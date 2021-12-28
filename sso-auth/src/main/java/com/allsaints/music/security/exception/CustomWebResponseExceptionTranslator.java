package com.allsaints.music.security.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <p>
 * date: 2021/9/27 17:52
 * <p>
 * Author: Mr.S
 */
@Component("customWebResponseExceptionTranslator")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
        return ResponseEntity
                .status(oAuth2Exception.getHttpErrorCode())
                .body(new CustomOauthException(oAuth2Exception.getMessage()));
    }
}
