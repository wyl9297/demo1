package com.demo.service;

import java.util.Map;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:06 2019-02-20
 * @Description :
 */
public interface CorpExportService {

    // 采购品目录信息  及  中间表信息  处理
    Map<String, Object> exportCorpCatalogs(Long originCompanyId , Long destCompanyId);

    // 采购品信息 数据处理
    Map<String,Object> exportDirectorys(Long originCompanyId , Long destCompanyId);

    // 根据companyID查询中心库信息
    Map<String , Object> findByCondition(Long companyId);

    // 校验采购品信息是否符合条件
    Map<String , Object> checkCorp(Long originCompanyId , Long destCompanyId);

}
