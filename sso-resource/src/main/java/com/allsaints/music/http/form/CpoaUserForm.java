package com.allsaints.music.http.form;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * <p>
 * date: 2021/9/14 17:23
 * <p>
 * Author: Mr.S
 */
@Data
public class CpoaUserForm implements Serializable {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String code;
}
