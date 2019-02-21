package com.demo.persistence.dao;

import com.demo.model.CorpDirectorys;
import com.demo.model.CorpDirectorysKey;

public interface CorpDirectorysMapper {
    int deleteByPrimaryKey(CorpDirectorysKey key);

    int insert(CorpDirectorys record);

    int insertSelective(CorpDirectorys record);

    CorpDirectorys selectByPrimaryKey(CorpDirectorysKey key);

    int updateByPrimaryKeySelective(CorpDirectorys record);

    int updateByPrimaryKey(CorpDirectorys record);
}