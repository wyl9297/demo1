package cn.bidlink.datacenter.actualize.service;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  17:52 2019-03-06
 * @Description : 采购项目供应商成交价
 */
public interface PurchasesService {

    /**
     * 查询采购品 的 供应商历史成交价
     * @param directoryId 采购品id
     * @param companyId 采购商id
     * @param syncTime 同步时间
     * @return
     */
    String selWinPrices(Long directoryId , Long companyId , String syncTime);

    /**
     * 查询采购品 的 历史成交供应商
     * @param directoryId  采购品id
     * @param companyId   采购商id
     * @param syncTime     同步时间
     * @return
     */
    String selQuotationHistory(Long directoryId , Long companyId , String syncTime);
}
