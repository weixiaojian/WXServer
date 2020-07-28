package com.zhitengda.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author langao_q
 * @create 2020-07-21 15:13
 * 微信配置类
 */
@Data
@Component
@ConfigurationProperties("weixin")
public class WxConfig {

    /**
     * 微信appid
     */
    private String appId;
    /**
     * 微信appSecret
     */
    private String appSecret;
    /**
     * 微信token
     */
    private String token;

    /**
     * 获取accessToken地址url
     */
    private String accessTokenUrl;

    /**
     * 获取accessTicket地址url
     */
    private String jsapiTicketUrl;

    /**
     * 创建菜单地址url
     */
    private String createMenuUrl;

}
