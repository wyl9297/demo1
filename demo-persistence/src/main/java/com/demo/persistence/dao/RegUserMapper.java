package com.demo.persistence.dao;

import com.demo.model.RegUser;

import java.util.List;

public interface RegUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RegUser record);

    int insertSelective(RegUser record);

    RegUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RegUser record);

    int updateByPrimaryKey(RegUser record);

    List<RegUser> getRegUsers();
}