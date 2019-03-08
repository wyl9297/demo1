package com.demo.persistence.dao;

import com.demo.model.TransactionHistoryRecord;
import com.demo.model.TransactionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TransactionRecord record);

    int insertSelective(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TransactionRecord record);

    int updateByPrimaryKey(TransactionRecord record);

    /**
     * 查询 采购品的供应商历史最高最低成交价
     * @param directoryId 采购品id
     * @param supplierId 供应商id
     * @param syncTime 同步时间  格式：2017-08-30 21:53:47
     * @return
     */
    List<TransactionRecord> selMaxAndMinPrice(@Param("directoryId") Long directoryId , @Param("supplierId") Long supplierId , @Param("syncTime") String syncTime);

    /**
     * 查询采购品的供应商历史报价记录
     * @param directoryId
     * @param supplierId
     * @param syncTime
     * @return
     */
    List<TransactionHistoryRecord> selQuotationHistory(@Param("directoryId") Long directoryId , @Param("supplierId") Long supplierId , @Param("syncTime") String syncTime);
}