package com.zx.stlife.tools.ajax;

/**
 * Created by micheal on 15/9/26.
 */
public class AjaxFormResult {

    public static String RESULT_SUCCESS = "y";
    public static String RESULT_FAIL = "n";

    private String info;
    private String status;

    public AjaxFormResult() {
    }

    public AjaxFormResult(String info, String status) {
        this.info = info;
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static AjaxFormResult getSuccessResult(String info){
        return new AjaxFormResult(info, RESULT_SUCCESS);
    }

    public static AjaxFormResult getFailResult(String info){
        return new AjaxFormResult(info, RESULT_FAIL);
    }

    public static AjaxFormResult getSuccessResult(Integer code){
        return getSuccessResult(String.valueOf(code));
    }

    public static AjaxFormResult getFailResult(Integer code){
        return getFailResult(String.valueOf(code));
    }
}
