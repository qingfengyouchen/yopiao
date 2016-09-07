package com.zx.stlife.tools;

import java.util.List;
import java.util.Map;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.zx.stlife.controller.app.base.AppStatusCode;
import com.zx.stlife.controller.app.base.JsonResult;

/**
 * Created by micheal on 15/12/12.
 */
public class PageUtils {

    /**
     * 封装page
     * @param page 分页对象
     * @param attrs 属性列表
     * @return
     */
    public static JsonResult buildPage(Page page, String... attrs){

        /*if(SimpleUtils.isNullList(page.getResult())){
            return new JsonResult(AppStatusCode.SC_SUCCESS, null);
        }*/

        Map<String, Object> data= ConvertUtils.convertEntityToMap(
                page, "pageNo", "totalPages", "totalCount");
        List<Map<String, Object>> result= ConvertUtils.convertCollectionToListMap
                (page.getResult(), attrs);

        data.put("result", result);
        return new JsonResult(AppStatusCode.SC_SUCCESS, data);
    }
    
    /**
     * 封装page
     * @param page 分页对象
     * @param attrAliasArrList 属性列表,长度为2，当长度为1时表示不使用别名；否则使用别名(第二个元素就是别名)
     * @return
     */
    public static JsonResult buildPage(Page page, String[]... attrAliasArrList){

        /*if(SimpleUtils.isNullList(page.getResult())){
            return new JsonResult(AppStatusCode.SC_SUCCESS, null);
        }*/

        Map<String, Object> data= ConvertUtils.convertEntityToMap(
                page, "pageNo", "totalPages", "totalCount");
        List<Map<String, Object>> result= ConvertUtils.convertCollectionToListMap
                (page.getResult(), attrAliasArrList);

        data.put("result", result);
        return new JsonResult(AppStatusCode.SC_SUCCESS, data);
    }
}
