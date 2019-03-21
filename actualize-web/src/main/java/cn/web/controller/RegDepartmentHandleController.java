package cn.web.controller;

import cn.bidlink.datacenter.actualize.model.RegDepartment;
import cn.bidlink.datacenter.actualize.persistence.dao.RegDepartmentMapper;
import cn.bidlink.datacenter.actualize.service.RegDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class RegDepartmentHandleController {

    @Autowired
    private RegDepartmentMapper regDepartmentMapper;

    @Autowired(required = false)
    @Qualifier("RegDepartmentService")
    private RegDepartmentService regDepartmentService;

    @RequestMapping("getRelationData")
    @ResponseBody
    private String getRelationData(HttpServletRequest request, HttpServletResponse response ,Long companyId){
        List<Map<String,String>> userRelation = regDepartmentMapper.getDepUserRelation(companyId);
        return userRelation.toString();
    }

    @RequestMapping("getDepartment")
    @ResponseBody
    private String getDepartment(HttpServletRequest request, HttpServletResponse response ,Long companyId){
        List<RegDepartment> department = regDepartmentMapper.getRegDepartmentByCompanyId(companyId);
        return department.toString();
    }

    @RequestMapping("handleRegDepartment")
    @ResponseBody
    private String handleRegDepartment(HttpServletRequest request, HttpServletResponse response , @RequestParam(value = "originCompanyId",required = true) Long originCompanyId
            , @RequestParam(value = "destCompanyId",required = true) Long destCompanyId){
        String handleRegDepartment = regDepartmentService.handleRegDepartment(originCompanyId, destCompanyId);
        return handleRegDepartment;
    }


}
