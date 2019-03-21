package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.RegDepartment;
import cn.bidlink.datacenter.actualize.model.RegDepartmentKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RegDepartmentMapper {
    int deleteByPrimaryKey(RegDepartmentKey key);

    int insert(RegDepartment record);

    int insertSelective(RegDepartment record);

    RegDepartment selectByPrimaryKey(RegDepartmentKey key);

    int updateByPrimaryKeySelective(RegDepartment record);

    int updateByPrimaryKey(RegDepartment record);

    List<Map<String,String>> getDepUserRelation(@Param("companyId") Long companyId);

    List<RegDepartment> getRegDepartmentByCompanyId(@Param("companyId") Long companyId);
}