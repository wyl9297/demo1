package com.demo.model;

/**
 * @author : <a href="mailto:congyaozhu@ebnew.com">congyaozhu</a>
 * @Date : Created in  21:09 2019-02-19
 * @Description : 采购品目录  对应数据库字段
 */
public class CorpCatalog {
    private Long id;// `ID` bigint(20) NOT NULL COMMENT '主键',			IDService
    private Long oldId; //`NAME` varchar(50) DEFAULT '' COMMENT '名称',		限定为50
    private String name; // `CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '编码',	限定长度为20
    private String code; // `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父级ID',
    private String demo;//         `IS_ROOT` tinyint(4) DEFAULT NULL COMMENT '是否根ID, 1 =是，0=否',
    private Long parentId; //          `ID_PATH` varchar(768) DEFAULT NULL COMMENT '目录路径ID，包含上级目录ID及自身ID，以#做分隔，如#1#563437227#563437323#',		TREEPATH
    private Long compId; // `NAME_PATH` varchar(768) DEFAULT NULL COMMENT '目录路径名称，包含上级目录ID及自身ID，以#做分隔，如#1#563437227#563437323#',	CATALOG_NAME_PATH
    private int isRoot; // `company_id` bigint(20) NOT NULL COMMENT '企业ID',
    private String treepath; //           `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',		creator
    private String erpId;  // `create_user_name` varchar(30) DEFAULT '' COMMENT '创建人',		creator
    private String erpcode; // `create_time` datetime DEFAULT NULL COMMENT '创建时间',			null
    private int flagAssociateUser; //          `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',		无
    private int flagAssociateSupplier; // `update_user_name` varchar(30) DEFAULT NULL COMMENT '更新人',		无
    private String catalogNamePath; // `update_time` datetime DEFAULT NULL COMMENT '更新时间',			当前时间
    private Long companyId;
    private String createTime;
    private Long creator;
    private String updateTime;
    private Long modifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
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

    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    public String getErpcode() {
        return erpcode;
    }

    public void setErpcode(String erpcode) {
        this.erpcode = erpcode;
    }

    public int getFlagAssociateUser() {
        return flagAssociateUser;
    }

    public void setFlagAssociateUser(int flagAssociateUser) {
        this.flagAssociateUser = flagAssociateUser;
    }

    public int getFlagAssociateSupplier() {
        return flagAssociateSupplier;
    }

    public void setFlagAssociateSupplier(int flagAssociateSupplier) {
        this.flagAssociateSupplier = flagAssociateSupplier;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }
}
