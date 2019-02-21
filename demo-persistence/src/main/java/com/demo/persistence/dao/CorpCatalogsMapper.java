package com.demo.persistence.dao;

import com.demo.model.CorpCatalog;
import com.demo.model.CorpCatalogNew;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  15:32 2019-02-20
 * @Description :
 */
public interface CorpCatalogsMapper {
    public List<CorpCatalog> selAll(Long companyId);
}
