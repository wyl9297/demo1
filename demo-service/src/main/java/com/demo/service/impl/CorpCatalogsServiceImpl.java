package com.demo.service.impl;

import com.demo.model.CorpCatalog;
import com.demo.persistence.dao.CorpCatalogsMapper;
import com.demo.service.CorpCatalogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:07 2019-02-20
 * @Description :
 */
@Service("CorpCatalogsService")
public class CorpCatalogsServiceImpl implements CorpCatalogsService {

    @Autowired
    private CorpCatalogsMapper corpCatalogsMapper;

    @Override
    public List<CorpCatalog> selAll() {
        List<CorpCatalog> corpCatalogNews = corpCatalogsMapper.selAll(111l);
        return corpCatalogNews;
    }
}
