package cn.bidlink.datacenter.actualize.model;

import java.util.Date;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  14:44 2019-03-10
 * @Description : 采购品  历史中标供应商记录
 */
public class PurchasesWinSupplierRecord {

    private Long id;
    private Long companyId;
    private Long supplierId;
    private Long directoryId;
    private Date winBidTime;
    private Date syncTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(Long directoryId) {
        this.directoryId = directoryId;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Date getWinBidTime() {
        return winBidTime;
    }

    public void setWinBidTime(Date winBidTime) {
        this.winBidTime = winBidTime;
    }
}
