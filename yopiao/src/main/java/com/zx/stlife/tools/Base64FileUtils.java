package com.zx.stlife.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by micheal on 15/9/21.
 */
public class Base64FileUtils {

    public static Logger logger = LoggerFactory.getLogger(Base64FileUtils.class);

    public static boolean decoderBase64File(String base64FileStr,String imgFilePath){
        if (base64FileStr == null){ // 图像数据为空
            return false;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64FileStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return  false;
    }
}
