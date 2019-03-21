package cn.bidlink.datacenter.actualize.model;

import java.math.BigDecimal;
import java.util.Date;

public class RegDepartment extends RegDepartmentKey {
    private String name;

    private BigDecimal parentId;

    private Long status;

    private Long type;

    private Long bidlinkId;

    private String departmentCode;

    private String isCompany;

    private String companyCode;

    private String erpCode;

    private String erpCompanyCode;

    private Date createTime;

    private Long creator;

    private Date updateTime;

    private Long modifier;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getParentId() {
        return parentId;
    }

    public void setParentId(BigDecimal parentId) {
        this.parentId = parentId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getBidlinkId() {
        return bidlinkId;
    }

    public void setBidlinkId(Long bidlinkId) {
        this.bidlinkId = bidlinkId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode == null ? null : departmentCode.trim();
    }

    public String getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(String isCompany) {
        this.isCompany = isCompany == null ? null : isCompany.trim();
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public String getErpCompanyCode() {
        return erpCompanyCode;
    }

    public void setErpCompanyCode(String erpCompanyCode) {
        this.erpCompanyCode = erpCompanyCode == null ? null : erpCompanyCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }
}