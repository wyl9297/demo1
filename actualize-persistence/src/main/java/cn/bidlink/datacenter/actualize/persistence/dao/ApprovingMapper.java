package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.Approving;
import cn.bidlink.datacenter.actualize.model.ApprovingKey;
import cn.bidlink.datacenter.actualize.model.BsmCompanySupplierApply;
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