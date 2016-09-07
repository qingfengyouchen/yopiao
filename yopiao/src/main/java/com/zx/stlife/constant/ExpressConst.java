package com.zx.stlife.constant;

import com.base.modules.util.PropertiesLoader;

import java.util.*;

/**
 * 快递常量
 */
public class ExpressConst {

    public static PropertiesLoader propertiesLoader = new PropertiesLoader("express.properties");
    /*public static Map<String, String> expressMap = new TreeMap<>();

    static {
        Properties properties = propertiesLoader.getProperties();
        for(String key : properties.stringPropertyNames()){
            expressMap.put(key, propertiesLoader.getPropertyWithChinese(key));
        }
    }*/

    public static List<String> expressList = new ArrayList<>();
    static {
        Properties properties = propertiesLoader.getProperties();
        for(String key : properties.stringPropertyNames()){
            expressList.add(propertiesLoader.getPropertyWithChinese(key));
        }
    }
}
