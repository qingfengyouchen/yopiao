package com.zx.stlife.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by micheal on 15/12/25.
 */
public class LogUtils {

    private static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void logMap(String title, Map<String, String> map) {
        logger.info(title);
        int i = 0;
        StringBuffer sb = new StringBuffer("\n{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("\n\t\"")
                    .append(entry.getKey())
                    .append("\": \"")
                    .append(entry.getValue())
                    .append("\"");
            if( i < map.size() - 1){
                sb.append(",");
            }
            i ++;
        }
        sb.append("\n}");
        logger.info(sb.toString());
    }
}
