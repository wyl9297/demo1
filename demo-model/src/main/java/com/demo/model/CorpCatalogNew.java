package com.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.Max;
import java.util.Date;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  21:09 2019-02-19
 * @Description : 采购品目录  对应数据库字段
 */
public class CorpCatalogNew {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "name")
    @Max(value = 50,message = "name 最多不能超过50")
    private String name;
    @Excel(name = "code")
    @Max(value = 20,message = "code 最多不能超过20")
    private String code;
    @Excel(name = "parentId")
    private Long parentId;
    @Excel(name = "isRoot")
    private int isRoot;
    @Excel(name = "treePath")
    private String treepath;
    @Excel(name = "catalogNamePath")
    private String catalogNamePath;
    @Excel(name = "companyId")
    private Long companyId;
    @Excel(name = "createUserId")
    private Long createUserId;
    @Excel(name = "createUserName")
    private String createUserName;
    @Excel(name = "createTime")
    private Date createTime;
    @Excel(name = "updateUserId")
    private Long updateUserId;
    @Excel(name = "updateUserName")
    private String updateUserName;
    @Excel(name = "updateTime")
    private Date updateTime;

    public CorpCatalogNew() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(int isRoot) {
        this.isRoot = isRoot;
    }

    public String getTreepath() {
        return treepath;
    }

    public void setTreepath(String treepath) {
        this.treepath = treepath;
    }

    public String getCatalogNamePath() {
        return catalogNamePath;
    }

    public void setCatalogNamePath(String catalogNamePath) {
        this.catalogNamePath = catalogNamePath;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
