package com.zhitengda.interceptor;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @author langao_q
 * @create 2020-07-29 16:53
 */
@Slf4j
@Component
public class WxAuthInterceptor implements HandlerInterceptor{

    /**
     * appid
     */
    final static String appId = "wx6447c1fe6b20697b";

    /**
     * 获取用户数据类型
     */
    final static String SNSSCOPE_USERINFO = "snsapi_userinfo";

    /**
     * 微信oath2授权地址;通过该地址拿到用户授权code码
     */
    static final String oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize";

    final static String exclusions [] = {"/forbid"};

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        log.info(req.getRequestURI());
        //code只能使用一次，5分钟未被使用自动过期
        String code = req.getParameter("code");
        String uri = req.getRequestURI();
        String callback = req.getContextPath() + "/callback";
        //如果请求uri不相等 说明未拿到code 需要去授权
        if(req.getSession().getAttribute("auth") != null){
            return true;
        }else if (!uri.equals(req.getContextPath()+"/callback")) {
            String mainUrl = "http://" + req.getServerName() + req.getContextPath();
            String rurl = mainUrl + req.getServletPath();
            if (req.getQueryString() != null) {
                rurl += "?" + req.getQueryString();
            }
            String callbackUrl = getRedirectUrl(req, rurl);
            String _url = oauth_url + "?appid=" + appId + "&redirect_uri=" + URLEncoder.encode(callbackUrl) + "&response_type=code&scope="+SNSSCOPE_USERINFO+"&state=STATE#wechat_redirect";
            res.sendRedirect(_url);
            return false;
        }else{
            String rUrl = req.getParameter("rUrl");
            rUrl = StrUtil.isBlank("rUrl") ? "/index" : rUrl;

            String error = req.getParameter("error");
            String error_description = req.getParameter("error");
            if (StrUtil.isNotBlank(error)) {
                //TODO 跳转错误页面
            } else if(StrUtil.isBlank(code)){
                //TODO 用户取消授权
            } else {
                log.info("----保存用户数据------" + code);
                //增加session
                req.getSession().setAttribute("auth", "bingo");
                //根据拿到的code作为参数，发送post请求，拿access_token,openId
                res.sendRedirect(rUrl);
                return false;
            }
        }
        return true;
    }

    /**
     * 获取项目重定向地址
     * @param req
     * @param rUrl
     * @return
     */
    protected static String getRedirectUrl(HttpServletRequest req, String rUrl) {
        String mainUrl = "http://" + req.getServerName()+ req.getContextPath();//微信只支持80端口  增加 request.getContextPath()  20171213
        return mainUrl + "/callback" + "?rUrl=" + (StrUtil.isNotBlank(rUrl) ? URLEncoder.encode(rUrl) : "");
    }

    /*@Override
    public void intercept(Invocation ai) {

        Controller controller = ai.getController();
        for(String exclu : exclusions){
            if((controller.getRequest().getContextPath()+exclu).equals(uri)){
                //	if(exclu.equals(uri)){
                ai.invoke();
                return;
            }
        }

        doIntercept(ai);

    }

    protected abstract void doIntercept(Invocation inv);

    protected String getAuthAppId(HttpServletRequest request){
        if(PropKit.getBoolean("jfinal.devmode")){
            return PropKit.get("authAppId");
        }
        return getAuthUser(request).getAppId();
    }

    protected AuthUser getAuthUser(HttpServletRequest request){
        String serverName = request.getServerName();
        serverName = serverName.substring(0, serverName.indexOf("."));
        AuthUser authUser = (AuthUser) request.getSession().getAttribute(Constants.APPUSER_IN_SESSION);
        if(authUser == null){
            authUser = AuthUser.dao.findFirst("select * from " + AuthUser.table + " where app_id=? and active=1 ", serverName);
            request.getSession().setAttribute(Constants.APPUSER_IN_SESSION, authUser);
        }
        return authUser;
    }

    protected String getCompTicket(){
        CompTicket compTicket = CompTicket.dao.findFirst("select * from " + CompTicket.table);
        return compTicket == null ? "" : compTicket.getCompVerifyTicket();
    }*/
}
