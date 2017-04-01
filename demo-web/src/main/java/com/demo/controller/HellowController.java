package com.demo.controller;

import com.demo.model.Company;
import com.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */
@EnableAutoConfiguration
@RestController
public class HellowController {

    @Autowired
    @Qualifier("DemoService")
    private DemoService demoService;

    @RequestMapping( value = "success")
    public ModelAndView success(HttpServletRequest request , HttpServletResponse response){
        List<Company> list = demoService.getCompanys(0L);
        ModelAndView modelAndView = new ModelAndView("success");
        modelAndView.addObject("names","yanlinwang");
        modelAndView.addObject("list",list);
        return modelAndView;
    }

    @RequestMapping( value = "returnJson")
    public Company returnJson(HttpServletRequest request , HttpServletResponse response){
        Company company = new Company();
        company.setName("百度");
        company.setId(66666L);
        return company;
    }

    @RequestMapping( value = "returnJsp")
    public ModelAndView returnJsp(HttpServletRequest request , HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hellow");
        return modelAndView;
    }
}
