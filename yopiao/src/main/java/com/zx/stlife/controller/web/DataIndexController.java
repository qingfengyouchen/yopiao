package com.zx.stlife.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by micheal on 15/6/25.
 */
@Controller
public class DataIndexController {

    @RequestMapping(value = "/dataIndex")
    public String list(Model model) {

        return "data-index";
    }

}
