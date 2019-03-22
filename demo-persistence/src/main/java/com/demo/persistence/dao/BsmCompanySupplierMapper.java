package com.demo.persistence.dao;

import com.demo.model.BsmCompanySupplier;
import com.demo.model.BsmCompanySupplierKey;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BsmCompanySupplierMapper {
    int deleteByPrimaryKey(BsmCompanySupplierKey key);

    int insert(BsmCompanySupplier record);

    int insertSelective(BsmCompanySupplier record);

    BsmCompanySupplier selectByPrimaryKey(BsmCompanySupplierKey key);

    int updateByPrimaryKeySelective(BsmCompanySupplier record);

    int updateByPrimaryKey(BsmCompanySupplier record);

    List<BsmCompanySupplier> getCooperateSupplier(@Param("companyId") Long companyId );

    BsmCompanySupplier getSupplierInfo(@Param("companyId") Long companyId , @Param("supplierId") Long supplierId );

    @MapKey("supplierId")
    Map<Long , BsmCompanySupplier> getSupplierInfoList(@Param("companyId") Long companyId , @Param("supplierIds") List<Long> supplierIds );
}