package com.allsaints.music.web;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.allsaints.music.extra.MailTemplateService;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;
import com.allsaints.music.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:发送邮箱验证码
 * <p>
 * date: 2021/9/26 19:08
 * <p>
 * Author: Mr.S
 */
@RestController
@RequestMapping("/code")
public class CodeController {

    @Autowired
    private MailTemplateService mailTemplateService;

    @CreateCache(name = "code", cacheType = CacheType.BOTH, localLimit = 50, expire = 600)
    private Cache<String, String> codeCatch;

    @PostMapping("/sendcode")
    public Result sendMailCode(String mail) {
        String generateCode = CodeUtils.generateCode();
        Map<String, Object> msg = new HashMap<>();
        codeCatch.put(mail, generateCode);
        msg.put("checkCode", generateCode);
        mailTemplateService.send(mail, "01", msg);
        return new Result(ResultCodeTemplate.SUCCESS, "请在邮箱查收验证码");
    }
}
