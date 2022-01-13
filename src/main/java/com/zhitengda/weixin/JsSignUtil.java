package com.zhitengda.weixin;

import cn.hutool.core.util.RandomUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信js签名验证类
 *
 * @author langao_q
 * @since 2020-07-13 14:52
 */
public class JsSignUtil {


    public static Map<String, String> sign(String url, String appId, String jsapi_ticket) throws UnsupportedEncodingException {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = RandomUtil.randomString(16);
        String timestamp = createTimestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序  
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", appId);
        String time = createTimestamp();
        ret.put("Istime", time);

        return ret;
    }

    /**
     * 随机加密
     *
     * @param hash
     * @return
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 由程序自己获取当前时间
     *
     * @return
     */
    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
