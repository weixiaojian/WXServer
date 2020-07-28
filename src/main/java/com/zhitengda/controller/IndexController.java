package com.zhitengda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author langao_q
 * @create 2020-07-17 18:04
 */
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
}
