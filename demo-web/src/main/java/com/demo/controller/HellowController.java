package com.demo.controller;

import com.demo.model.Company;
import com.demo.model.RegUser;
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

    @RequestMapping( value = "getRegUser")
    public RegUser getRegUser(HttpServletRequest request , HttpServletResponse response){
        //List<RegUser> regUsers = demoService.getRegUsers();
        RegUser regUser = demoService.getRegUser(1L);
        return regUser;
    }

    @RequestMapping( value = "getRegUserList")
    public List<RegUser> getRegUserList(HttpServletRequest request , HttpServletResponse response){
        List<RegUser> regUsers = demoService.getRegUsers();
        return regUsers;
    }

    @RequestMapping( value = "saveRegUser")
    public String saveRegUser(HttpServletRequest request , HttpServletResponse response){
        RegUser regUser = new RegUser();
        regUser.setName("炎林王");
        regUser.setSex("男");
        regUser.setLoginName("yanlinwang");
        regUser.setPassword("66666666666666666666666666666");
        regUser.setCompanyId(12345678L);
        regUser.setType(1);
        regUser.setGrade((short) 6);
        regUser.setStatus((short) 3);
        regUser.setWebType("CSPCS");
        regUser.setIsSubuser((short) 1);
        Long userId = demoService.saveUser(regUser);
        return userId.toString();
    }

}
