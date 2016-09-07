package com.zx.stlife.base;

import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.sys.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by micheal on 16/1/23.
 */
public class UserWxUtils {
    
    private static String USER = "user";
    
    public static HttpServletRequest getRequest(){
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static void setUserToSession(User user){
        HttpServletRequest request = getRequest();
        setUserToSession(request.getSession(), user);
    }

    public static void setUserToSession(HttpSession session, User user){
        session.setAttribute(USER, user);
    }

    public static User getUserFromSession(){
        HttpServletRequest request = getRequest();
        return getUserFromSession(request.getSession());
    }

    public static User getUserFromSession(HttpSession session){
        return (User)session.getAttribute(USER);
    }

    public static Integer getCurrUserId(){
        User user = getCurrUser();
        if(user == null)
            return null;

        return user.getId();
    }

    public static Integer getCurrUserId(HttpSession session){
        User user = getCurrUser(session);
        if(user == null)
            return null;

        return user.getId();
    }

    public static User getCurrUser(){
        User user = getUserFromSession();
        return user;
    }

    public static User getCurrUser(HttpSession session){
        User user = getUserFromSession(session);
        return user;
    }

    public static boolean hasLogin(HttpSession session){
        User user = getUserFromSession(session);
        return null != user
                && null !=user.getId();
    }

    public static void refreshUser(User user){
        HttpServletRequest request = getRequest();
        refreshUser(request.getSession(), user);
    }

    public static void refreshUser(HttpSession session, User user){
        setUserToSession(session, user);
    }

    public static void setBackUrl(String backUrl){
        HttpServletRequest request = getRequest();
        request.getSession().setAttribute("backUrl", backUrl);
    }

    public static String getBackUrl(String backUrl, boolean isDel){
        HttpServletRequest request = getRequest();
        String url = (String)request.getSession().getAttribute("backUrl");
        if(url != null && isDel){
            request.getSession().removeAttribute("backUrl");
        }

        return url;
    }
}
