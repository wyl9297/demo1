package com.demo.controller;

import com.demo.service.PurchasesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  17:58 2019-03-06
 * @Description :
 */
@Controller
public class PurchaseController {

    @Autowired
    @Qualifier("PurchasesService")
    private PurchasesService purchasesService;

    @RequestMapping("/priceHistory")
    public String priceMaxAndMin(@Param("directoryId") Long directoryId , @Param("companyId") Long companyId ,@Param("syncTime") String syncTime){
        String priceMaxAndMin = purchasesService.selWinPrices(directoryId, companyId , syncTime);
        return priceMaxAndMin;
    }

    @RequestMapping("/quotationHistory")
    public String quotationHistory(@Param("directoryId") Long directoryId ,@Param("companyId") Long companyId , @Param("syncTime") String syncTime){
        String priceHistory = purchasesService.selQuotationHistory(directoryId,companyId,syncTime);
        return priceHistory;
    }
}
