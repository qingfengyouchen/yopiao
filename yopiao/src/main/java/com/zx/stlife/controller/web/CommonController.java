package com.zx.stlife.controller.web;

import com.zx.stlife.service.sys.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by micheal on 15/6/25.
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController {

    @Autowired
    private AccountService accountService;

}
