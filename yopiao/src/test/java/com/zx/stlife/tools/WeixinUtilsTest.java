package com.zx.stlife.tools;

import com.base.modules.util.WebUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.tools.weixin.WeixinUitls;
import org.junit.Test;

/**
 * Created by micheal on 16/1/2.
 */
public class WeixinUtilsTest {

    @Test
    public void test(){
        System.out.println(WeixinUitls.queryWeixinPayResult("8aafe0dd51f8a6ff0152019dbbaf0019", Const.WX_API_KEY));
    }
}
