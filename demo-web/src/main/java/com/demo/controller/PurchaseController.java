package com.demo.controller;

import com.demo.service.TransactionRecordService;
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
    @Qualifier("TransactionRecordService")
    private TransactionRecordService transactionRecordService;

    @RequestMapping("/priceMaxAndMin")
    public String priceMaxAndMin(Long directoryId , Long supplierId , String syncTime){
        String priceMaxAndMin = transactionRecordService.findPriceByParams(directoryId, supplierId , syncTime);
        return priceMaxAndMin;
    }

    @RequestMapping("/quotationHistory")
    public String quotationHistory(Long directoryId , Long supplierId , String syncTime){
        String priceHistory = transactionRecordService.selQuotationHistory(directoryId,supplierId,syncTime);
        return priceHistory;
    }
}
