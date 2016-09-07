package com.zx.stlife.tools.weixin;

import com.alibaba.fastjson.JSONObject;
import com.base.modules.util.DateUtilsEx;
import static com.zx.stlife.constant.Const.*;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.LogUtils;
import com.zx.stlife.tools.PayUtils;
import com.zx.stlife.tools.RequestUtils;
import com.zx.stlife.tools.XMLUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import static com.zx.stlife.controller.app.base.JsonResultUtils.*;

/**
 * 微信支付
 * Created by micheal on 15/12/19.
 */
public class WeixinUitls {

    private static Logger logger = LoggerFactory.getLogger(WeixinUitls.class);

    public static String SUCCESS = "SUCCESS";

    /**
     * 微信下单
     * @param request
     * @param user
     * @param outTradeNo
     * @param money
     * @param isSnatch
     * @param isH5Pay
     * @return
     */
    public static JsonResult order(HttpServletRequest request, User user, String outTradeNo,
                                   Integer money, boolean isSnatch, boolean isH5Pay){

        String type = isSnatch ? "支付" : "充值";
        try {
            String requestXML = WeixinUitls.buildWxPayRequestXML(
                    request, PAY_SUBJECT, user, outTradeNo, money, isSnatch, true);
            logger.info("微信{}下单请求参数：{}", type, requestXML);
            String responseXML = WeixinHttpUtils.httpsRequest(WX_UNIFIED_ORDER_URL, "POST", requestXML);
            logger.info("微信{}下单响应结果：{}", type, responseXML);

            Map<String, String> resultMap = XMLUtils.doXMLParse(responseXML);
            if(resultMap == null){
                return buildFailureResult(buildOneElementMap("errDescr", "微信"+ type +"下单失败"));
            }

            String apiKey = isH5Pay ? WX_H5_API_KEY : WX_API_KEY;
            if(SUCCESS.equalsIgnoreCase(resultMap.get("return_code")) &&
                    SUCCESS.equalsIgnoreCase(resultMap.get("result_code"))){
                if(isValidSign((TreeMap)resultMap, apiKey)){
                    SortedMap<String, String> data = new TreeMap<>();
                    String prepayId = resultMap.get("prepay_id");
                    if(isH5Pay){
                        data.put("appId", WX_H5_APP_ID);
                        //data.put("timeStamp", Long.toString(new Date().getTime()));
                        data.put("timeStamp", WeixinUitls.getCurrTimeStampWithS());
                        data.put("nonceStr", PayCommonUtils.CreateNoncestr());
                        data.put("package", "prepay_id=" + prepayId);
                        data.put("signType", WX_SIGN_TYPE);
                        //data.put("timestamp", WeixinUitls.getCurrTimeStampWithS());
                        String paySign = PayCommonUtils.createSign("UTF-8", data, apiKey);
                        logger.info("==========: " + PayCommonUtils.getSignString("UTF-8", data, apiKey));
                        //String paySign = resultMap.get("sign");
                        //data.put("packageValue", "prepay_id=" + prepayId); // 这里用packageValue是预防package是关键字在js获取值出错
                        data.put("paySign", paySign); // paySign的生成规则和Sign的生成规则一致
                        String userAgent = request.getHeader("user-agent");
                        char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
                        data.put("agent", new String(new char[]{agent}));// 微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。
                    }else {
                        data.put("appid", WX_APP_ID);
                        data.put("partnerid", WX_MCH_ID);
                        data.put("noncestr", PayCommonUtils.CreateNoncestr());
                        data.put("prepayid", prepayId);
                        data.put("package", "Sign=WXPay");
                        data.put("noncestr", PayCommonUtils.CreateNoncestr());
                        data.put("timestamp", WeixinUitls.getCurrTimeStampWithS());
                        String paySign = PayCommonUtils.createSign("UTF-8", data, apiKey);
                        data.put("sign", paySign); // paySign的生成规则和Sign的生成规则一致
                    }
                    data.put("outTradeNo", outTradeNo);
                    LogUtils.logMap("返回客户端数据: ", data);

                    return buildSuccessResult(data);
                }else{
                    logger.error("微信{}下单返回结果，签名错误。", type);
                }
            }else{
                String returnMsg = resultMap.get("return_msg");
                String errCodeDes = resultMap.get("err_code_des");
                String errDescr = errCodeDes == null ?
                        (returnMsg == null ? "微信"+ type +"下单失败" : returnMsg) : errCodeDes;
                logger.error(errDescr);
                return buildFailureResult(buildOneElementMap("errDescr",  errDescr));
            }
        } catch (Exception e) {
            logger.error("微信{}下单失败", type);
            logger.error(e.getMessage(), e);
        }

        return buildFailureResult(buildOneElementMap("errDescr", "微信"+ type +"下单失败"));
    }

