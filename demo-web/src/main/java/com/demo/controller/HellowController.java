package com.demo.controller;

import cn.bidlink.base.ServiceResult;
import cn.bidlink.procurement.materials.dal.server.entity.CorpCatalogs;
import cn.bidlink.procurement.materials.dal.server.service.DubboCorpCatalogsService;
import com.demo.model.Company;
import com.demo.persistence.dao.RegDepartmentMapper;
import com.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/3/28.
 */
@EnableAutoConfiguration
@RestController
public class HellowController {

    @Autowired
    @Qualifier("DemoService")
    private DemoService demoService;

    @Autowired
    private DubboCorpCatalogsService dubboCorpCatalogsService;

    @Autowired
    @Qualifier("aclJdbcTemplate")
    protected JdbcTemplate aclJdbcTemplate;

    @Autowired
    @Qualifier("uniregJdbcTemplate")
    protected JdbcTemplate uniregJdbcTemplate;

    @Autowired
    private RegDepartmentMapper regDepartmentMapper;

    @RequestMapping( value = "success")
    public ModelAndView success(HttpServletRequest request , HttpServletResponse response){
        int i = 0;
        CorpCatalogs corpCatalogs = new CorpCatalogs();
        corpCatalogs.setId(283248124493824000L);
        corpCatalogs.setCompanyId(1113172744L);
        ServiceResult<Integer> delete = dubboCorpCatalogsService.delete(corpCatalogs);
        /*System.out.println(delete.getResult());
        while ( i < 100 ){
            System.out.println(IdWork.nextId());
            i ++;
        }*/
        return null;
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

    @RequestMapping( value = "testMutilDataSource")
    public String testMutilDataSource( HttpServletRequest request , HttpServletResponse response ){
        aclJdbcTemplate.queryForMap("select ORGNAME FROM SYS_ORG WHERE ORGID = 100");
        uniregJdbcTemplate.queryForMap("select create_user_name FROM supplier WHERE ID = 174957304234377216");
        regDepartmentMapper.getDepUserRelation(1113172701L);
        return "success";
    }

}
