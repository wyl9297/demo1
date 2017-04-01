package com.demo.persistence.dao;

import com.demo.model.BmpfjzProject;

public interface BmpfjzProjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BmpfjzProject record);

    int insertSelective(BmpfjzProject record);

    BmpfjzProject selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BmpfjzProject record);

    int updateByPrimaryKey(BmpfjzProject record);
}