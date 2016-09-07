package com.zx.stlife.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by micheal on 15/12/19.
 */
public class RequestUtils {

    private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static String getHeader(HttpServletRequest request, String headName) {
        String value = request.getHeader(headName);
        return !StringUtils.isBlank(value)
                && !"unknown".equalsIgnoreCase(value)
                && null != value ? value : getIpAddr(request);
    }

    public static String getRealIp(HttpServletRequest request){
        return getHeader(request, "X-Real-IP");
    }

    /**
     * 获取登录用户IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static Map<String, String> collectParams(HttpServletRequest request) {
        Map<String, String> params = new TreeMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        return params;
        /*Enumeration<String> paramNames = request.getParameterNames();
        SortedMap<String, String> parmasMap = new TreeMap<>();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            parmasMap.put(paramName, request.getParameter(paramName));
        }

        return parmasMap;*/
    }

    public static Map<String, String> collectXmlParamsToMap(HttpServletRequest request){
        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null;
        String result = null;
        try {
            inStream = request.getInputStream();
            outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                outSteam.close();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }

            try {
                inStream.close();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        if( result != null) {
            try {
                Map<String, String> map = XMLUtils.doXMLParse(result);
                return  map;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return null;
    }
}
