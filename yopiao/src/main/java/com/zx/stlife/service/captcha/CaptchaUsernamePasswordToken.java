package com.zx.stlife.service.captcha;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {
	private static final long serialVersionUID = 2214495096728566366L;
	private String captcha;
	
     public String getCaptcha() {
          return captcha;
     }
     public void setCaptcha(String captcha) {
          this.captcha = captcha;
     }

     public CaptchaUsernamePasswordToken() {
          super();
     }
     public CaptchaUsernamePasswordToken(String username, char[] password,
                boolean rememberMe, String host,String captcha) {        
          super(username, password, rememberMe, host);
          this.captcha = captcha;
     }
}
