package com.zhitengda.controller;

import com.zhitengda.util.RetResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @RequestMapping("/auth")
    public RetResult authorization(HttpServletRequest req, HttpServletResponse res,
                                   @RequestParam String code){
        try {
            //TODO saveOrUpdateUser

            return RetResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return RetResult.fail(e.getMessage());
        }
    }

}
