package com.allsaints.music.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.alicp.jetcache.anno.CacheType;
import com.allsaints.music.extentity.TblCpoaUser;
import com.allsaints.music.extentity.TblCppartner;
import com.allsaints.music.extentity.repository.TblCpoaUserRepository;
import com.allsaints.music.extentity.repository.TblCppartnerRepository;
import com.allsaints.music.extra.MailTemplateService;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;
import com.allsaints.music.http.form.CpoaUserForm;
import com.allsaints.music.http.form.ForgetPwdForm;
import com.allsaints.music.service.ExtUserService;
import com.allsaints.music.utils.CodeUtils;
import com.allsaints.music.utils.Md5Utils;
import com.allsaints.music.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * <p>
 * date: 2021/9/14 17:22
 * <p>
 * Author: Mr.S
 */
public class ExtUserServiceImpl implements ExtUserService {

    @Autowired
    private TblCpoaUserRepository cpoaUserRepository;

    @Autowired
    private TblCppartnerRepository cppartnerRepository;

    @Autowired
    private MailTemplateService mailTemplateService;

    @CreateCache(name = "code", cacheType = CacheType.BOTH, localLimit = 50, expire = 600)
    private Cache<String, String> codeCatch;

    @Override
    public Result createCpUser(CpoaUserForm form) {
        String code = form.getCode();
        String mailCode = codeCatch.get(form.getEmail());
        if (!StringUtils.isEmpty(code) && !code.equals(mailCode)) {
            return new Result(ResultCodeTemplate.BAD_REQUEST, "验证码错误或已超时");
        }

        TblCpoaUser user = new TblCpoaUser();
        user.setUsername(form.getUsername());
        List<TblCpoaUser> all = cpoaUserRepository.findAll(Example.of(user));
        if (!ObjectUtils.isEmpty(all)) {
            return new Result(ResultCodeTemplate.BAD_REQUEST, "用户名重复!");
        }
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(form.getPassword()));
        user.setPhone(form.getPhone());
        user.setRegisterTime(new Date());
        user.setLocked(false);
        user.setEmail(form.getEmail());
        TblCpoaUser save = cpoaUserRepository.save(user);
        return new Result(ResultCodeTemplate.SUCCESS, save.getId());
    }

    @Override
    public Result autoGenerateUser(Long cpid) {
        TblCppartner cppartner = cppartnerRepository.getOne(cpid);
        TblCpoaUser cpoaUser = cpoaUserRepository.findByUsername(cppartner.getAliasName());
        String username = null;
        if (ObjectUtils.isEmpty(cpoaUser)) {
            username = cppartner.getAliasName();
        } else {
            username = cppartner.getAliasName() + CodeUtils.generateCode();
        }
        if (!ObjectUtils.isEmpty(cppartner)) {
            String password = new PasswordGenerator(8, 4).generateRandomPassword();
            String md5Pwd = Md5Utils.md5(username + password);
            TblCpoaUser user = new TblCpoaUser();
            user.setCppartnerId(cpid);
            List<TblCpoaUser> all = cpoaUserRepository.findAll(Example.of(user));
            if (!ObjectUtils.isEmpty(all)) {
                return new Result(ResultCodeTemplate.BAD_REQUEST, "用户已存在,请勿重复创建!");
            }
            user.setLocked(false);
            user.setRegisterTime(new Date());
            user.setPhone(cppartner.getContactPhone());
            user.setUsername(username);
            user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(md5Pwd));
            user.setCppartnerId(cpid);
            user.setEmail(cppartner.getContactEmail());
            cpoaUserRepository.save(user);

            HashMap<String, Object> var = new HashMap<>();
            var.put("username", username);
            var.put("password", password);
            mailTemplateService.send(cppartner.getContactEmail(), "02", var);

            return new Result(ResultCodeTemplate.SUCCESS, "请注意查收邮件!");
        }
        return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND, "CP不存在!");
    }

    @Override
    public Result forgetPwd(ForgetPwdForm form) {
        TblCpoaUser user = new TblCpoaUser();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        List<TblCpoaUser> users = cpoaUserRepository.findAll(Example.of(user));
        if (!ObjectUtils.isEmpty(users)) {
            String mailCode = codeCatch.get(form.getEmail());
            String code = form.getCode();
            if (!StringUtils.isEmpty(mailCode) && !mailCode.equals(code)) {
                return new Result(ResultCodeTemplate.BAD_REQUEST, "验证码错误或已失效");
            }

            TblCpoaUser cpoaUser = users.get(0);
            cpoaUser.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(form.getPassword()));
            cpoaUserRepository.save(cpoaUser);
            return new Result(ResultCodeTemplate.SUCCESS);
        }
        return new Result(ResultCodeTemplate.BAD_REQUEST, "用户不存在,请核对信息后重试");
    }
}
