package com.demo.controller;

import com.demo.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SupplierController {

    @Autowired
    @Qualifier("SupplierService")
    private SupplierService supplierService;

    @RequestMapping("bsmToSupplier")
    @ResponseBody
    public String bsmToSupplier( HttpServletRequest request ,
                                 HttpServletResponse response ,
                                 @RequestParam("originCompanyId") Long originCompanyId ,
                                 @RequestParam("destCompanyId") Long destCompanyId ){
        String result = supplierService.bsmToSupplier(originCompanyId, destCompanyId);
        return result;
    }

    @RequestMapping("/catalogRealtion")
    public String testSupplierCatalogRealtion(){
        String result = supplierService.handleSupplierCatalogRelation(1113172701L);
        return result;
    }


}