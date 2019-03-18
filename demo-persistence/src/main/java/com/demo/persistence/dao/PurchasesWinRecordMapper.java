package com.demo.persistence.dao;

import com.demo.model.PurchasesWinRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PurchasesWinRecordMapper {

    /**
     * 查询 采购品的供应商历史成交价
     * @param directoryId 采购品id
     * @param companyId 采购商id
     * @param syncTime 同步时间  格式：2017-08-30 21:53:47
     * @return  selMaxAndMinPrice
     */
    List<PurchasesWinRecord> selWinPrices(@Param("directoryId") Long directoryId , @Param("companyId") Long companyId , @Param("syncTime") String syncTime);

    /**
     * 查询供应商某个采购品的历史报价最大值和最小值
     * @param companyId
     * @param supplierId
     * @param directoryId
     * @return
     */
    Map<String,Long>  getDirectoryPriceTopAndLow(@Param("companyId") Long companyId , @Param("supplierId") Long supplierId , @Param("directoryId") Long directoryId);

}