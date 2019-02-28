package com.demo.persistence.dao;

import com.demo.model.BsmSupplierCatalogRelation;
import com.demo.model.BsmSupplierCatalogRelationKey;

import java.util.List;

public interface BsmSupplierCatalogRelationMapper {
    int deleteByPrimaryKey(BsmSupplierCatalogRelationKey key);

    int insert(BsmSupplierCatalogRelation record);

    int insertSelective(BsmSupplierCatalogRelation record);

    BsmSupplierCatalogRelation selectByPrimaryKey(BsmSupplierCatalogRelationKey key);

    List<BsmSupplierCatalogRelation> selectByCompanyId(Long companyId);

    int updateByPrimaryKeySelective(BsmSupplierCatalogRelation record);

    int updateByPrimaryKey(BsmSupplierCatalogRelation record);
}