package com.demo.controller;

import com.demo.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SupplierController {

    @Autowired
    @Qualifier("SupplierService")
    private SupplierService supplierService;

    @RequestMapping("/catalogRealtion")
    public String testSupplierCatalogRealtion(){
        String result = supplierService.handleSupplierCatalogRelation(1113172701L);
        return result;
    }


}
