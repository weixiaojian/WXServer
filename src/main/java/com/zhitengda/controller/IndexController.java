package com.zhitengda.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * index控制类
 * @author langao_q
 * @since 2020-07-17 18:04
 */
@Slf4j
@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/index2")
    public String index2(){
        return "index2";
    }

    @RequestMapping("/auth")
    public String auth(){
        return "index3";
    }

    @RequestMapping("/error")
    public String error(String msg){
        log.error("----错误----" + msg);
        return "error";
    }
}
