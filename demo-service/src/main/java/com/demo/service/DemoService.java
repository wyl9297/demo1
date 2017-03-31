package com.demo.service;

import com.demo.entity.Company;

import java.util.List;

public interface DemoService {
    List<Company> getCompanys(Long companyId);

}
