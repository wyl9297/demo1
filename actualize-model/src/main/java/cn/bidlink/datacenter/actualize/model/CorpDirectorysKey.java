package cn.bidlink.datacenter.actualize.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class CorpDirectorysKey {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "companyId")
    private Long companyId;

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
}