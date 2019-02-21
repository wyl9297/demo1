package com.demo.controller;

import com.demo.model.CorpCatalog;
import com.demo.service.CorpCatalogsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:05 2019-02-20
 * @Description :
 */
@Controller
public class CorpExportController {

    private static Logger log = LoggerFactory.getLogger(CorpExportController.class);
    @Autowired
    @Qualifier("CorpCatalogsService")
    private CorpCatalogsService corpCatalogsService;

    @RequestMapping("/test")
    public void test(){
        List<CorpCatalog> corpCatalogs = corpCatalogsService.selAll();
        log.info("console:{}",corpCatalogs);
    }
}
