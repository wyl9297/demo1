package cn.bidlink.datacenter.actualize.service;

import cn.bidlink.datacenter.actualize.model.Company;

import java.util.List;

public interface DemoService {
    List<Company> getCompanys(Long companyId);

}
