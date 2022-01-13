package com.zhitengda.weixin;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>ClassUtils<／p>
 *
 * @author wst
 * @date 2018年11月8日
 * 微信发送模板消息类
 */
@Slf4j
@Component
public class TemplateMsgUtil {

    /**
     * 发送模板消息
     *
     * @param openId   用户openid
     * @param content  内容
     * @param billCode 运单号
     * @return 发送结果
     */
    public static String sendMsg(String openId, String content, String billCode, String accessToken) {
        if (openId == null || content == null || openId.equals("") || content.equals("")) {
            return "发送内容参数不全！";
        } else {
            log.info("发送模板消息开始：【openId：】：" + openId + "\t【content】：" + content + "\t【billCode】：" + billCode);
            String str = " {" +
                    "           \"touser\":\"" + openId + "\"," +
                    "           \"template_id\":\"peaVpld-GClemLN97pSGf6uhIoa3In2MHcTMhYiZs9c\"," +
                    "           \"topcolor\":\"#FF0000\"," +
                    "           \"data\":{" +
                    "                   \"orderNumber\": {" +
                    "                       \"value\":\"" + billCode + "\"," +
                    "                       \"color\":\"#173177\"" +
                    "                   }," +
                    "                   \"status\": {" +
                    "                       \"value\":\"" + content + "\"," +
                    "                       \"color\":\"#173177\"" +
                    "                   }," +
                    "                   \"remark\": {" +
                    "                       \"value\":\"" + "" + "\"," +
                    "                       \"color\":\"#173177\"" +
                    "                   }" +
                    "           }" +
                    "       }";
            String res = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken, str);
            log.info("发送模板消息结束：【res：】：" + res);
            return res;
        }
    }
}
