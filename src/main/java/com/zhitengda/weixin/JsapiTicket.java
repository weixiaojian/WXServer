package com.zhitengda.weixin;

/**
 * JsapiTicket实体类
 * @author langao_q
 * @since 2020-07-28 10:57
 */
public class JsapiTicket {

    /**
     * ticket实体
     */
    public static String ticket;

    /**
     * 凭证有效时间，单位：秒
     */
    public static Long expiresIn;

    /**
     * 错误编码
     */
    public static String errcode;

    /**
     * 错误消息详情
     */
    public static String errmsg;

}
