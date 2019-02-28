package com.demo.persistence.dao;

import com.demo.model.CorpCatalogs;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  15:32 2019-02-20
 * @Description :
 */
public interface CorpCatalogsMapper {

    // 查询corp_catalogs表信息
    public List<CorpCatalogs> selCataLogsByCompanyId(Long companyId);

}
