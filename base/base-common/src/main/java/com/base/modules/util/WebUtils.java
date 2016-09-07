package com.base.modules.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @author micheal
 * 
 */
public class WebUtils {
	// -- header 常量定义 --//
	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final boolean NOCACHE_DEFAULT = true;

	// -- content-type 常量定义 --//
	private static final String TEXT_TYPE = "text/plain";
	private static final String JSON_TYPE = "application/json";
	private static final String XML_TYPE = "text/xml";
	private static final String HTML_TYPE = "text/html";
	
	private static Logger logger = LoggerFactory.getLogger(WebUtils.class);
	/**
	 * 
	 * @param response
	 * @param fileName
	 * @param contentType
	 */
	public static void prepareDownload(HttpServletResponse response, String fileName, String contentType) {
		try {
			String encodedFileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");

			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition", String.format("attachment; filename=%s", encodedFileName));
			response.setContentType(contentType);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}

	}

	/**
	 * 
	 * @param response
	 * @param fileName
	 * @param contentType
	 */
	public static void prepareDownload(HttpServletResponse response, String fileName, ContentType contentType) {
		prepareDownload(response, fileName, contentType.asMeta());
	}
	/**
	 * 设置立即过期 
	 */
	public static void setExpires(HttpServletResponse response) {
		//Http 1.0 header
		response.setDateHeader("Expires", 0);
		//Http 1.1 header
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Pragma","No-cache");
	}
	/** 返回IP */
	public static String getIPv4(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static enum ContentType {

		PPT("application/vnd.ms-powerpoint"),
		WORD("application/msword"),
		EXCEL("application/msexcel"),

		PDF("application/pdf"),

		OCTET("application/octet-strem"),

		ZIP("application/zip"),
		TXT("text/plain"),
		JS("application/x-javascript"),
		
		JPG("image/jpeg"),
		PNG("image/png"),
		BMP("application/x-bmp"),
		GIF("image/gif");

		private final String meta;

		private ContentType(final String meta) {
			this.meta = meta;
		}

		public String asMeta() {
			return meta;
		}
	}

	private WebUtils() {
	}
	
	// -- 绕过jsp/freemaker直接输出文本的函数 --//
	/**
	 * 直接输出内容的简便函数.
	 * 
	 * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
	 * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
	 * "no-cache:false");
	 * 
	 * @param headers
	 *            可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	public static void render(HttpServletResponse response,
			final String contentType, final String content,
			final String... headers) {
		try {
			// 分析headers参数
			String encoding = ENCODING_DEFAULT;
			boolean noCache = NOCACHE_DEFAULT;
			for (String header : headers) {
				String headerName = StringUtils.substringBefore(header, ":");
				String headerValue = StringUtils.substringAfter(header, ":");

				if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
					encoding = headerValue;
				} else if (StringUtils.equalsIgnoreCase(headerName,
						NOCACHE_PREFIX)) {
					noCache = Boolean.parseBoolean(headerValue);
				} else
					throw new IllegalArgumentException(headerName
							+ "不是一个合法的header类型");
			}

			// 设置headers参数
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
			}

			response.getWriter().write(content);
			response.getWriter().flush();

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 直接输出文本.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderText(HttpServletResponse response,
			final String text, final String... headers) {
		render(response, TEXT_TYPE, text, headers);
	}

	/**
	 * 直接输出HTML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(HttpServletResponse response,
			final String html, final String... headers) {
		render(response, HTML_TYPE, html, headers);
	}

	/**
	 * 直接输出XML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(HttpServletResponse response,
			final String xml, final String... headers) {
		render(response, XML_TYPE, xml, headers);
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param jsonString
	 *            json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(HttpServletResponse response,
			final String jsonString, final String... headers) {
		render(response, JSON_TYPE, jsonString, headers);
	}
	
	public static String getRequestUrl(HttpServletRequest request, boolean isEncodeUrl){
		String queryStr = request.getQueryString();
		String url = null;
		if(StringUtils.isBlank(queryStr)){
			url = String.format("%s%s", getRequestHostUrl(request),
					request.getRequestURI());
		}else{
			url = String.format("%s%s%s%s", getRequestHostUrl(request),
					request.getRequestURI(), "?", queryStr);
		}

		if(isEncodeUrl) {
			return Encodes.urlEncode(url);
		}

		return url;
	}
	
	public static String getRequestHostUrl(HttpServletRequest request){
		String url = String.format("%s%s%s", request.getScheme(),
				"://", request.getHeader("host"));
		
		return url;
	}
}
