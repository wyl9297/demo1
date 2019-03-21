package cn.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Administrator on 2017/3/28.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan //spring能够扫描到我们自己编写的servlet和filter。
@ImportResource("classpath:application-dubbo.xml")
public class DemoApplication  {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }

}