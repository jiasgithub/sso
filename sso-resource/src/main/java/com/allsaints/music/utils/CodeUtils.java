package com.allsaints.music.utils;

import java.util.Random;

/**
 * Description:生成四位验证码
 * <p>
 * date: 2021/9/26 19:41
 * <p>
 * Author: Mr.S
 */
public class CodeUtils {

    private static final String LIBRARY = "ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm0123456789";

    public static String generateCode() {
        StringBuilder buffer = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            char ch = LIBRARY.charAt(new Random().nextInt(LIBRARY.length()));
            buffer.append(ch);
        }
        String findkey = buffer.toString().toLowerCase();
        return findkey;
    }
}
