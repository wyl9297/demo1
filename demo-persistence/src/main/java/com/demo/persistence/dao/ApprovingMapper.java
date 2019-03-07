package com.demo.persistence.dao;

import com.demo.model.Approving;
import com.demo.model.ApprovingKey;
import com.demo.model.BsmCompanySupplierApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApprovingMapper {
    int deleteByPrimaryKey(ApprovingKey key);

    int insert(Approving record);

    int insertSelective(Approving record);

    Approving selectByPrimaryKey(ApprovingKey key);

    int updateByPrimaryKeySelective(Approving record);

    int updateByPrimaryKey(Approving record);

    List<Approving> selApproveBySupplierIds(List<BsmCompanySupplierApply> supplierIds);

    List<Approving> selApprovingByProcInstanceIds(@Param(value = "procInstanceIds") String procInstanceIds, @Param(value = "companyId") Long companyId);
}