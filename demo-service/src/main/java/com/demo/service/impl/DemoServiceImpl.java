package com.demo.service.impl;

import com.demo.model.Company;
import com.demo.service.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */
@Service("DemoService")
public class DemoServiceImpl implements DemoService {

    @Override
    public List<Company> getCompanys(Long companyId) {
        List<Company> list = new ArrayList<>();
        for (int i = 0; i < 4 ; i++) {
            Company company = new Company();
            company.setName("必联" + i);
            company.setId(Long.valueOf(i));
            company.setType("1");
            list.add(company);
        }
        return list;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoServiceImpl.class,args);
    }
}