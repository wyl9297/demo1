package com.demo.persistence.dao;

import com.demo.model.CorpCatalog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  15:32 2019-02-20
 * @Description :
 */
public interface CorpCatalogsMapper {
    // 根据companyID查询corp_catalogs和corp_directorys信息
    public List<CorpCatalog> selAll(@Param("companyId") Long companyId);

    // 查询corp_catalogs表信息
    public List<CorpCatalog> selCataLogs(Long companyId);

}
