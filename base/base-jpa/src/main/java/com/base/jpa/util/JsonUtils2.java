package com.base.jpa.util;

import com.base.jpa.query.Page;
import com.base.modules.util.json.Field;
import com.base.modules.util.json.JsonUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author micheal
 */
public class JsonUtils2 extends JsonUtils {

    /***
     * 支持POJO,Map两种
     *
     * @param page
     * @param fields
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("rawtypes")
    public static String pageToJson(Page page, List<Field> fields) {
        StringBuffer json = new StringBuffer();
        json.append("{ ")
                .append(addAroundDoubleQuotes("total")).append(" : ").append(addAroundDoubleQuotes(page.getTotalCount()))
                .append(", ").append(addAroundDoubleQuotes("rows")).append(": ")
                .append(listToJson(page.getResult(), fields))
                .append("}");

        return json.toString();
    }

    /***
     * 支持POJO,Map两种
     *
     * @param page
     * @param fields
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("rawtypes")
    public static String pageToJson2(Page page, List<Field> fields) {
        StringBuffer json = new StringBuffer();
        json.append("{ ")
                .append(addAroundDoubleQuotes("pageNo")).append(" : ").append(addAroundDoubleQuotes(page.getPageNo()))
                .append(addAroundDoubleQuotes("totalPages")).append(" : ").append(addAroundDoubleQuotes(page.getTotalPages()))
                .append(", ").append(addAroundDoubleQuotes("totalCount")).append(" : ").append(addAroundDoubleQuotes(page.getTotalCount()))
                .append(", ").append(addAroundDoubleQuotes("result")).append(": ")
                .append(listToJson(page.getResult(), fields))
                .append(" }");

        return json.toString();
    }

}
