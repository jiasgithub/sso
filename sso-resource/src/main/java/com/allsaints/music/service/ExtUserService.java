package com.allsaints.music.service;

import com.allsaints.music.http.Result;
import com.allsaints.music.http.form.CpoaUserForm;
import com.allsaints.music.http.form.ForgetPwdForm;

/**
 * Description:
 * <p>
 * date: 2021/9/14 17:21
 * <p>
 * Author: Mr.S
 */
public interface ExtUserService {

    Result createCpUser(CpoaUserForm form);

    Result autoGenerateUser(Long cpid);

    Result forgetPwd(ForgetPwdForm form);
}
