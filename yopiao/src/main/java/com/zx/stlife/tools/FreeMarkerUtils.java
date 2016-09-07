package com.zx.stlife.tools;

import com.base.modules.util.FreeMarkers;
import com.base.modules.util.FileUtilsEx;
import com.zx.stlife.constant.Const;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * Created by micheal on 15/7/8.
 */
public class FreeMarkerUtils {
    private static Logger logger = LoggerFactory.getLogger(FreeMarkerUtils.class);

    public static Configuration freemarkerCfg = null;
    static {
        try{
            freemarkerCfg = FreeMarkers.buildConfiguration(Const.TEMPLATE_ROOT_PATH, true);
            freemarkerCfg.setClassicCompatible(true);
            freemarkerCfg.setDefaultEncoding("utf-8");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     *  生成html页面
     * @param relativePathOfTemplate 模板的相对路径
     * @param model                  模板封装的参数模型
     * @param relativePathOfHtml     生成网页的相对路径
     * @return 生成页面是否成功
     */
    public static boolean createHtml(String relativePathOfTemplate, Map<String, Object> model,
                                     String dir, String relativePathOfHtml){
        Boolean isSuccess = true;
        try{
            Template template = freemarkerCfg.getTemplate(relativePathOfTemplate);
            String content = FreeMarkers.renderTemplate(template, model);
            String htmlPath = FileUtilsEx.joinPaths(dir, "/", relativePathOfHtml);
            FileUtilsEx.write(new File(htmlPath), content, "utf-8");
        }catch (Exception ex){
            isSuccess = false;
            logger.error(ex.getMessage(), ex);
        }

        return isSuccess;
    }
}
