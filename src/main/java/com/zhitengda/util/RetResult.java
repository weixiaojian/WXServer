package com.zhitengda.util;

import lombok.Data;

/**
 * 统一的 REST响应对象
 * @author langao_q
 * @since 2020-07-13 14:52
 */
@Data
public class RetResult {

    public static int SUCCESS_CODE = 0;
    public static int FAIL_CODE = 1;

    int code;
    String message;
    Object data;

    private RetResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RetResult success() {
        return new RetResult(SUCCESS_CODE,null,null);
    }
    public static RetResult success(Object data) {
        return new RetResult(SUCCESS_CODE,"",data);
    }
    public static RetResult fail(String message) {
        return new RetResult(FAIL_CODE,message,null);
    }

}
