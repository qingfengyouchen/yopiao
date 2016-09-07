package com.zx.stlife.tools;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 
 * @ClassName: MyX509TrustManager
 * @Description: TODO(证书信任管理器（用于https请求）)
 * @author chenhq 526562339@qq.com
 * @date 2015-4-21 下午3:03:43
 * 
 */
public class MyX509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String authType) throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String authType) throws CertificateException {

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
	}

}