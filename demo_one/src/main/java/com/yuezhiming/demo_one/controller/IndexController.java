package com.yuezhiming.demo_one.controller;/**
 * Created by ASUSon 2020/4/12 17:59
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: demo_one

 * @description:

 * @author: yuezm

 * @create: 2020-04-12 17:59
 **/
@Controller
public class IndexController {


    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
