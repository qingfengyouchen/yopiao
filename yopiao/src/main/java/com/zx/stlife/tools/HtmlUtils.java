package com.zx.stlife.tools;

import com.base.modules.util.SimpleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by micheal on 15/10/1.
 */
public class HtmlUtils {

    private static Logger logger = LoggerFactory.getLogger(HtmlUtils.class);

    /**
     * 去除如下样式
     * 1. float:
     * 2. width:
     * @param html
     * @return
     */
    public static String doHtmlContent(String html){
        if(StringUtils.isBlank(html))
            return null;

        html = html.replaceAll("float\\s*:\\s*\\S+;","");
        Pattern pattern = Pattern.compile("(width\\s*:\\s*)(\\d+)(\\s*\\S*;)");
        Matcher matcher = pattern.matcher(html);
        while(matcher.find()){
            try {
                String target = matcher.group(0);
                int width = SimpleUtils.stringToInteger(matcher.group(2));
                if(width > 700){
                    html = html.replaceAll(target,"");
                }
            }catch (Exception ex){
                logger.error(ex.getMessage(), ex);
            }
        }

        return html;
    }
}
