package cn.bidlink.datacenter.actualize.persistence.dao;

import cn.bidlink.datacenter.actualize.model.PurchasesWinSupplierRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  14:17 2019-03-12
 * @Description :
 */
public interface PurchasesWinSupplierRecordMapper {

    /**
     * 查询 采购品的供应商成交记录
     * @param directoryId
     * @param companyId
     * @param syncTime
     * @return
     */
    List<PurchasesWinSupplierRecord> selQuotationHistory(@Param("directoryId") Long directoryId , @Param("companyId") Long companyId , @Param("syncTime") String syncTime);
}
