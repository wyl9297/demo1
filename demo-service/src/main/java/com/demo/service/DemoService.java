package com.demo.service;

import com.demo.model.Company;
import com.demo.model.RegUser;

import java.util.List;

public interface DemoService {
    List<Company> getCompanys(Long companyId);

    List<RegUser> getRegUsers();

    RegUser getRegUser(Long userId);

    Long saveUser(RegUser regUser);
}
