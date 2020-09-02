package com.zhitengda.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhitengda.config.WxConfig;
import com.zhitengda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信授权获取用户信息拦截器 <br>
 * 第一步：获取code <br>
 * 第二步：根据code获取用户的openid和access_token <br>
 * 第三步：根据openid和access_token获取用户的基本信息 <br>
 * 附：刷新access_token、检验授权凭证（access_token）是否有效
 * @author langao_q
 * @since 2020-07-29 16:53
 */
@Slf4j
@Component
public class WxAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private UserService userService;

    /**
     * 授权后获取用户基本信息
     */
    final static String USER_IFNO = "userInfo";

    /**
     * 获取用户数据类型
     */
    final static String SNSSCOPE_USERINFO = "snsapi_userinfo";

    /**
     * 微信oath2授权地址;通过该地址拿到用户授权code码
     */
    final static String OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

    /**
     * 根据拿到的code获取openId以及access_token
     */
    final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 授权后获取用户基本信息
     */
    final static String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";


    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        //code只能使用一次，5分钟未被使用自动过期
        String code = req.getParameter("code");
        String uri = req.getRequestURI();

        try {
            if (req.getSession().getAttribute(USER_IFNO) != null) {
                //如果session中已经有用户信息 说明已经授权过了直接放行
                return true;
            } else if (!uri.equals(req.getContextPath() + "/callback")) {
                //如果请求uri不相等 说明未拿到code 需要去授权
                String rurl = "http://" + req.getServerName() + req.getContextPath() + req.getServletPath();
                if (req.getQueryString() != null) {
                    rurl += "?" + req.getQueryString();
                }
                String callbackUrl = getRedirectUrl(req, rurl);
                String _url = OAUTH_URL + "?appid=" + wxConfig.getAppId() + "&redirect_uri=" + URLEncoder.encode(callbackUrl, "UTF-8") + "&response_type=code&scope=" + SNSSCOPE_USERINFO + "&state=STATE#wechat_redirect";
                res.sendRedirect(_url);
                return false;
            } else {
                //已经授权过 可以拿到code去获取openid和用户数据
                String rUrl = req.getParameter("rUrl");
                rUrl = StrUtil.isBlank("rUrl") ? "/index" : rUrl;
                log.info("-----第一步：获取code-----" + code);

                String redirectUrl = getRedirectUrl(req, rUrl);
                Map<String, Object> mapOpenid = new HashMap<String, Object>();
                mapOpenid.put("code", code);
                mapOpenid.put("appid", wxConfig.getAppId());
                mapOpenid.put("secret", wxConfig.getAppSecret());
                mapOpenid.put("redirect_uri", rUrl);
                mapOpenid.put("view", "web");
                mapOpenid.put("grant_type", "authorization_code");
                String resultOpenid = HttpUtil.post(ACCESS_TOKEN_URL, mapOpenid);

                JSONObject objOpenid = JSONUtil.parseObj(resultOpenid);
                 /*{
                      "access_token":"ACCESS_TOKEN",
                      "expires_in":7200,
                      "refresh_token":"REFRESH_TOKEN",
                      "openid":"OPENID",
                      "scope":"SCOPE"
                    }*/
                log.info("-----第二步：获取openid-----" + objOpenid);
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("access_token", objOpenid.getStr("access_token"));
                userMap.put("openid", objOpenid.getStr("openid"));
                userMap.put("lang", "zh_CN");
                String resultUser = HttpUtil.get(USERINFO_URL, userMap);

                JSONObject objUser = JSONUtil.parseObj(resultUser);
                /*{
                  "openid":" OPENID",
                  "nickname": NICKNAME,
                  "sex":"1",
                  "province":"PROVINCE",
                  "city":"CITY",
                  "country":"COUNTRY",
                  "headimgurl":       "http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
                  "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
                  "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
                }*/
                log.info("-----第三步：保存/更新用户信息-----" + objUser);
                //添加session
                req.getSession().setAttribute(USER_IFNO, objUser);
                //保存/更新用户信息后，重定向到业务地址
                res.sendRedirect(rUrl);
                return false;
            }
        } catch (Exception e) {
            log.error("-----授权错误-----"+  e);
            req.getSession().setAttribute("errorMgs", e);
            res.sendRedirect(req.getContextPath() + "/error");
            return false;
        }
    }

    /**
     * 获取项目重定向地址
     *
     * @param req  request对象
     * @param rUrl 当前请求的url（需要保存下来）
     * @return 跳转的url
     */
    protected static String getRedirectUrl(HttpServletRequest req, String rUrl) throws UnsupportedEncodingException {
        String mainUrl = "http://" + req.getServerName() + req.getContextPath();
        return mainUrl + "/callback" + "?rUrl=" + (StrUtil.isNotBlank(rUrl) ? URLEncoder.encode(rUrl, "UTF-8") : "");
    }

}
