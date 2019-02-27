package com.demo.service;

import com.demo.model.CorpDirectorys;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:06 2019-02-20
 * @Description :
 */
public interface CorpDirectorysService {
    List<CorpDirectorys> findByCondition(CorpDirectorys corpDirectorys);
}
