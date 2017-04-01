package com.demo.model;

import java.util.Date;

public class BmpfjzProject {
    private Long id;

    private Long compId;

    private String compName;

    private Long userId;

    private String userName;

    private Byte type;

    private String projectType;

    private String instanceId;

    private Byte instanceStatus;

    private String subInstanceId;

    private Byte subInstanceFinished;

    private Byte projectStatus;

    private Byte approveStatus;

    private String name;

    private String code;

    private String erpCode;

    private Boolean projectSource;

    private Byte sourceType;

    private String sourceName;

    private Byte openBidFlag;

    private Long bidSheetTemplateId;

    private Long totalSheetTemplateId;

    private String totalSheetTemplateData;

    private Date createTime;

    private Date openBidTime;

    private Date updateTime;

    private Long operator;

    private String reserve;

    private String departmentCode;

    private Byte stopStatus;

    private Byte orderStatus;

    private Byte isUploadFile;

    private String customDefId;

    private Byte isImportant;

    private Byte hasContract;

    private Byte isCore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName == null ? null : compName.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType == null ? null : projectType.trim();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

    public Byte getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(Byte instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public String getSubInstanceId() {
        return subInstanceId;
    }

    public void setSubInstanceId(String subInstanceId) {
        this.subInstanceId = subInstanceId == null ? null : subInstanceId.trim();
    }

    public Byte getSubInstanceFinished() {
        return subInstanceFinished;
    }

    public void setSubInstanceFinished(Byte subInstanceFinished) {
        this.subInstanceFinished = subInstanceFinished;
    }

    public Byte getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Byte projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Byte getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Byte approveStatus) {
        this.approveStatus = approveStatus;
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

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public Boolean getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(Boolean projectSource) {
        this.projectSource = projectSource;
    }

    public Byte getSourceType() {
        return sourceType;
    }

    public void setSourceType(Byte sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    public Byte getOpenBidFlag() {
        return openBidFlag;
    }

    public void setOpenBidFlag(Byte openBidFlag) {
        this.openBidFlag = openBidFlag;
    }

    public Long getBidSheetTemplateId() {
        return bidSheetTemplateId;
    }

    public void setBidSheetTemplateId(Long bidSheetTemplateId) {
        this.bidSheetTemplateId = bidSheetTemplateId;
    }

    public Long getTotalSheetTemplateId() {
        return totalSheetTemplateId;
    }

    public void setTotalSheetTemplateId(Long totalSheetTemplateId) {
        this.totalSheetTemplateId = totalSheetTemplateId;
    }

    public String getTotalSheetTemplateData() {
        return totalSheetTemplateData;
    }

    public void setTotalSheetTemplateData(String totalSheetTemplateData) {
        this.totalSheetTemplateData = totalSheetTemplateData == null ? null : totalSheetTemplateData.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOpenBidTime() {
        return openBidTime;
    }

    public void setOpenBidTime(Date openBidTime) {
        this.openBidTime = openBidTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode == null ? null : departmentCode.trim();
    }

    public Byte getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(Byte stopStatus) {
        this.stopStatus = stopStatus;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Byte getIsUploadFile() {
        return isUploadFile;
    }

    public void setIsUploadFile(Byte isUploadFile) {
        this.isUploadFile = isUploadFile;
    }

    public String getCustomDefId() {
        return customDefId;
    }

    public void setCustomDefId(String customDefId) {
        this.customDefId = customDefId == null ? null : customDefId.trim();
    }

    public Byte getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(Byte isImportant) {
        this.isImportant = isImportant;
    }

    public Byte getHasContract() {
        return hasContract;
    }

    public void setHasContract(Byte hasContract) {
        this.hasContract = hasContract;
    }

    public Byte getIsCore() {
        return isCore;
    }

    public void setIsCore(Byte isCore) {
        this.isCore = isCore;
    }
}