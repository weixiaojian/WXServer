package com.zhitengda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author langao_q
 * @create 2020-07-17 17:55
 * 微信用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/auth")
    public String authorization(HttpServletRequest req, HttpServletResponse res,
                                   @RequestParam String code){
        try {
            //TODO saveOrUpdateUser
            return "index";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
