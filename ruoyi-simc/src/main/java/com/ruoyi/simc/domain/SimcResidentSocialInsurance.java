package com.ruoyi.simc.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

public class SimcResidentSocialInsurance extends BaseEntity {
    private String residentIdCardNo;

    private String residentName;

    private Integer residentSex;

    private Date residentBrithDate;

    private String residentPhone;

    private Long residentTownshipDistrictId;

    private Long residentVillageDistrictId;

    private Long residentGroupDistrictId;

    private Long districtId;

    private String districtName;

    private Integer fllId;

    private String fllProjectName;

    private String fllTime;

    private Integer fllProjectIsCityLevel;

    private String socialInsuranceType;

    private String socialInsuranceState;

    private Date socialInsuranceJointApprovalTime;

    private String strSocialInsuranceJointApprovalTime;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    public String getResidentIdCardNo() {
        return residentIdCardNo;
    }

    public void setResidentIdCardNo(String residentIdCardNo) {
        this.residentIdCardNo = residentIdCardNo == null ? null : residentIdCardNo.trim();
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName == null ? null : residentName.trim();
    }

    public Integer getResidentSex() {
        return residentSex;
    }

    public void setResidentSex(Integer residentSex) {
        this.residentSex = residentSex;
    }

    public Date getResidentBrithDate() {
        return residentBrithDate;
    }

    public void setResidentBrithDate(Date residentBrithDate) {
        this.residentBrithDate = residentBrithDate;
    }

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone == null ? null : residentPhone.trim();
    }

    public Long getResidentTownshipDistrictId() {
        return residentTownshipDistrictId;
    }

    public void setResidentTownshipDistrictId(Long residentTownshipDistrictId) {
        this.residentTownshipDistrictId = residentTownshipDistrictId;
    }

    public Long getResidentVillageDistrictId() {
        return residentVillageDistrictId;
    }

    public void setResidentVillageDistrictId(Long residentVillageDistrictId) {
        this.residentVillageDistrictId = residentVillageDistrictId;
    }

    public Long getResidentGroupDistrictId() {
        return residentGroupDistrictId;
    }

    public void setResidentGroupDistrictId(Long residentGroupDistrictId) {
        this.residentGroupDistrictId = residentGroupDistrictId;
    }

    public Integer getFllId() {
        return fllId;
    }

    public void setFllId(Integer fllId) {
        this.fllId = fllId;
    }

    public String getFllProjectName() {
        return fllProjectName;
    }

    public void setFllProjectName(String fllProjectName) {
        this.fllProjectName = fllProjectName == null ? null : fllProjectName.trim();
    }

    public String getFllTime() {
        return fllTime;
    }

    public void setFllTime(String fllTime) {
        this.fllTime = fllTime == null ? null : fllTime.trim();
    }

    public Integer getFllProjectIsCityLevel() {
        return fllProjectIsCityLevel;
    }

    public void setFllProjectIsCityLevel(Integer fllProjectIsCityLevel) {
        this.fllProjectIsCityLevel = fllProjectIsCityLevel;
    }

    public String getSocialInsuranceType() {
        return socialInsuranceType;
    }

    public void setSocialInsuranceType(String socialInsuranceType) {
        this.socialInsuranceType = socialInsuranceType == null ? null : socialInsuranceType.trim();
    }

    public String getSocialInsuranceState() {
        return socialInsuranceState;
    }

    public void setSocialInsuranceState(String socialInsuranceState) {
        this.socialInsuranceState = socialInsuranceState == null ? null : socialInsuranceState.trim();
    }

    public Date getSocialInsuranceJointApprovalTime() {
        return socialInsuranceJointApprovalTime;
    }

    public void setSocialInsuranceJointApprovalTime(Date socialInsuranceJointApprovalTime) {
        this.socialInsuranceJointApprovalTime = socialInsuranceJointApprovalTime;
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStrSocialInsuranceJointApprovalTime() {
        return strSocialInsuranceJointApprovalTime;
    }

    public void setStrSocialInsuranceJointApprovalTime(String strSocialInsuranceJointApprovalTime) {
        this.strSocialInsuranceJointApprovalTime = strSocialInsuranceJointApprovalTime;
    }
}