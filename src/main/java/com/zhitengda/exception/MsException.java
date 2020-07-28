package com.zhitengda.exception;

import com.zhitengda.util.RetResult;

/**
 * @author langao_q
 * @create 2020-07-28 10:13
 * 自定义异常
 */
public class MsException extends RuntimeException{


    private int code;

    /**
     * 自定义异常01
     * @param result
     */
    public MsException(RetResult result) {
        super(result.getMessage());
        this.code = result.getCode();
    }

    /**
     * 自定义异常02
     * @param msg 异常信息,错误码默认为400
     */
    public MsException(String msg) {
        super(msg);
        this.code = 400;
    }

    /**
     * 自定义异常03
     * @param code 错误码
     * @param msg 错误信息
     */
    public MsException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
