package com.zhitengda.config;

import com.zhitengda.interceptor.WxAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置类
 *
 * @author langao_q
 * @since 2020-07-29 17:33
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private WxAuthInterceptor wxAuthInterceptor;

    /**
     * 配置微信授权拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(wxAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error/**", "/msg/**",  "/index/**", "/js/**");
    }

    /**
     * 前后端分离：跨域问题解决
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 所有的当前站点的请求地址，都支持跨域访问。
        registry.addMapping("/**")
                // 当前站点支持的跨域请求类型是什么。
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // 是否支持跨域用户凭证
                .allowCredentials(true)
                // 所有的外部域都可跨域访问。 如果是localhost则很难配置，因为在跨域请求的时候，外部域的解析可能是localhost、127.0.0.1、主机名
                .allowedOrigins("*")
                // 超时时长设置为1小时。 时间单位是秒。
                .maxAge(60);
    }
}
