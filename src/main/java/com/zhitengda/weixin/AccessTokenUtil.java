package com.zhitengda.weixin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhitengda.config.WxConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * AccessToken获取工具类
 * 设置过期时间 如果过期再去重新获取
 * @author langao_q
 * @since 2020-07-28 10:26
 */
@Slf4j
@Component
public class AccessTokenUtil {

    @Autowired
    private WxConfig wxConfig;

    /**
     * access_token
     */
    private String access_token;

    private String jsapi_ticket;
    /**
     * 凭证有效时间
     */
    private Integer expires_in;
    /**
     * 过期时间（单位：毫秒）
     */
    private Long expiredTime;
    /**
     * 错误代码
     */
    private Integer errcode;
    /**
     * 错误消息
     */
    private String errmsg;
    /**
     * 接口获取到的json
     */
    private String json;

    /**
     * 获取过期时间
     * @return
     */
    public Long getExpiredTime() {
        return expiredTime;
    }

    @PostConstruct
    public String getAccessToken() {
        //验证token是否在有效时间内 有效直接返回
        if (isAvailable()) {
            return access_token;
        }
        //token过期 需要重新去获取一遍
        rushToken();
        return access_token;
    }

    public String getJsapiTicket() {
        //验证token是否在有效时间内 有效直接返回
        if (isAvailable()) {
            return jsapi_ticket;
        }
        //token过期 需要重新去获取一遍
        rushToken();
        return jsapi_ticket;
    }

    /**
     * 校验token是否有效
     *
     * @return
     */
    public boolean isAvailable() {
        if (expiredTime == null) {
            return false;
        } else if (errcode != null) {
            return false;
        } else if (expiredTime < System.currentTimeMillis()) {
            return false;
        } else {
            return access_token != null;
        }
    }

    /**
     * 刷新access_token
     */
    public void rushToken() {
        try {
            //1.组装url
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxConfig.getAppId()
                    + "&secret=" + wxConfig.getAppSecret();
            //2.发起请求
            log.info("【刷新access_token开始】，URI: " + url);
            this.json = HttpUtil.get(url);
            log.info("【刷新access_token结束】，RESULT: " + this.json);
            //3.解析结果
            JSONObject jsonObject = JSONUtil.parseObj(this.json);
            if (jsonObject != null && !StrUtil.isBlankIfStr(jsonObject.getStr("access_token"))) {
                this.access_token = jsonObject.getStr("access_token");
                this.expires_in = jsonObject.getInt("expires_in");
                this.expiredTime = System.currentTimeMillis() + ((this.expires_in - 5) * 1000);
                log.error("刷新access_token成功，过期时间【" + this.expiredTime + "】，解析結果：" + this.json);
            } else {
                // 获取access_token失败
                this.errcode = jsonObject.getInt("errcode");
                this.errmsg = jsonObject.getStr("errmsg");
                log.error("刷新access_token失败，返回結果：" + this.json);
            }
            //-------------------------获取Ticket------------------------------------------------
            //4.组装url
            String ticketUrl = wxConfig.getJsapiTicketUrl() + "?access_token=" + access_token + "&type=jsapi&offset_type=1";
            //5.发起请求
            log.info("【刷新ticket开始】，URI: " + ticketUrl);
            String result = HttpUtil.get(ticketUrl);
            log.info("【刷新ticket结束】，result: " + result);
            //6.解析结果
            JSONObject resObj = JSONUtil.parseObj(result);
            if (resObj != null && !StrUtil.isBlankIfStr(resObj.getStr("ticket"))) {
                this.jsapi_ticket = resObj.getStr("ticket");
            } else {
                // 获取jsapiTicket失败
                log.error("获取ticket失败，返回結果：" + result);
            }
        } catch (Exception e) {
            this.errcode = 400;
            this.errmsg = e.getMessage();
            log.error("刷新access_token/ticket异常：", e);
        }
    }
}
