package com.zx.stlife.controller.wx;

import com.zx.stlife.entity.wx.WxAccount;
import com.zx.stlife.service.wx.WxAccountService;
import com.zx.stlife.tools.weixin.WxSignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/wx/wechat")
public class WechatController {
	@Autowired
	private WxAccountService wxAccountService;
	
	@RequestMapping(params = "wechat", method = RequestMethod.GET)
	public void wechatGet(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "signature") String signature, @RequestParam(value = "timestamp") String timestamp,
			@RequestParam(value = "nonce") String nonce, @RequestParam(value = "echostr") String echostr) {

		WxAccount wxAccount = wxAccountService.get();
		if(WxSignUtils.checkSignature(wxAccount.getToken(), signature, timestamp, nonce)){
			try {
				response.getWriter().print(echostr);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	@RequestMapping(params = "wechat", method = RequestMethod.GET)
	public void wechatPost(HttpServletResponse response, HttpServletRequest request) throws IOException {
		//String respMessage = wechatService.coreService(request);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print("hello");
		out.close();
	}*/
}
