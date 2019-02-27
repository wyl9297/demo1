package com.demo.service;

import cn.bidlink.usercenter.server.entity.TRegUser;

import java.util.List;
import java.util.Map;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  16:06 2019-02-20
 * @Description :
 */
public interface DataMigrationService {
    // 采购品目录信息  及  中间表信息  导出
    public Map<String, Object> exportCorpCatalogs(Long companyId);

    // 导入Excel(暂未用)
    public Map<String, Object> importExcel();

    //
    public Map<String,Object> exportDirectorys(Long companyId);

    // 根据companyID查询中心库信息
    public List<TRegUser> findByCondition(Long companyId);
}
