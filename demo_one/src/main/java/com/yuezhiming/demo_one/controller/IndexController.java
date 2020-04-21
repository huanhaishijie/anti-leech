package com.yuezhiming.demo_one.controller;/**
 * Created by ASUSon 2020/4/12 17:59
 */

import com.yuezhiming.demo_one.annotation.ExtApiIdempotent;
import com.yuezhiming.demo_one.commons.ConstantUtils;
import com.yuezhiming.demo_one.commons.RedisTokenUtil;
import com.yuezhiming.demo_one.commons.TokenUtil;
import com.yuezhiming.demo_one.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: demo_one

 * @description:

 * @author: yuezm

 * @create: 2020-04-12 17:59
 **/
@Controller
public class IndexController {

    @Autowired
    IndexService indexService;

    @Autowired
    RedisTokenUtil redisTokenUtil;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/getToken")
    public String getToken(){
        return TokenUtil.getToken();
    }
    @RequestMapping(value = "/addInfo", method = RequestMethod.POST)
    public String addInfo(@RequestParam Map<String, Object> param, HttpServletRequest request){
        String token = request.getHeader("token");
        if(!TokenUtil.tokenExist(token)) throw new RuntimeException("不能重复提交");
        return null;
    }

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    @ExtApiIdempotent(value = ConstantUtils.EXTAPIFORM)
    public Object addUser(@RequestParam Map<String, Object> param){
        System.out.println(param);
        return null;
    }
}
