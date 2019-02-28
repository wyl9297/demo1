package com.demo.service;

public interface SupplierService {

    public String handleSupplierCatalogRelation(Long company);

    String bsmToSupplier( Long originCompanyId , Long destCompanyId );

}
