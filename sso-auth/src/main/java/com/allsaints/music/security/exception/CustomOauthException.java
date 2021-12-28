package com.allsaints.music.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * Description:
 * <p>
 * date: 2021/9/27 17:45
 * <p>
 * Author: Mr.S
 */
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    public CustomOauthException(String msg) {
        super(msg);
    }
}
