package com.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  17:44 2019-02-21
 * @Description :
 */
public class MiddleTable implements Serializable {
    @Excel(name = "oldId")
    @Max(value = 20,message = "oldId 最大长度为20")
    private Long oldId;
    @Excel(name = "newId")
    @Max(value = 20,message = "newId 最大长度为20")
    private Long newId;
    @Excel(name = "companyId")
    @Max(value = 20,message = "companyId 最大长度为20")
    private Long companyId;
    public MiddleTable() {
    }

    public MiddleTable(Long oldId,Long newId, Long companyId) {
        this.oldId = oldId;
        this.newId = newId;
        this.companyId = companyId;
    }

    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
