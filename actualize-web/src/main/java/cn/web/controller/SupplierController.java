package cn.web.controller;

import cn.bidlink.datacenter.actualize.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
public class SupplierController {

    @Autowired(required = false)
    @Qualifier("SupplierService")
    private SupplierService supplierService;

    //迁移供应商主数据信息
    @RequestMapping("bsmToSupplier")
    @ResponseBody
    public String bsmToSupplier( HttpServletRequest request ,
                                 HttpServletResponse response ,
                                 @RequestParam("originCompanyId") Long originCompanyId ,
                                 @RequestParam("destCompanyId") Long destCompanyId ){
        String result = supplierService.bsmToSupplier(originCompanyId, destCompanyId);
        return result;
    }

    //迁移供应商准入记录
    @RequestMapping("/supplierAdmittanceRecord")
    public String SupplierAdmittanceRecord(@RequestParam("originCompanyId") Long originCompanyId ,
                                           @RequestParam("destCompanyId") Long destCompanyId ){
        String result = supplierService.handleSupplierAdmittanceRecode(originCompanyId,destCompanyId,new ArrayList<Long>());
        return result;
    }

    //迁移供应商采购目录关联关系
    @RequestMapping("/catalogRelationMigrate")
    @ResponseBody
    public String catalogRelationMigrate( HttpServletRequest request ,
                                               HttpServletResponse response ,
                                               @RequestParam("originCompanyId") Long originCompanyId ,
                                               @RequestParam("destCompanyId") Long destCompanyId ){
        String result = supplierService.catalogRelationMigrate( originCompanyId , destCompanyId );
        return result;
    }

    @RequestMapping("/handleApproveTaskRecode")
    public String handleApproveTaskRecode(@RequestParam("originCompanyId") Long originCompanyId ,
                                          @RequestParam("destCompanyId") Long destCompanyId){
        String result = supplierService.handleApproveTaskRecode(originCompanyId,destCompanyId);
        return result;
    }
}
