package com.zx.stlife.tools.alipay;

import static com.zx.stlife.constant.Const.*;

import com.base.modules.util.Encodes;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by micheal on 15/12/23.
 */
public class AlipayUtils {

    private static String PAY_SUBJECT_H5 = Encodes.urlEncode(PAY_SUBJECT);
    /**
     * 构建请求参数
     * @param outTradeNo
     * @param totalFee
     * @param isPay 是否支付，否则为充值
     * @return
     */
    public static SortedMap<String, String> buildRequstParams(String outTradeNo, int totalFee, boolean isPay, boolean isAlipayWithH5){
        SortedMap<String, String> parameters = new TreeMap<>();
        String money = String.valueOf(isPay1Fen ? 0.01 : totalFee);
        String notifyUrl = isPay ? ALIPAY_PAY_NOTIFY_URL : ALIPAY_RECHARGE_NOTIFY_URL;
        if(isAlipayWithH5){
            parameters.put("service", ALIPAY_SERVICE_H5);
            parameters.put("partner", ALIPAY_PARTNER);
            parameters.put("_input_charset", ALIPAY_INPUT_CHARSET);
            parameters.put("sign_type", ALIPAY_SIGN_TYPE);
            parameters.put("notify_url", notifyUrl);
            parameters.put("out_trade_no", outTradeNo);
            parameters.put("subject", PAY_SUBJECT);
            parameters.put("total_fee", money);
            parameters.put("seller_id", ALIPAY_SELLER_ID);
            parameters.put("payment_type", ALIPAY_PAYMENT_TYPE);
            parameters.put("it_b_pay", ALIPAY_IT_B_PAY);
        }else {
            putElement(parameters, "service", ALIPAY_SERVICE);
            putElement(parameters, "partner", ALIPAY_PARTNER);
            putElement(parameters, "_input_charset", ALIPAY_INPUT_CHARSET);
            putElement(parameters, "sign_type", ALIPAY_SIGN_TYPE);
            putElement(parameters, "notify_url", notifyUrl);
            putElement(parameters, "out_trade_no", outTradeNo);
            putElement(parameters, "subject", PAY_SUBJECT);
            putElement(parameters, "payment_type", ALIPAY_PAYMENT_TYPE);
            putElement(parameters, "seller_id", ALIPAY_SELLER_ID);
            putElement(parameters, "total_fee", money);
            putElement(parameters, "body", PAY_SUBJECT);
            putElement(parameters, "it_b_pay", ALIPAY_IT_B_PAY);
        }

        putSign(parameters, isAlipayWithH5);
        return parameters;
    }


    public static void putElement(SortedMap<String, String> params, String key, String value){
        params.put(key, "\"" + value + "\"");
    }

    /*public static void main(String[] args) {
        System.out.println(Encodes.urlEncode("学分充值"));
        System.out.println(Encodes.urlDecode(Encodes.urlEncode("学分充值")));
    }*/

    public static String buildRequestUrl(String outTradeNo, int totalFee, boolean isPay, boolean isAlipayWithH5){
        SortedMap<String, String> params =
                buildRequstParams(outTradeNo, totalFee, isPay, isAlipayWithH5);

        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (StringUtils.isNotBlank(k) &&
                    StringUtils.isNotBlank(v)) {
                sb.append(k).append("=")
                        .append(isAlipayWithH5 && "subject".equals(k) ? PAY_SUBJECT_H5 : v)
                        .append("&");
            }
        }

        String paramsStr = sb.substring(0, sb.length() - 1);
        if(isAlipayWithH5){
            paramsStr = ALIPAY_GATEWAY_H5 + "?" + paramsStr;
        }
        return paramsStr;
    }

    public static void putSign(SortedMap<String, String> parameters, boolean isAlipayWithH5){
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (StringUtils.isNotBlank(k) &&
                    StringUtils.isNotBlank(v) &&
                    !"sign".equals(k) &&
                    !"sign_type".equals(k)) {
                sb.append(k).append("=")
                        .append(v)
                        .append("&");
            }
        }
        String content = sb.substring(0, sb.length() - 1);

        String sign = RSAUtils.urlEncode(content, ALIPAY_RSA_PRIVATE_KEY);
        if(isAlipayWithH5){
            parameters.put("sign", sign);
        }else {
            putElement(parameters, "sign", sign);
        }
    }
}
