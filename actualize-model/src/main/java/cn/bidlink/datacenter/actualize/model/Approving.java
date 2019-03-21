package cn.bidlink.datacenter.actualize.model;

import java.util.Date;

public class Approving extends ApprovingKey {
    private Long projectId;

    private String projectNo;

    private String projectName;

    private Long projectCreateUserId;

    private String projectCreateUserName;

    private Date projectCreateTime;

    private Boolean module;

    private Byte moduleNodeType;

    private Long createUserId;

    private String procInstId;

    private Long customId;

    private Date createTime;

    private Byte approveStatus;

    private Byte projectStatus;

    private Boolean isHistoryProject;

    private Boolean isSolveData;

    private Byte approveResult;

    private Byte customVersion;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo == null ? null : projectNo.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Long getProjectCreateUserId() {
        return projectCreateUserId;
    }

    public void setProjectCreateUserId(Long projectCreateUserId) {
        this.projectCreateUserId = projectCreateUserId;
    }

    public String getProjectCreateUserName() {
        return projectCreateUserName;
    }

    public void setProjectCreateUserName(String projectCreateUserName) {
        this.projectCreateUserName = projectCreateUserName == null ? null : projectCreateUserName.trim();
    }

    public Date getProjectCreateTime() {
        return projectCreateTime;
    }

    public void setProjectCreateTime(Date projectCreateTime) {
        this.projectCreateTime = projectCreateTime;
    }

    public Boolean getModule() {
        return module;
    }

    public void setModule(Boolean module) {
        this.module = module;
    }

    public Byte getModuleNodeType() {
        return moduleNodeType;
    }

    public void setModuleNodeType(Byte moduleNodeType) {
        this.moduleNodeType = moduleNodeType;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId == null ? null : procInstId.trim();
    }

    public Long getCustomId() {
        return customId;
    }

    public void setCustomId(Long customId) {
        this.customId = customId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Byte approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Byte getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Byte projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Boolean getIsHistoryProject() {
        return isHistoryProject;
    }

    public void setIsHistoryProject(Boolean isHistoryProject) {
        this.isHistoryProject = isHistoryProject;
    }

    public Boolean getIsSolveData() {
        return isSolveData;
    }

    public void setIsSolveData(Boolean isSolveData) {
        this.isSolveData = isSolveData;
    }

    public Byte getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(Byte approveResult) {
        this.approveResult = approveResult;
    }

    public Byte getCustomVersion() {
        return customVersion;
    }

    public void setCustomVersion(Byte customVersion) {
        this.customVersion = customVersion;
    }
}