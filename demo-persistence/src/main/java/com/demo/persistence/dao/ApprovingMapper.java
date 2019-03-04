package com.demo.persistence.dao;

import com.demo.model.Approving;
import com.demo.model.ApprovingKey;
import com.demo.model.BsmCompanySupplierApply;

import java.util.List;

public interface ApprovingMapper {
    int deleteByPrimaryKey(ApprovingKey key);

    int insert(Approving record);

    int insertSelective(Approving record);

    Approving selectByPrimaryKey(ApprovingKey key);

    int updateByPrimaryKeySelective(Approving record);

    int updateByPrimaryKey(Approving record);

    List<Approving> selApproveBySupplierIds(List<BsmCompanySupplierApply> supplierIds);
}