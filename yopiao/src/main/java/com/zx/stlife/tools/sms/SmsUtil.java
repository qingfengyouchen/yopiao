package com.zx.stlife.tools.sms;

import java.util.List;

import com.zx.stlife.constant.Const;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.modules.util.PropertiesLoader;
import com.base.modules.util.SimpleUtils;
import com.bcloud.msg.http.HttpSender;

public class SmsUtil {
    protected static Logger logger = LoggerFactory.getLogger(SmsUtil.class);
    
    /**发短信地址*/
    private final static String URL = Const.appPropertiesLoader.getProperty("sms.url");
    /**发短信账号*/
    private final static String ACCOUNT = Const.appPropertiesLoader.getProperty("sms.account");
    /**发短信密码*/
    private final static String PASSWORD = Const.appPropertiesLoader.getProperty("sms.password");

    /**
     * 单发短信
     * @param mobileNo
     * @param content
     * @param needstatus 是否需要状态报告，需要true，不需要false
     * @param product
     * @param extno
     * @return
     * @throws Exception
     */
    public static String send(String mobileNo, String content,
                              boolean needstatus, String product, String extno) {
        if(StringUtils.isBlank(mobileNo) &&
                StringUtils.isBlank(content) ){
            return "-1";
        }
        
        try {
			return HttpSender.batchSend(URL, ACCOUNT, PASSWORD, mobileNo, content, needstatus, product, extno);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        
        return "-2";
    }

    /**
     * 群发短信
     * @param content 短信内容
     * @param needstatus 是否需要状态报告，需要true，不需要false
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */
    public static String batchSend(List<String> mobileNoList, String content,
                                   boolean needstatus, String product, String extno) {
        if(SimpleUtils.isNullList(mobileNoList) &&
                StringUtils.isBlank(content) ){
            return "-1";
        }
        
        try {
	        String mobiles = SimpleUtils.listToString(mobileNoList, ",", null);
	        return HttpSender.batchSend(URL, ACCOUNT, PASSWORD, mobiles, content, needstatus, product, extno);
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        
        return "-2";
    }
    
}