    /**
     * 微信订单查询
     * @param outTradeNo
     * @return
     */
    public static Object[] queryWeixinPayResult(String outTradeNo, String apiKey){
        try {
            String requestXML = WeixinUitls.buildWxQueryOrderRequestXML(outTradeNo, apiKey);
            //logger.info("微信查询订单请求参数：{}", requestXML);
            String responseXML = WeixinHttpUtils.httpsRequest(WX_QUERY_ORDER_URL, "POST", requestXML);
            //logger.info("微信查询订单响应结果：{}", responseXML);

            Map<String, String>  resultMap = XMLUtils.doXMLParse(responseXML);
            if(resultMap != null){
                if(SUCCESS.equals(resultMap.get("return_code")) &&
                        SUCCESS.equals(resultMap.get("result_code"))){
                    if(isValidSign((TreeMap)resultMap, apiKey)){
                        String tradeState = resultMap.get("trade_state");
                        if(SUCCESS.equals(tradeState)){
                            String timeEndStr = resultMap.get("time_end");
                            Date payTime = DateUtilsEx.toDateFromYYYYMMDDHHMMSS(timeEndStr,
                                    StringUtils.isBlank(timeEndStr) ? DateUtilsEx.getNow() : null);
                            return new Object[]{true, payTime};
                        }else if("NOTPAY".equals(tradeState)){ // 未支付状态
                            return null;
                        }else{
                            logger.info("tradeState: {}", tradeState);
                        }
                    }
                }else{
                    LogUtils.logMap("微信查询订单失败，返回数据: ", resultMap);
                }
            }
        } catch (Exception e) {
            logger.error("微信查询订单失败， outTradeNo:{}", outTradeNo);
            logger.error(e.getMessage(), e);
        }

        return new Object[]{false};
    }

    public static boolean isValidSign(TreeMap resultMap, String apiKey){
        String paySign = PayCommonUtils.createSign("UTF-8", resultMap, apiKey);
        return paySign.equals(resultMap.get("sign"));
    }

    /**
     * 构建支付请求参数的XML
     * @param request
     * @param descr
     * @param user
     * @param outTradeNo
     * @param money
     * @param isSnatch 是否夺宝，否则为充值
     * @return
     */
    private static String buildWxPayRequestXML(HttpServletRequest request, String descr,
                                               User user, String outTradeNo, int money,
                                               boolean isSnatch, boolean isH5Pay) {
        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put("appid", isH5Pay ? WX_H5_APP_ID : WX_APP_ID);// 公众账号ID
        parameters.put("mch_id", isH5Pay ? WX_H5_MCH_ID : WX_MCH_ID);// 商户号
        //parameters.put("device_info", user.getUserInfo()); // String(32),终端设备号(门店号或收银设备ID),我们用来记录用户信息
        parameters.put("device_info", "WEB");
        parameters.put("nonce_str", PayCommonUtils.CreateNoncestr());// 随机字符串
        parameters.put("body", descr); // 商品或支付单简要描述 String(32)
        parameters.put("detail", descr);// 商品名称明细列表
        parameters.put("out_trade_no", outTradeNo);// 商户订单号
        parameters.put("total_fee",  String.valueOf( isPay1Fen ? 1 : (money * 100) ));// 总金额,交易金额默认为人民币交易，接口中参数支付金额单位为【分】
        parameters.put("spbill_create_ip", RequestUtils.getRealIp(request));// APP和网页支付提交用户端ip，这个地址是nginx实现负载均衡的时候转发过来的
        parameters.put("notify_url", (isSnatch ? WX_PAY_NOTIFY_URL : WX_RECHARGE_NOTIFY_URL) + (isH5Pay ? "H5" : "") );// 通知地址
        parameters.put("trade_type", isH5Pay ? "JSAPI" : "APP");// trade_type
        parameters.put("time_start", DateUtilsEx.getYYYYMMDDHHMMSS(DateUtilsEx.getNow()));
        parameters.put("time_expire", DateUtilsEx.getYYYYMMDDHHMMSS(PayUtils.getExpireDate()));
        if(isH5Pay){
            parameters.put("openid", user.getOpenId());
        }
        String sign = PayCommonUtils.createSign("UTF-8", parameters, isH5Pay ? WX_H5_API_KEY :WX_API_KEY);
        parameters.put("sign", sign);
        String requestXML = PayCommonUtils.getRequestXml(parameters);
        return requestXML;
    }

    /**
     * 构建查询订单请求参数的XML
     * @param outTradeNo
     * @return
     */
    private static String buildWxQueryOrderRequestXML(String outTradeNo, String apiKey) {
        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put("appid", WX_APP_ID);// 公众账号ID
        parameters.put("mch_id", WX_MCH_ID);// 商户号
        parameters.put("out_trade_no", outTradeNo);// 商户订单号
        parameters.put("nonce_str", PayCommonUtils.CreateNoncestr());// 随机字符串
        String sign = PayCommonUtils.createSign("UTF-8", parameters, apiKey);
        parameters.put("sign", sign);
        String requestXML = PayCommonUtils.getRequestXml(parameters);
        return requestXML;
    }

    /**
     * 获取当前时间戳（单位是秒）
     * @return
     */
    private static String getCurrTimeStampWithS(){
        return Long.toString(new Date().getTime()/1000);
    }


    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl
     *            请求地址
     * @param requestMethod
     *            请求方式（GET、POST）
     * @param outputStr
     *            提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

            jsonObject = JSONObject.parseObject(buffer.toString());
        }
        catch (ConnectException ce) {
            ce.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
