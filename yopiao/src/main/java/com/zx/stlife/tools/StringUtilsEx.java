package com.zx.stlife.tools;

import com.base.modules.util.SimpleUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micheal on 15/12/17.
 */
public class StringUtilsEx extends StringUtils {

    public static  <T> List<T> stringToBaseDataTypeList(String str, Class toClass){
        if(isBlank(str)){
            return null;
        }

        String[] strArr = str.split(",");
        List<T> list = new ArrayList<>(strArr.length);
        for(String e: strArr){
            if(Integer.class == toClass){
                list.add((T)SimpleUtils.stringToInteger(e));
            }else if(Byte.class == toClass){
                list.add((T)SimpleUtils.stringToByte(e));
            }else if(Long.class == toClass){
                list.add((T)SimpleUtils.stringToLong(e));
            }else{
                list.add((T)e);
            }
        }

        return list;
    }

    public static String transferString(String str){
        if(isBlank(str))
            return null;

        byte[] strBytes = str.getBytes();
        for (int i = 0; i < strBytes.length; i++) {//对UTF-8编码格式中的4字节编码（UTF-8编码规范）进行处理，替换为XXXX
            if ((strBytes[i] & 0xF8) == 0xF0) {
                for (int j = 0; j < 4; j++) {
                    strBytes[i + j] = 88;
                }
                i += 3;
            }
        }
        return new String(strBytes);
    }

    public static String limitLength(String str, int len){
        if(isBlank(str))
            return null;

        if(str.length() > len){
            if(len > 3) {
                str = str.substring(0, len - 3) + "...";
            }else{
                str = str.substring(0, len);
            }
        }

        return str;
    }

    public static String getLastStr(String str, int lastLen){
        if(isBlank(str))
            return null;

        int len = str.length();
        if(len > lastLen){
            str = str.substring(len - lastLen, len);
        }

        return str;
    }

}
