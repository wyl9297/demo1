package com.demo.persistence.dao;

import com.demo.model.CorpDirectorys;
import com.demo.model.CorpDirectorysKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CorpDirectorysMapper {
    int deleteByPrimaryKey(CorpDirectorysKey key);

    int insert(CorpDirectorys record);

    int insertSelective(CorpDirectorys record);

    CorpDirectorys selectByPrimaryKey(CorpDirectorysKey key);

    int updateByPrimaryKeySelective(CorpDirectorys record);

    int updateByPrimaryKey(CorpDirectorys record);

    List<CorpDirectorys> findByCompanyId(@Param("companyId") Long companyId);

    Long selCountByCompanyId(@Param("companyId") Long companyId);

    List<CorpDirectorys> findByCompanyIdWithPageing(@Param("companyId") Long companyId , @Param("startPage") Integer startPage , @Param("pageSize") Integer pageSize);
}