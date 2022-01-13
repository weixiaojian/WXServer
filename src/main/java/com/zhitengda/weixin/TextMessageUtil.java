package com.zhitengda.weixin;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.thoughtworks.xstream.XStream;
import com.zhitengda.entity.MessageText;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 微信消息处理工具类
 * @author langao_q
 * @since 2021-09-07 17:27
 */
public class TextMessageUtil {

    /**
     * 收取用户发送的文本消息内容
     * 将request中的xml提取出来 转换为map
     * @param request
     * @return
     */
    public static MessageText xmlToMap(HttpServletRequest request){
        JSONObject result = new JSONObject();
        SAXReader reader = new SAXReader();
        InputStream in = null;
        try {
            in = request.getInputStream();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element element : list) {
                result.putOpt(element.getName(), element.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JSONUtil.toBean(result, MessageText.class);
    }

    /**
     * 回复用户文本消息
     * 封装发送消息对象,封装时，需要将调换发送者和接收者的关系
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    public static String initMessage(String FromUserName, String ToUserName, String content) {
        MessageText text = new MessageText();
        text.setToUserName(FromUserName);
        text.setFromUserName(ToUserName);
        text.setContent(content);
        text.setCreateTime(System.currentTimeMillis());
        text.setMsgType("text");

        XStream xstream  = new XStream();
        xstream.alias("xml", text.getClass());
        return xstream.toXML(text);
    }

    /**
     * 回复用户图片消息
     * 封装发送消息对象,封装时，需要将调换发送者和接收者的关系
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    public static String initImgMessage(String FromUserName, String ToUserName, String media_id) {
        String str = "<xml>" +
                "    <ToUserName>"+FromUserName+"</ToUserName>" +
                "    <FromUserName>"+ToUserName+"</FromUserName>" +
                "    <CreateTime>"+System.currentTimeMillis()+"</CreateTime>" +
                "    <MsgType>image</MsgType>" +
                "    <Image>" +
                "        <MediaId>"+media_id+"</MediaId>" +
                "    </Image>" +
                "</xml>";
        return str;
    }
}
