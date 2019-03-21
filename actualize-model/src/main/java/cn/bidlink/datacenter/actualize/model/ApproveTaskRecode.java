package cn.bidlink.datacenter.actualize.model;

import java.util.Date;

public class ApproveTaskRecode extends ApproveTaskRecodeKey {
    private String procInstanceId;

    private String taskId;

    private String taskDefKey;

    private String assign;

    private Byte currentNodeIndex;

    private Byte type;

    private Byte status;

    private Byte multInstance;

    private String approveSuggestion;

    private Boolean approveType;

    private Date approveTime;

    private Date createTime;

    private Long createUserId;

    private Date updateTime;

    private Long updateUserId;

    private String description;

    private Long customId;

    private String supplierIds;

    private Long supplierId;
    public String getProcInstanceId() {
        return procInstanceId;
    }

    public void setProcInstanceId(String procInstanceId) {
        this.procInstanceId = procInstanceId == null ? null : procInstanceId.trim();
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierIds() {
        return supplierIds;
    }

    public void setSupplierIds(String supplierIds) {
        this.supplierIds = supplierIds;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey == null ? null : taskDefKey.trim();
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign == null ? null : assign.trim();
    }

    public Byte getCurrentNodeIndex() {
        return currentNodeIndex;
    }

    public void setCurrentNodeIndex(Byte currentNodeIndex) {
        this.currentNodeIndex = currentNodeIndex;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getMultInstance() {
        return multInstance;
    }

    public void setMultInstance(Byte multInstance) {
        this.multInstance = multInstance;
    }

    public String getApproveSuggestion() {
        return approveSuggestion;
    }

    public void setApproveSuggestion(String approveSuggestion) {
        this.approveSuggestion = approveSuggestion == null ? null : approveSuggestion.trim();
    }

    public Boolean getApproveType() {
        return approveType;
    }

    public void setApproveType(Boolean approveType) {
        this.approveType = approveType;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getCustomId() {
        return customId;
    }

    public void setCustomId(Long customId) {
        this.customId = customId;
    }
}