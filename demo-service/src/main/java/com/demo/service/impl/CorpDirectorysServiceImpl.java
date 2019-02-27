package com.demo.service.impl;

import com.demo.model.CorpDirectorys;
import com.demo.persistence.dao.CorpDirectorysMapper;
import com.demo.persistence.dao.RegUserMapper;
import com.demo.service.CorpDirectorysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:07 2019-02-20
 * @Description :
 */
@Service("CorpDirectorysService")
public class CorpDirectorysServiceImpl implements CorpDirectorysService {

    private static Logger log = LoggerFactory.getLogger(CorpDirectorysServiceImpl.class);
    @Autowired
    private CorpDirectorysMapper corpDirectorysMapper;
    @Autowired
    private RegUserMapper regUserMapper;


    @Override
    public List<CorpDirectorys> findByCondition(CorpDirectorys corpDirectorys) {
        List<CorpDirectorys> byCondition = corpDirectorysMapper.findByCondition(corpDirectorys);
        return byCondition;
    }
}
