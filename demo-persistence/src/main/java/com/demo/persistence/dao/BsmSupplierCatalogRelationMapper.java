package com.demo.persistence.dao;

import com.demo.model.BsmSupplierCatalogRelation;
import com.demo.model.BsmSupplierCatalogRelationKey;

public interface BsmSupplierCatalogRelationMapper {
    int deleteByPrimaryKey(BsmSupplierCatalogRelationKey key);

    int insert(BsmSupplierCatalogRelation record);

    int insertSelective(BsmSupplierCatalogRelation record);

    BsmSupplierCatalogRelation selectByPrimaryKey(BsmSupplierCatalogRelationKey key);

    int updateByPrimaryKeySelective(BsmSupplierCatalogRelation record);

    int updateByPrimaryKey(BsmSupplierCatalogRelation record);
}