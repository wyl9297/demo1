package com.demo.persistence.dao;

import com.demo.model.PurchasesWinRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchasesWinRecordMapper {

    /**
     * 查询 采购品的供应商历史成交价
     * @param directoryId 采购品id
     * @param companyId 采购商id
     * @param syncTime 同步时间  格式：2017-08-30 21:53:47
     * @return  selMaxAndMinPrice
     */
    List<PurchasesWinRecord> selWinPrices(@Param("directoryId") Long directoryId , @Param("companyId") Long companyId , @Param("syncTime") String syncTime);


}