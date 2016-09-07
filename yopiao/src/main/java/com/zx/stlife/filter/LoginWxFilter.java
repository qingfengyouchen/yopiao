package com.zx.stlife.filter;

import com.base.modules.util.Encodes;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.ConstWeixinH5;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 微信版h5过滤器
 */
public class LoginWxFilter implements Filter {

	public Logger logger = LoggerFactory.getLogger(getClass());

	/**不需要用户登录的功能*/
	private static String EXCLUDE_REGEX = "^.*(" +
			"/login/|" +
			"/user/saveUser|" +
			"/user/modifyInfo|" +
			"/wx/wechat" +
			").*$";

	/**需要用户登录的功能*/
	private static String INCLUDE_REGEX = "^.*(" +
			"/user/|" +
			"/snatchPay/|" +
			"/receiveAddress/|" +
			"/shareGoods/create/|" +
			"/shareGoods/save/|" +
			"/shareGoods/deleteImg/|" +
			"/snatchList/|" +
			"/winng/|" +
			"/jifen/|" +
			"/redPack/" +
			").*$";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession(true);
		String url = request.getRequestURI();
		boolean isLogin = UserWxUtils.hasLogin(session);
		boolean needAuth = "true".equals(request.getParameter("needAuth"));

		//拦截未登录情况下访问/wx下的目录
		if ( needAuth || (
				!isLogin &&
				StringUtils.isNotBlank(url) &&
				!SimpleUtils.regex(url, EXCLUDE_REGEX) &&
				SimpleUtils.regex(url, INCLUDE_REGEX) ) ) {

			/*if(url.contains("/wx/myFavorite/save")){
				AjaxFormResult ajaxFormResult = new AjaxFormResult("请登录系统", AjaxFormResult.RESULT_FAIL);
				WebUtils.renderJson(response, JSON.toJSONString(ajaxFormResult));
				return;
			}*/

			String redirectUrl = ConstWeixinH5.LOGIN_URL;
			String backUrl = WebUtils.getRequestUrl(request, false);
			if(needAuth){
				backUrl = backUrl.replace("needAuth=true", "needAuth=");
			}
			if(!backUrl.endsWith("/wx/index")){
				redirectUrl += "?backUrl=" + Encodes.encodeBase64(backUrl.getBytes());
			}

			response.sendRedirect(redirectUrl);
		}else{
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
