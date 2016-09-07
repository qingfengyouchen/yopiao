package com.zx.stlife.service.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zx.stlife.tools.weixin.WxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import static com.zx.stlife.constant.ConstWeixinH5.*;

@Service
public class AutoResponseService {

	@Autowired
	private WxAccountService wxAccountService;

	public void sendMessage(String openid, String contentJson) {
		String requestUrl = buildSendMessageUrl();
		JSONObject messageJO = new JSONObject();
		messageJO.put("touser", openid);
		messageJO.put("msgtype", "text");
		JSONObject contentJO = new JSONObject();
		contentJO.put("content", contentJson);
		messageJO.put("text", contentJO);
		//System.out.println(JSON.toJSONString(messageJO));
		WxCommonUtils.httpsRequest(requestUrl, "POST", JSON.toJSONString(messageJO));// 发送消息
	}

	public void sendNewMessage(String openid, List<Map<String,Object>> list, String requestUrl) {
		JSONObject messageJO = new JSONObject();
		messageJO.put("touser", openid);
		messageJO.put("msgtype", "news");
		JSONObject contentJO = new JSONObject();
		contentJO.put("articles", list);
		messageJO.put("news", contentJO);
		System.out.println(JSON.toJSONString(messageJO));
		WxCommonUtils.httpsRequest(requestUrl, "POST", JSON.toJSONString(messageJO));// 发送消息
	}

	/*public void sendApplySalerNotice(List<String> openids,
									 String companyName, String subCompanyName, String name, String salerId) {
		if(SimpleUtils.isNotNullList(openids)) {
			String content = NOTICE_APPLY_SALER.replace("{companyName}", companyName)
					.replace("{subCompanyName}", subCompanyName)
					.replace("{name}", name)
					.replace("{id}", salerId);
			for (String openid : openids) {
				sendMessage(openid, content);
			}
		}
	}*/

	private String buildSendMessageUrl(){
		String accessToken = wxAccountService.getValidAccessToken();
		return CUSTOMER_SERVICE_URL.replace("ACCESS_TOKEN", accessToken);
	}

}