package com.zhitengda.task;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhitengda.config.WxConfig;
import com.zhitengda.weixin.AccessToken;
import com.zhitengda.weixin.JsapiTicket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author langao_q
 * @create 2020-07-28 10:38
 * 获取AccessToken/JsapiTicket任务
 */
@Slf4j
@Component
public class AccessTokenTask {

    @Autowired
    private WxConfig wxConfig;

    /**
     * 定时任务获取AccessToken/JsapiTicket
     * 每7000秒调用一次，并设置延迟一秒执行
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 7000 * 1000)
    public void RefreshToken() {
        try {
            //刷新AccessToken
            getToken(wxConfig.getAppId(), wxConfig.getAppSecret());
            log.info("token已更新! token:" + AccessToken.token);

            //刷新JsapiTicket
            getTicket(AccessToken.token);
            log.info("jsapiTicket已更新! ticket:" + JsapiTicket.ticket);
        } catch (Exception e) {
            log.error("获取微信adcessToken出错，信息如下：");
            e.printStackTrace();
        }
    }

    /**
     * 获取AccessToken方法
     * @param appid 微信appid
     * @param appSecrect 微信appSecrect
     */
    public void getToken(String appid, String appSecrect) {
        //1.组装url
        String url = wxConfig.getAccessTokenUrl() + "?grant_type=client_credential&appid=" + appid
                + "&secret=" + appSecrect;
        //2.发起请求
        log.info("【请求开始】，URI: " + url);
        String result = HttpUtil.get(url);
        log.info("【请求结束】，RESULT: " + result);
        //3.解析结果
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (jsonObject != null) {
            try {
                AccessToken.token = jsonObject.getStr("access_token");
                AccessToken.expiresIn = jsonObject.getLong("expires_in");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 获取jsapiTicket失败
            log.error("获取token失败，返回結果：" + result);
        }
    }

    /**
     * 获取JsapiTicket方法
     * @param token 传入accessToken
     */
    public void getTicket(String token) {
        //1.组装url
        String url = wxConfig.getJsapiTicketUrl() + "?access_token=" + token + "&type=jsapi&offset_type=1";
        //2.发起请求
        log.info("【请求开始】，URI: " + url);
        String result = HttpUtil.get(url);
        log.info("【请求结束】，result: " + result);
        //3.解析结果
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (jsonObject != null) {
            try {
                JsapiTicket.ticket = jsonObject.getStr("ticket");
                JsapiTicket.expiresIn = jsonObject.getLong("expires_in");
                JsapiTicket.errcode = jsonObject.getStr("errcode");
                JsapiTicket.errmsg = jsonObject.getStr("errmsg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 获取jsapiTicket失败
            log.error("获取ticket失败，返回結果：" + result);
        }
    }
}
