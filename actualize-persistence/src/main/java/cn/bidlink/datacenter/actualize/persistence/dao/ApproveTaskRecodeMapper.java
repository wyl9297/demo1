package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.ApproveTaskRecode;
import cn.bidlink.datacenter.actualize.model.ApproveTaskRecodeKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApproveTaskRecodeMapper {
    int deleteByPrimaryKey(ApproveTaskRecodeKey key);

    int insert(ApproveTaskRecode record);

    int insertSelective(ApproveTaskRecode record);

    ApproveTaskRecode selectByPrimaryKey(ApproveTaskRecodeKey key);

    int updateByPrimaryKeySelective(ApproveTaskRecode record);

    int updateByPrimaryKey(ApproveTaskRecode record);

    String concatSupplierId(Long companyId);

    List<ApproveTaskRecode> selTaskRecodeBySupplierIds(@Param(value = "supplierIds") String supplierIds,@Param(value = "companyId") Long companyId);
}