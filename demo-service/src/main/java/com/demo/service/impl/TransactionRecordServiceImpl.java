package com.demo.service.impl;

import cn.bidlink.framework.util.gen.IdWork;
import com.demo.model.TransactionHistoryRecord;
import com.demo.model.TransactionRecord;
import com.demo.persistence.dao.TransactionRecordMapper;
import com.demo.service.TransactionRecordService;
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
@Service("TransactionRecordService")
public class TransactionRecordServiceImpl implements TransactionRecordService {

    private static final Logger log = LoggerFactory.getLogger(TransactionRecordServiceImpl.class);

    @Autowired
    @Qualifier("purchaseJdbcTemplate")
    protected JdbcTemplate purchaseJdbcTemplate;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    public String findPriceByParams(Long directoryId , Long supplierId , String  syncTime){
        List<TransactionRecord> transactionRecords = transactionRecordMapper.selMaxAndMinPrice(directoryId, supplierId,syncTime);
        if( transactionRecords.size() == 0 || transactionRecords == null){
            log.error("--------没有查询到数据，请校验参数------------");
            log.info("采购品id：{},供应商id：{}",directoryId,supplierId);
            throw new RuntimeException("查询供应商最高成交价、最低成交价失败");
        }else{
            TransactionRecord transactionRecord = transactionRecords.get(0);
            int update = purchaseJdbcTemplate.update("insert into transaction_record(id,project_id,project_name,company_id,supplier_id,supplier_name," +
                    "directory_id , directory_name , min_price , max_price , win_time , sync_time) values( ? , ? , ? ,? ,? ,? ,? ,? , ? ,? , ? , ? )", new Object[]{
                    IdWork.nextId(), transactionRecord.getProjectId(), transactionRecord.getProjectName(), transactionRecord.getCompanyId(), transactionRecord.getSupplierId(), transactionRecord.getSupplierName(),
                    transactionRecord.getDirectoryId(), transactionRecord.getDirectoryName(), transactionRecord.getMinPrice(), transactionRecord.getMaxPrice() , transactionRecord.getWinTime(), new Date()});
            if( update != 1){
                log.error("数据插入失败，请检查数据和SQL的准确性，参数为：",transactionRecord);
            }
            return "success";
        }
    }

    @Override
    public String selQuotationHistory(Long directoryId, Long supplierId, String syncTime) {
        List<TransactionHistoryRecord> transactionHistoryRecords = transactionRecordMapper.selQuotationHistory(directoryId, supplierId, syncTime);
        List<Object[]> params = new ArrayList<>();
        String insertSql = "INSERT INTO `db_boot`.`transaction_history_record`(`id`, `project_id`, `project_name`, `company_id`, `supplier_id`, `supplier_name`, `directory_id`, " +
                "`directory_name`, `deal_price`, `win_time`, `deal_amount`, `deal_total_price`, `sync_time`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";
        if(transactionHistoryRecords.size()<=0){
            log.error("查询数据失败，请检验参数：");
        }else{
            for (TransactionHistoryRecord thr:transactionHistoryRecords) {
                params.add(new Object[]{
                        IdWork.nextId() , thr.getProjectId() , thr.getProjectName() , thr.getCompanyId() , thr.getSupplierId() , thr.getSupplierName() ,
                        thr.getDirectoryId() , thr.getDirectoryName() , thr.getDealPrice() , thr.getWinTime() , thr.getDealAmount() , thr.getDealTotalPrice() ,new Date()                });
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
