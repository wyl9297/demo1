package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.BsmSupplierCatalogRelation;
import cn.bidlink.datacenter.actualize.model.BsmSupplierCatalogRelationKey;

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