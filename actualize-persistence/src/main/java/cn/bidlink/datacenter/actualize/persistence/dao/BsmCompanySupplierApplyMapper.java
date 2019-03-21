package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.BsmCompanySupplierApply;
import cn.bidlink.datacenter.actualize.model.BsmCompanySupplierApplyKey;

import java.util.List;

public interface BsmCompanySupplierApplyMapper {
    int deleteByPrimaryKey(BsmCompanySupplierApplyKey key);

    int insert(BsmCompanySupplierApply record);

    int insertSelective(BsmCompanySupplierApply record);

    BsmCompanySupplierApply selectByPrimaryKey(BsmCompanySupplierApplyKey key);

    int updateByPrimaryKeySelective(BsmCompanySupplierApply record);

    int updateByPrimaryKey(BsmCompanySupplierApply record);

    List<BsmCompanySupplierApply> selectByCompanyId(Long companyId);
}