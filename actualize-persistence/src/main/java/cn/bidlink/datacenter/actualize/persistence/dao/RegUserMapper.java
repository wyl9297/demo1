package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.RegUser;

public interface RegUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RegUser record);

    int insertSelective(RegUser record);

    RegUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RegUser record);

    int updateByPrimaryKey(RegUser record);

    RegUser findByCondition(RegUser regUser);
}