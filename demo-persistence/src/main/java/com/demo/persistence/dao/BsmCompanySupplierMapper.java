package com.demo.persistence.dao;

import com.demo.model.bsmCompanySupplier;
import com.demo.model.bsmCompanySupplierKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BsmCompanySupplierMapper {
    int deleteByPrimaryKey(bsmCompanySupplierKey key);

    int insert(bsmCompanySupplier record);

    int insertSelective(bsmCompanySupplier record);

    bsmCompanySupplier selectByPrimaryKey(bsmCompanySupplierKey key);

    int updateByPrimaryKeySelective(bsmCompanySupplier record);

    int updateByPrimaryKey(bsmCompanySupplier record);

    List<bsmCompanySupplier> getCooperateSupplier(@Param("companyId") Long companyId );
}