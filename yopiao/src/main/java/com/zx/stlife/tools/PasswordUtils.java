package com.zx.stlife.tools;

import org.apache.commons.lang3.StringUtils;

import com.base.modules.util.SimpleUtils;

public class PasswordUtils {

    /**
     * 验证密码是否符合规则
     * 规则：
     * 1. 长度为8-16
     * 2. 可以包含数字、字母和符合，至少含有两者以上
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password){
        if(StringUtils.isBlank(password) ||
                password.length() > 16 || password.length() < 8 ){
            return false;
        }

        boolean b1 = SimpleUtils.regex(password, "^.*[\\d]+.*$");
        boolean b2 = SimpleUtils.regex(password, "^.*[A-Za-z]+.*$");
        boolean b3 = SimpleUtils.regex(password, "^.*[?!_@#%&^+-/*\\/\\\\]+.*$");
        if( (b1 && b2) ||
                (b1 && b3) ||
                (b2 && b3)){
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(isValidPassword("1234567?"));
        System.out.println(isValidPassword("1234567!"));
        System.out.println(isValidPassword("1234567."));
        System.out.println(isValidPassword("1234567@"));
        System.out.println(isValidPassword("123?4560"));
    }
}
