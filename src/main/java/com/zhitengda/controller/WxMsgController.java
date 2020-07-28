package com.zhitengda.controller;

import cn.hutool.http.HttpUtil;
import com.zhitengda.config.WxConfig;
import com.zhitengda.exception.MsException;
import com.zhitengda.util.RetResult;
import com.zhitengda.weixin.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author langao_q
 * @create 2020-07-28 9:51
 * 微信msg控制类
 */
@Slf4j
@Controller
@RequestMapping("/msg")
public class WxMsgController {

    @Autowired
    private WxConfig wxConfig;

    /**
     * 创建菜单
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping("/createMenu")
    public void createMenu(HttpServletResponse res) throws Exception {
        String str = MenusEntity.menus();
        String jsonResult = HttpUtil.post(wxConfig.getCreateMenuUrl() + AccessToken.token, str);
        res.getWriter().write(jsonResult);
    }

    /**
     * 微信开发模式配置的签名验证地址
     * @param echostr   微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param signature 随机字符串
     * @return 验证结果
     */
    @RequestMapping({"/checkSignature"})
    public void checkSignature(HttpServletResponse res,
                               @RequestParam String echostr,
                               @RequestParam String timestamp,
                               @RequestParam String nonce,
                               @RequestParam String signature) {
        try {
            //1.将token、timestamp、nonce三个参数进行字典序排序
            List<String> list = new ArrayList<String>();
            list.add(timestamp);
            list.add(nonce);
            list.add(wxConfig.getToken());
            Collections.sort(list);
            //2.将三个参数字符串拼接成一个字符串进行sha1加密
            String signatureCp = SHAEncryption.SHA1(list.get(0) + list.get(1) + list.get(2));
            //3.开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
            if (signatureCp.equals(signature)) {
                res.getWriter().print(echostr);
            } else {
                throw new MsException("微信签名验证失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取微信JsConfig
     * @param url 页面路径
     * @return 返回结果
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping("/getJsConfig")
    public Map getJsConfig(@RequestParam String url) throws Exception {
        return JsSignUtil.sign(url, wxConfig.getAppId());
    }

    /**
     * 发送模板消息
     * @param openId 用户openid
     * @param content 内容
     * @param billCode 运单号
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendMsg")
    public RetResult sendMsg(@RequestParam String openId
            , @RequestParam String content
            , @RequestParam String billCode){
        String result = TemplateMsgUtil.sendMsg(openId, content, billCode);
        return RetResult.success(result);
    }

    /**
     * 获取微信配置 appId、appSecret、token等
     * @return 微信配置
     */
    @ResponseBody
    @RequestMapping("/getConfig")
    public RetResult getConfig(){
        return RetResult.success(wxConfig);
    }
}
