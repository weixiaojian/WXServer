package com.zhitengda.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author langao_q
 * @since 2020-12-03 10:48
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 拦截器授权
     * @return
     */
    @RequestMapping("/auth2")
    public String auth(){
        return "index3";
    }
}
