package cn.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MigrateIndex {


    @RequestMapping("/")
    public ModelAndView toMigrate(HttpServletRequest request , HttpServletResponse response){
        ModelAndView migrate = new ModelAndView("migrate");
        return migrate;
    }

}
