package com.demo.model;

import java.util.Date;

public class CorpCatalogs extends CorpCatalogsKey {
    private Long oldId;

    private String name;

    private String code;

    private String demo;

    private Long parentId;

    private Long compId;

    private Byte isRoot;

    private String treepath;

    private String erpId;

    private String erpcode;

    private Byte flagAssociateUser;

    private Byte flagAssociateSupplier;

    private String catalogNamePath;

    private Date createTime;

    private Long creator;

    private Date updateTime;

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