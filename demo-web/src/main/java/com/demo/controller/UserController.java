package com.demo.controller;

import com.demo.model.RegUser;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/4/2.
 */
@EnableAutoConfiguration
@RestController
public class UserController {

    @Autowired
    @Qualifier("UserService")
    private UserService userService;

    @RequestMapping( { "/login" } )
    public ModelAndView login(HttpServletRequest request , HttpServletResponse response,RegUser regUser){
        RegUser user = userService.checkLogin(regUser);
        ModelAndView modelAndView = new ModelAndView();
        if(user != null){
            modelAndView.setViewName("redirect:index");
        }else {
            modelAndView.setViewName("login");
            modelAndView.addObject("msg","用户名或密码错误");
        }
        return modelAndView;
    }

    @RequestMapping( { "/to_login" } )
    public ModelAndView to_login(HttpServletRequest request , HttpServletResponse response){
        return new ModelAndView("login");
    }
}
