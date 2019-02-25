package com.demo.persistence.dao;

import com.demo.model.RegUser;

public interface RegUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RegUser record);

    int insertSelective(RegUser record);

    RegUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RegUser record);

    int updateByPrimaryKey(RegUser record);

    RegUser findByCondition(RegUser regUser);
}