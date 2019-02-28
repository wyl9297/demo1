package com.demo.persistence.dao;

import com.demo.model.BsmCompanySupplierApply;
import com.demo.model.BsmCompanySupplierApplyKey;

public interface BsmCompanySupplierApplyMapper {
    int deleteByPrimaryKey(BsmCompanySupplierApplyKey key);

    int insert(BsmCompanySupplierApply record);

    int insertSelective(BsmCompanySupplierApply record);

    BsmCompanySupplierApply selectByPrimaryKey(BsmCompanySupplierApplyKey key);

    int updateByPrimaryKeySelective(BsmCompanySupplierApply record);

    int updateByPrimaryKey(BsmCompanySupplierApply record);
}