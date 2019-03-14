package com.demo.service.impl;

import cn.bidlink.framework.util.gen.IdWork;
import com.demo.model.PurchasesWinSupplierRecord;
import com.demo.model.PurchasesWinRecord;
import com.demo.persistence.dao.PurchasesWinRecordMapper;
import com.demo.persistence.dao.PurchasesWinSupplierRecordMapper;
import com.demo.service.PurchasesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  17:52 2019-03-06
 * @Description : 成交价记录
 */
@Service("PurchasesService")
public class PurchasesServiceImpl implements PurchasesService {

    private static final Logger log = LoggerFactory.getLogger(PurchasesServiceImpl.class);

    @Autowired
    @Qualifier("purchaseJdbcTemplate")
    protected JdbcTemplate purchaseJdbcTemplate;

    @Autowired
    private PurchasesWinRecordMapper transactionRecordMapper;

    @Autowired
    private PurchasesWinSupplierRecordMapper purchasesWinSupplierRecordMapper;

    /**
     * 采购品 供应商 历史成交价记录迁移
     * @param directoryId 采购品id
     * @param supplierId
     * @param syncTime 同步时间
     * @return
     */
    @Override
    public String selWinPrices(Long directoryId , Long companyId , String  syncTime){
        String insertSQL = "insert into purchases_win_price_record(id,project_id,company_id,supplier_id," +
                "directory_id  , deal_price ,  win_time ,   sync_time) values( ? , ? , ? ,? ,? ,? , ? , ?)";
        List<PurchasesWinRecord> transactionRecords = transactionRecordMapper.selWinPrices(directoryId, companyId,null);
        List<Object[]> params = new ArrayList<>();
        if( transactionRecords.size() == 0 || transactionRecords == null){
            log.error("--------没有查询到数据，请校验参数------------");
            log.info("采购品id：{},供应商id：{}",directoryId,companyId);
            throw new RuntimeException("查询供应商最高成交价、最低成交价失败");
        }else{
            for(PurchasesWinRecord record :transactionRecords){
                if(record.getDealPrice() == null){
                    record.setDealPrice(0.0);
                }
                params.add(new Object[]{
                        IdWork.nextId(), record.getProjectId(), record.getCompanyId(), record.getSupplierId(),
                        record.getDirectoryId(), record.getDealPrice() ,   record.getWinTime() , new Date()});
            }
            int[] result = purchaseJdbcTemplate.batchUpdate(insertSQL, params);
            if( result.length <= 0 ){
                log.error("数据插入失败，请检查数据和SQL的准确性");
                throw new RuntimeException("数据插入失败，请检查数据和SQL的准确性");
            }else{
                return "success";
            }
        }
    }

    /**
     * 采购品 历史成交供应商记录迁移
     * @param directoryId  采购品id
     * @param companyId   采购商id
     * @param syncTime     同步时间
     * @return
     */
    @Override
    public String selQuotationHistory(Long directoryId, Long companyId, String syncTime) {
        List<PurchasesWinSupplierRecord> transactionHistoryRecords = purchasesWinSupplierRecordMapper.selQuotationHistory(directoryId, companyId, null);
        List<Object[]> params = new ArrayList<>();
        String insertSql = "INSERT INTO `db_boot`.`purchases_win_supplier_record`(`id`, `directory_id`, `company_id`, `supplier_id`, `win_time` , `sync_time`)" +
                "VALUES (?, ?, ?, ?, ? , ?)\n";
        if(transactionHistoryRecords.size()<=0){
            log.error("查询数据失败，请检验参数：");
        }else{
            for (PurchasesWinSupplierRecord thr:transactionHistoryRecords) {
                params.add(new Object[]{
                        IdWork.nextId() ,thr.getDirectoryId() , thr.getCompanyId() , thr.getSupplierId() , thr.getWinTime() , new Date()});
            }
        }
        int[] result = purchaseJdbcTemplate.batchUpdate(insertSql, params);
        if( result.length <=0 ){
            log.error("------------数据插入失败，请检查-------------");
            throw new RuntimeException("供应商成交价历史记录数据插入失败，请检查");
        }else{
            log.info("数据插入成功");
            return "success";
        }
    }
}
