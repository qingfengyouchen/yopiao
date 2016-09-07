package com.zx.stlife.tools.weixin;

import com.zx.stlife.constant.Const;
import com.zx.stlife.tools.CryptoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

/**
 * micheal cao
 */
public class PayCommonUtils {
	private static Logger log = LoggerFactory.getLogger(PayCommonUtils.class);

	public static String CreateNoncestr(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < length; i++) {
			Random rd = new Random();
			res += chars.indexOf(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 32; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	/**
	 * @Description：sign签名
	 * @param characterEncoding
	 *            编码格式
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	public static String createSign(String characterEncoding, SortedMap<String, String> parameters, String apiKey) {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if (StringUtils.isNotBlank(k) &&
					StringUtils.isNotBlank(v) &&
					!"sign".equals(k) &&
					!"key".equals(k)) {
				sb.append(k)
						.append("=")
						.append(v)
						.append("&");
			}
		}

		sb.append("key=" + apiKey);
		String sign = MD5Utils.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
	
	public static String getSignString(String characterEncoding, SortedMap<String, String> parameters, String apiKey) {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if (StringUtils.isNotBlank(k) &&
					StringUtils.isNotBlank(v) &&
					!"sign".equals(k) &&
					!"key".equals(k)) {
				sb.append(k)
						.append("=")
						.append(v)
						.append("&");
			}
		}

		sb.append("key=" + apiKey);
		//String sign = MD5Utils.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sb.toString();
	}
	/**
	 * 
	 * @Title: createSignature 
	 * @Description: TODO(Signature签名) 
	 * @param @param characterEncoding
	 * @param @param parameters
	 * @param @return
	 * @param @throws Exception
	 * @return String
	 * @throws 
	 */
	public static String createSignature(SortedMap<String, String> parameters) throws Exception {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, String> entry : parameters.entrySet()){
			String k = entry.getKey();
			String v = entry.getValue();
			if (StringUtils.isNotBlank(k) &&
					StringUtils.isNotBlank(v)) {
				sb.append(k)
					.append("=")
					.append(v)
					.append("&");
			}
		}
		String str = null;
		if(sb.indexOf("&")>0){
			str = sb.substring(0, sb.length()-1);
		}
		String signature = CryptoUtils.hexSHA1(str);
		return signature;
	}

	/**
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	public static String getRequestXml(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		for(Map.Entry<String, String> entry : parameters.entrySet()){
			String k = entry.getKey();
			String v = entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "detail".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				sb.append("<").append(k).append(">")
						.append("<![CDATA[").append(v).append("]]>")
						.append("</").append(k).append(">");
			}else {
				sb.append("<").append(k).append(">")
						.append(v)
						.append("</").append(k).append(">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * @Description：返回给微信的参数
	 * @param return_code
	 *            返回编码
	 * @param return_msg
	 *            返回信息
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
		StringBuffer sb = new StringBuffer()
				.append("<xml><return_code><![CDATA[")
				.append(return_code)
				.append("]]></return_code><return_msg><![CDATA[")
				.append(return_msg)
				.append("]]></return_msg></xml>");
		return sb.toString();
	}
}
