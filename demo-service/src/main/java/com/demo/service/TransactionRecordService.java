package com.demo.service;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  17:52 2019-03-06
 * @Description : 采购项目供应商成交价
 */
public interface TransactionRecordService {

    /**
     * 查询采购品 的 供应商成交价[最高成交价、最低成交价]
     * @param directoryId 采购品id
     * @param supplierId 供应商id
     * @param syncTime 同步时间
     * @return
     */
    String findPriceByParams(Long directoryId , Long supplierId , String syncTime);

    /**
     * 查询采购品 的 供应商历史成交价
     * @param directoryId  采购品id
     * @param supplierId   供应商id
     * @param syncTime     同步时间
     * @return
     */
    String selQuotationHistory(Long directoryId , Long supplierId , String syncTime);
}
