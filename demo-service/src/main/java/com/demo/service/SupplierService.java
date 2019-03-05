package com.demo.service;

import java.util.List;

public interface SupplierService {

    public String handleSupplierAdmittanceRecode(Long company, List<Long> list);

    String bsmToSupplier( Long originCompanyId , Long destCompanyId );


    public String handleApproveTaskRecode(Long originCompanyId,Long destCompanyId);
    String catalogRelationMigrate( Long originCompanyId , Long destCompanyId );

}
