package com.zhitengda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 微信用户控制器
 * @author langao_q
 * @since 2020-07-17 17:55
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/auth")
    public String authorization(){
        try {
            return "index";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
