package com.base.modules.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 组合URL工具类.
 *
 * Created by cyh on 2016/1/31.
 */
public class UrlUtils {

    private static final String separator = "/";

    /**
     * 组合URL
     *
     * @param urlPrefix URL前缀
     * @param fileName  文件名
     * @param paths     路径集合
     * @return
     */
    public static String joinUrl(String urlPrefix, String fileName, String... paths) {
        String urlStr = joinUrlPrefixAndPaths(urlPrefix, paths);
        StringBuilder buffer = new StringBuilder(urlStr);
        buffer.append(UrlUtils.separator).append(fileName);

        return buffer.toString();
    }

    /**
     * 组合URL
     *
     * @param urlPrefix     URL前缀
     * @param fileNameArray 文件名数组
     * @param paths         路径集合
     * @return
     */
    public static List<String> joinUrl(String urlPrefix, String[] fileNameArray, String... paths) {
        List<String> urls = new ArrayList<String>();

        String urlStr = joinUrlPrefixAndPaths(urlPrefix, paths);
        for (String fileName : fileNameArray) {
            StringBuilder buffer = new StringBuilder(urlStr);
            buffer.append(UrlUtils.separator).append(fileName);
            urls.add(buffer.toString());
        }

        return urls;
    }

    /**
     * 组合URL
     *
     * @param urlPrefix    URL前缀
     * @param fileNameList 文件名集合
     * @param paths        路径集合
     * @return
     */
    public static List<String> joinUrl(String urlPrefix, Collection<String> fileNameList, String... paths) {
        List<String> urls = new ArrayList<String>();

        String urlStr = joinUrlPrefixAndPaths(urlPrefix, paths);
        for (String fileName : fileNameList) {
            StringBuilder buffer = new StringBuilder(urlStr);
            buffer.append(UrlUtils.separator).append(fileName);
            urls.add(buffer.toString());
        }

        return urls;
    }

    /**
     * 组合URL前缀和路径
     *
     * @param urlPrefix URL前缀
     * @param paths     路径集合
     * @return
     */
    private static String joinUrlPrefixAndPaths(String urlPrefix, String... paths) {
        if (StringUtils.isBlank(urlPrefix)) {
            return null;
        }

        if (urlPrefix.endsWith(UrlUtils.separator)) {
            urlPrefix = urlPrefix.substring(0, urlPrefix.length() - 1);
        }

        StringBuilder buffer = new StringBuilder(urlPrefix);
        for (String path : paths) {
            buffer.append(UrlUtils.separator).append(path);
        }

        return buffer.toString();
    }

}
