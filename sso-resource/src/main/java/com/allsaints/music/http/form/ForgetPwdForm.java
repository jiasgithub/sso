package com.allsaints.music.http.form;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * <p>
 * date: 2021/9/17 11:42
 * <p>
 * Author: Mr.S
 */
@Data
public class ForgetPwdForm implements Serializable {
    private String username;
    private String password;
    private String email;
    private String code;
}
