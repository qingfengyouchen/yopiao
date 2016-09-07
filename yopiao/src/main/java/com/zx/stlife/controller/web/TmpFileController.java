package com.zx.stlife.controller.web;

import com.zx.stlife.base.UserUtils;
import com.zx.stlife.service.TmpFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by micheal on 15/6/25.
 */
@Controller
@RequestMapping(value = "/tmpFile")
public class TmpFileController {
    private static Logger logger = LoggerFactory.getLogger(TmpFileController.class);
    @Autowired
    private TmpFileService tmpFileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    private Integer upload(@RequestParam("file") MultipartFile file) {
        Integer id = tmpFileService.upload(file, UserUtils.getCurrentUserId());
        return id;
    }

    @RequestMapping(value =  "uploadWithBase64", method = RequestMethod.POST)
    @ResponseBody
    public Integer uploadWithBase64(@RequestParam("file")String file){
        Integer userId = 3;
        Integer id = tmpFileService.uploadBase64File(userId, file);
        return id;
    }

}
