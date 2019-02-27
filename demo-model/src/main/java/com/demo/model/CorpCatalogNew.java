package com.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.Max;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  21:09 2019-02-19
 * @Description : 采购品目录  对应数据库字段
 */
public class CorpCatalogNew {
    @Excel(name = "id")
    private Long id;// `ID` bigint(20) NOT NULL COMMENT '主键',			IDService
    @Excel(name = "name")
    @Max(value = 50,message = "name 最多不能超过50")
    private String name; //`NAME` varchar(50) DEFAULT '' COMMENT '名称',		限定为50
    @Excel(name = "code")
    @Max(value = 20,message = "code 最多不能超过20")
    private String code; // `CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '编码',	限定长度为20
    @Excel(name = "parentId")
    private Long parentId; // `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父级ID',
    @Excel(name = "isRoot")
    private int isRoot;//         `IS_ROOT` tinyint(4) DEFAULT NULL COMMENT '是否根ID, 1 =是，0=否',
    @Excel(name = "idPath")
    private String idPath; //          `ID_PATH` varchar(768) DEFAULT NULL COMMENT '目录路径ID，包含上级目录ID及自身ID，以#做分隔，如#1#563437227#563437323#',		TREEPATH
    @Excel(name = "catalogNamePath")
    private String catalogNamePath; // `NAME_PATH` varchar(768) DEFAULT NULL COMMENT '目录路径名称，包含上级目录ID及自身ID，以#做分隔，如#1#563437227#563437323#',	CATALOG_NAME_PATH
    @Excel(name = "companyId")
    private Long companyId; // `company_id` bigint(20) NOT NULL COMMENT '企业ID',
    @Excel(name = "createUserId")
    private Long createUserId; //           `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',		creator
    @Excel(name = "createUserName")
    private String createUserName;  // `create_user_name` varchar(30) DEFAULT '' COMMENT '创建人',		creator
    @Excel(name = "createTime")
    private String createTime; // `create_time` datetime DEFAULT NULL COMMENT '创建时间',			null
    @Excel(name = "updateUserId")
    private Long updateUserId; //          `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',		无
    @Excel(name = "updateUserName")
    private String updateUserName; // `update_user_name` varchar(30) DEFAULT NULL COMMENT '更新人',		无
    @Excel(name = "updateTime")
    private String updateTime; // `update_time` datetime DEFAULT NULL COMMENT '更新时间',			当前时间

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

    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
