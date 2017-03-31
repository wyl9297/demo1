package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Created by Administrator on 2017/3/28.
 */
@SpringBootApplication
@ServletComponentScan //spring能够扫描到我们自己编写的servlet和filter。
public class DemoApplication  {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }

}