package com.zx.stlife.base.exception;

import com.base.modules.util.SimpleUtils;
import com.zx.stlife.controller.app.base.AppStatusCode;
import com.zx.stlife.controller.app.base.JsonResult;

/**
 * Created by micheal on 15/12/18.
 */
public class BizException extends RuntimeException {

    private int errorState;

    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(int errorState, String message) {
        super(message);
        this.errorState = errorState;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BizException(int errorState) {
        this.errorState = errorState;
    }

    public int getErrorState() {
        return errorState;
    }

    public void setErrorState(int errorState) {
        this.errorState = errorState;
    }
}
