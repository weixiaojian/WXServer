package com.zhitengda.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhitengda.config.WxConfig;
import com.zhitengda.entity.MessageText;
import com.zhitengda.exception.MsException;
import com.zhitengda.weixin.AccessTokenUtil;
import com.zhitengda.util.CharacterUtils;
import com.zhitengda.util.RetResult;
import com.zhitengda.weixin.TextMessageUtil;
import com.zhitengda.weixin.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 微信msg控制类
 * @author langao_q
 * @since 2020-07-28 9:51
 */
@Slf4j
@Controller
@RequestMapping("/msg")
public class WxMsgController {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    /**
     * 创建菜单
     * @param res
     * @throws Exception
     */
    @RequestMapping("/createMenu")
    public void createMenu(HttpServletResponse res) throws Exception {
        String str = MenusEntity.menus();
        String jsonResult = HttpUtil.post(wxConfig.getCreateMenuUrl() + accessTokenUtil.getAccessToken(), str);
        res.getWriter().write(jsonResult);
    }

    /**
     * 微信开发模式配置的签名验证地址
     * 微信开发模式：用户关注事件/回复用户消息
     * @param echostr   微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param signature 随机字符串
     * @return 验证结果
     */
    @RequestMapping({"/"})
    public void checkSignature(HttpServletRequest req, HttpServletResponse res,
                               String echostr, String timestamp, String nonce, String signature) throws IOException {
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        //微信接口校验
        if (StrUtil.isNotBlank(echostr)) {
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
        } else {
            //1.处理公众号相关事件：用户关注、用户发送消息...
            MessageText messageText = TextMessageUtil.xmlToMap(req);
            log.info("收到用户消息：" + JSONUtil.toJsonStr(messageText));
            if (messageText == null) {
                res.getWriter().write("公众号暂时无法提供无法！");
            }
            if ("event".equals(messageText.getMsgType())) {
                //2.用户关注/取消关注事件
                subscribeMsg(res, messageText);
            } else if ("text".equals(messageText.getMsgType())) {
                //3.回复文本消息
                textMsg(res, messageText);
            } else {
                //非文本消息回复 友好提示
                res.getWriter().write("你好，查询运单轨迹请输入单号！");
            }
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
        return JsSignUtil.sign(url, wxConfig.getAppId(), accessTokenUtil.getJsapiTicket());
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
        String result = TemplateMsgUtil.sendMsg(openId, content, billCode, accessTokenUtil.getAccessToken());
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

    /**
     * 处理用户点击事件/关注公众号事件
     * @param response
     * @param messageText
     * @throws IOException
     */
    private void subscribeMsg(HttpServletResponse response, MessageText messageText) throws IOException {
        String message = "";
        StringBuilder resMsg = new StringBuilder();
        try {
            if(StrUtil.equals("CLICK", messageText.getEvent())){
                if(StrUtil.equals("now_consulting", messageText.getEventKey())){
                    //菜单：现在咨询事件
                    resMsg.append("【1】服务热线：400-025-7080\n");
                    resMsg.append("【2】如需查件直接回复运单号进行查件！\n");
                    resMsg.append("【3】其他问题，请留言！\n\n");
                    message = TextMessageUtil.initMessage(messageText.getFromUserName(), messageText.getToUserName(), resMsg.toString());
                    log.info("【现在咨询事件】" + message);
                }else if(StrUtil.equals("aging_query", messageText.getEventKey())){
                    //菜单：时效查询事件，素材id：C-wC5EkWI6zZCOk2qPXHS5HRUW4Af-AusY7Jh7MuWDo
                    message = TextMessageUtil.initImgMessage(messageText.getFromUserName(), messageText.getToUserName(), "C-wC5EkWI6zZCOk2qPXHS5HRUW4Af-AusY7Jh7MuWDo");
                    log.info("【时效查询事件】" + message);
                }
                response.getWriter().write(message);
            }else if (StrUtil.equals("subscribe", messageText.getEvent())) {
                //先获取用户的openId 然后再根据openId调用获取用户信息的接口 更新到数据库
                //1.获取openId
                String openId = messageText.getFromUserName();
                //2.获取完整的用户信息
                HashMap<String, Object> param = new HashMap<>();
                param.put("access_token", accessTokenUtil.getAccessToken());
                param.put("openid", openId);
                param.put("lang", "zh_CN");
                String result = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/user/info", param);
                log.info("用户关注后获取到用户信息：" + result);
                JSONObject resultObj = JSONUtil.parseObj(result);
                //3.保存/更新到数据库
                if(resultObj != null && StrUtil.isBlank(resultObj.getStr("errcode"))){
                    //TODO 保存/更新用户信息到数据库
                    log.info("用户关注后更新数据库成功：" + JSONUtil.toJsonStr(resultObj));
                }
                resMsg.append("您好！欢迎关注！\n");
                message = TextMessageUtil.initMessage(messageText.getFromUserName(), messageText.getToUserName(), resMsg.toString());
                response.getWriter().write(message);
            } else if (StrUtil.equals("unsubscribe", messageText.getEvent())) {
                //TODO 这里处理用户取消关注事件
                log.info("用户取消关注后更新数据库成功：" + JSONUtil.toJsonStr(messageText));
            }
        } catch (Exception e) {
            log.error("处理用户关注/取消关注公众号事件异常：", e);
            //异常时直接回复空字符串 标识收到消息无需微信重推
            response.getWriter().write("");
        }
    }

    /**
     * 处理用户文本消息
     * @param response
     * @param messageText
     * @throws IOException
     */
    private void textMsg(HttpServletResponse response, MessageText messageText) throws IOException {
        String message = null;
        StringBuilder resMsg = new StringBuilder();
        try {
            String content = messageText.getContent();
            //处理文本类型，实现输入单号回复轨迹
            content = CharacterUtils.replaceStr(content);
            if("测试".equals(content)){
                //1.查询物流轨迹
                resMsg.append("你输入的消息为测试...");
            }else{
                resMsg.append("感谢您的反馈，您的问题我们已收到，请稍候...");
            }
            message = TextMessageUtil.initMessage(messageText.getFromUserName(), messageText.getToUserName(), resMsg.toString());
            response.getWriter().write(message);
        } catch (Exception e) {
            log.error("处理用户文本消息异常：", e);
            //异常时直接回复空字符串 标识收到消息无需微信重推
            response.getWriter().write("处理用户文本消息异常：" + e.getMessage());
        }
    }

}
