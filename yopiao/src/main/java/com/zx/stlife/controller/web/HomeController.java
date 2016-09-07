package com.zx.stlife.controller.web;

import com.zx.stlife.base.UserUtils;
import com.zx.stlife.entity.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by micheal on 15/6/25.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/home" , method = RequestMethod.GET)
    private String main(HttpSession session, HttpServletRequest request, Model model) {
        User user = UserUtils.getCurrentUser();
        model.addAttribute("us", user);
        return "home";
    }
}
