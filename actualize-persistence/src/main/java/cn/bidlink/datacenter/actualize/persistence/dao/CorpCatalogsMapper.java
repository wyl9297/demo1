package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.CorpCatalogs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  15:32 2019-02-20
 * @Description :
 */
public interface CorpCatalogsMapper {

    // 查询corp_catalogs表信息
    List<CorpCatalogs> selCataLogsByCompanyId(@Param("companyId") Long companyId);

    // 查询采购品目录总条数
    Long selCataLogsByCompanyIdWtihCount(@Param("companyId") Long companyId);

    // 查询corp_catalogs表信息
    List<CorpCatalogs> selCataLogsByCompanyIdWithPageing(@Param("companyId") Long companyId, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

}
