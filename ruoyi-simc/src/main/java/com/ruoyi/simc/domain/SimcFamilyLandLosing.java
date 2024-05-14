package com.ruoyi.simc.domain;

import java.util.Date;

public class SimcFamilyLandLosing {
    private Integer fllId;

    private String familyNo;

    private String headResidentName;

    private Integer fllFamilyMemberNumber;

    private Integer fllEligibleMemberNumber;

    private Double originalAgriculturalAcreage;

    private Double fllTotalAgriculturalAcreage;

    private Double currentAgriculturalAcreage;

    private Double perAgriculturalAcreage;

    private Integer projectId;

    private String projectName;

    private Integer projectIsCityLevel;

    private String fllTime;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    public Integer getFllId() {
        return fllId;
    }

    public void setFllId(Integer fllId) {
        this.fllId = fllId;
    }

    public String getFamilyNo() {
        return familyNo;
    }

    public void setFamilyNo(String familyNo) {
        this.familyNo = familyNo == null ? null : familyNo.trim();
    }

    public String getHeadResidentName() {
        return headResidentName;
    }

    public void setHeadResidentName(String headResidentName) {
        this.headResidentName = headResidentName == null ? null : headResidentName.trim();
    }

    public Integer getFllFamilyMemberNumber() {
        return fllFamilyMemberNumber;
    }

    public void setFllFamilyMemberNumber(Integer fllFamilyMemberNumber) {
        this.fllFamilyMemberNumber = fllFamilyMemberNumber;
    }

    public Integer getFllEligibleMemberNumber() {
        return fllEligibleMemberNumber;
    }

    public void setFllEligibleMemberNumber(Integer fllEligibleMemberNumber) {
        this.fllEligibleMemberNumber = fllEligibleMemberNumber;
    }

    public Double getOriginalAgriculturalAcreage() {
        return originalAgriculturalAcreage;
    }

    public void setOriginalAgriculturalAcreage(Double originalAgriculturalAcreage) {
        this.originalAgriculturalAcreage = originalAgriculturalAcreage;
    }

    public Double getFllTotalAgriculturalAcreage() {
        return fllTotalAgriculturalAcreage;
    }

    public void setFllTotalAgriculturalAcreage(Double fllTotalAgriculturalAcreage) {
        this.fllTotalAgriculturalAcreage = fllTotalAgriculturalAcreage;
    }

    public Double getCurrentAgriculturalAcreage() {
        return currentAgriculturalAcreage;
    }

    public void setCurrentAgriculturalAcreage(Double currentAgriculturalAcreage) {
        this.currentAgriculturalAcreage = currentAgriculturalAcreage;
    }

    public Double getPerAgriculturalAcreage() {
        return perAgriculturalAcreage;
    }

    public void setPerAgriculturalAcreage(Double perAgriculturalAcreage) {
        this.perAgriculturalAcreage = perAgriculturalAcreage;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Integer getProjectIsCityLevel() {
        return projectIsCityLevel;
    }

    public void setProjectIsCityLevel(Integer projectIsCityLevel) {
        this.projectIsCityLevel = projectIsCityLevel;
    }

    public String getFllTime() {
        return fllTime;
    }

    public void setFllTime(String fllTime) {
        this.fllTime = fllTime == null ? null : fllTime.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}