package com.zhitengda.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhitengda.config.WxConfig;
import com.zhitengda.util.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * index控制类
 * @author langao_q
 * @since 2020-07-17 18:04
 */
@Slf4j
@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private WxConfig wxConfig;

    /**
     * 跳转到vue界面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    /**
     * 跳转到html页面
     * @return
     */
    @RequestMapping("/index2")
    public String index2(){
        return "index2";
    }

    /**
     * 错误页面
     * @param msg 错误消息
     * @return
     */
    @RequestMapping("/error")
    public String error(String msg){
        log.error("----错误----" + msg);
        return "error";
    }

    /**
     * 根据code获取用户数据 然后再返回用户数据
     * @param code code
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth")
    public RetResult authorization(String code){
        try {
            log.info("-----第一步：获取code-----" + code);
            Map<String, Object> mapOpenid = new HashMap<String, Object>();
            mapOpenid.put("code", code);
            mapOpenid.put("appid", wxConfig.getAppId());
            mapOpenid.put("secret", wxConfig.getAppSecret());
            mapOpenid.put("view", "web");
            mapOpenid.put("grant_type", "authorization_code");
            String resultOpenid = HttpUtil.post("https://api.weixin.qq.com/sns/oauth2/access_token", mapOpenid);

            JSONObject objOpenid = JSONUtil.parseObj(resultOpenid);
            log.info("-----第二步：获取openid-----" + objOpenid);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("access_token", objOpenid.getStr("access_token"));
            userMap.put("openid", objOpenid.getStr("openid"));
            userMap.put("lang", "zh_CN");
            String resultUser = HttpUtil.get("https://api.weixin.qq.com/sns/userinfo", userMap);

            JSONObject objUser = JSONUtil.parseObj(resultUser);
            log.info("-----第三步：保存/更新用户信息-----" + objUser);
            return RetResult.success(objUser);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/auth2")
    public String auth(){
        return "index3";
    }
}
