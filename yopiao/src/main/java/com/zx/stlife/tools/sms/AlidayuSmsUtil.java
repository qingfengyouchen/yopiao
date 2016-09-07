package com.zx.stlife.tools.sms;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.modules.util.SimpleUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.BizResult;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.zx.stlife.constant.Const;

public class AlidayuSmsUtil {
    protected static Logger logger = LoggerFactory
            .getLogger(AlidayuSmsUtil.class);

    /** 发短信地址 */
    private final static String URL = Const.appPropertiesLoader
            .getProperty("ali.sms.url");
    /** 发短信账号 */
    private final static String ACCOUNT = Const.appPropertiesLoader
            .getProperty("ali.sms.account");
    /** 发短信密码 */
    private final static String PASSWORD = Const.appPropertiesLoader
            .getProperty("ali.sms.password");
    /** App Key */
    private final static String APPKEY = Const.appPropertiesLoader
            .getProperty("ali.sms.appkey");
    /** App Secret */
    private final static String APPSECRET = Const.appPropertiesLoader
            .getProperty("ali.sms.appsecret");
    /** 短信签名 */
    private final static String SIGNNAME = Const.appPropertiesLoader
            .getPropertyWithChinese("ali.sms.signname");

    /**
     * 单发短信
     * 
     * @param mobileNo
     * @param content
     * @param needstatus
     *            是否需要状态报告，需要true，不需要false
     * @param product
     * @param extno
     * @return
     * @throws Exception
     */
    public static String send(String mobileNo, String tplid, String paramString) {
        if (StringUtils.isBlank(mobileNo) || StringUtils.isBlank(tplid)) {
            return "-1";
        }

        TaobaoClient client = new DefaultTaobaoClient(URL, APPKEY, APPSECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName(SIGNNAME);
        if (StringUtils.isNotEmpty(paramString)) {
            req.setSmsParamString(paramString);
        }

        req.setRecNum(mobileNo);
        req.setSmsTemplateCode(tplid);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            return "-2";
        }
        if (!StringUtils.isEmpty(rsp.getErrorCode())) {
            return "fail " + rsp.getMsg();
        }

        BizResult result = rsp.getResult();
        if (result == null) {
            return "-2";
        }
        if ("0".equals(result.getErrCode())) {
            return "success " + result.getModel();
        } else {
            return "fail " + rsp.getSubCode();
        }
    }

    /**
     * 群发短信
     * 
     * @param content
     *            短信内容
     * @param needstatus
     *            是否需要状态报告，需要true，不需要false
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */
    public static String batchSend(List<String> mobileNoList, String tplid,
            String paramString) {
        if (SimpleUtils.isNullList(mobileNoList) || StringUtils.isEmpty(tplid)) {
            return "-1";
        }

        try {
            for (String mobile : mobileNoList) {
                send(mobile, tplid, paramString);
            }
            return "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return "-2";
    }

    public static void main(String[] args) {
//        System.out.println(send("18751980706", "SMS_12195761", null));
        System.out.println(send("18085070520", "SMS_10095009", "{\"code\":\"123456\",\"product\":\"乐夺宝\"}"));
    }
}
