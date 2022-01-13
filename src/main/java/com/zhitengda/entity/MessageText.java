package com.zhitengda.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author langao_q
 * @since 2021-09-07 17:30
 */
@Data
public class MessageText implements Serializable {

    /**
     * 接收方帐号/开发者微信号
     */
    protected String ToUserName;
    /**
     * 开发者微信号/发送方帐号
     */
    protected String FromUserName;
    /**
     * 消息时间
     */
    protected long CreateTime;
    /**
     * 消息类型:text、image、voice、video、music、news
     */
    protected String MsgType;

    /**
     * 文本消息内容
     */
    private String Content;

    /**
     * 消息id，64位整型
     */
    private String MsgId;
    /**
     * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
     */
    private String Event;
    /**
     * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
     */
    private String EventKey;
}
