package com.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.util.Date;

@ExcelTarget("corpCatalogs")
public class CorpCatalogs extends CorpCatalogsKey {

    @Excel(name = "oldId")
    private Long oldId;

    @Excel(name = "name")
    private String name;

    @Excel(name = "code")
    private String code;

    @Excel(name = "demo")
    private String demo;

    @Excel(name = "parentId")
    private Long parentId;

    @Excel(name = "compId")
    private Long compId;

    @Excel(name = "isRoot")
    private Byte isRoot;

    @Excel(name = "treepath")
    private String treepath;

    @Excel(name = "erpId")
    private String erpId;

    @Excel(name = "erpcode")
    private String erpcode;

    @Excel(name = "flagAssociateUser")
    private Byte flagAssociateUser;

    @Excel(name = "flagAssociateSupplier")
    private Byte flagAssociateSupplier;

    @Excel(name = "catalogNamePath")
    private String catalogNamePath;

    @Excel(name = "createTime")
    private Date createTime;

    @Excel(name = "creator")
    private Long creator;

    @Excel(name = "updateTime")
    private Date updateTime;

    @Excel(name = "modifier")
    private Long modifier;

    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo == null ? null : demo.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }

    public Byte getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Byte isRoot) {
        this.isRoot = isRoot;
    }

    public String getTreepath() {
        return treepath;
    }

    public void setTreepath(String treepath) {
        this.treepath = treepath == null ? null : treepath.trim();
    }

    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId == null ? null : erpId.trim();
    }

    public String getErpcode() {
        return erpcode;
    }

    public void setErpcode(String erpcode) {
        this.erpcode = erpcode == null ? null : erpcode.trim();
    }

    public Byte getFlagAssociateUser() {
        return flagAssociateUser;
    }

    public void setFlagAssociateUser(Byte flagAssociateUser) {
        this.flagAssociateUser = flagAssociateUser;
    }

    public Byte getFlagAssociateSupplier() {
        return flagAssociateSupplier;
    }

    public void setFlagAssociateSupplier(Byte flagAssociateSupplier) {
        this.flagAssociateSupplier = flagAssociateSupplier;
    }

    public String getCatalogNamePath() {
        return catalogNamePath;
    }

    public void setCatalogNamePath(String catalogNamePath) {
        this.catalogNamePath = catalogNamePath == null ? null : catalogNamePath.trim();
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