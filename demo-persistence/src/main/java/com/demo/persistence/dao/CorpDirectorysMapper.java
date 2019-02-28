package com.demo.persistence.dao;

import com.demo.model.CorpDirectorys;
import com.demo.model.CorpDirectorysKey;

import java.util.List;

public interface CorpDirectorysMapper {
    int deleteByPrimaryKey(CorpDirectorysKey key);

    int insert(CorpDirectorys record);

    int insertSelective(CorpDirectorys record);

    CorpDirectorys selectByPrimaryKey(CorpDirectorysKey key);

    int updateByPrimaryKeySelective(CorpDirectorys record);

    int updateByPrimaryKey(CorpDirectorys record);

    List<CorpDirectorys> findByCondition(CorpDirectorys corpDirectorys);
}