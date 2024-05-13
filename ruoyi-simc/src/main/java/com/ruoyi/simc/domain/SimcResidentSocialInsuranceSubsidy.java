package com.ruoyi.simc.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

public class SimcResidentSocialInsuranceSubsidy extends BaseEntity {
    private Long subsidyLogId;

    private String residentIdCardNo;

    private String residentName;

    private Integer residentSex;

    private Date residentBirthDate;

    private String residentPhone;

    private Long residentTownshipDistrictId;

    private Long residentVillageDistrictId;

    private Long residentGroupDistrictId;

    private Long districtId;

    private String districtName;

    private String socialInsuranceType;

    private Double subsidyMoney;

    private Date subsidyTime;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    private String age;

    private Integer retireState;

    private String strSubsidyTime;

    public Long getSubsidyLogId() {
        return subsidyLogId;
    }

    public void setSubsidyLogId(Long subsidyLogId) {
        this.subsidyLogId = subsidyLogId;
    }

    public String getResidentIdCardNo() {
        return residentIdCardNo;
    }

    public void setResidentIdCardNo(String residentIdCardNo) {
        this.residentIdCardNo = residentIdCardNo;
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

    public Date getResidentBirthDate() {
        return residentBirthDate;
    }

    public void setResidentBirthDate(Date residentBirthDate) {
        this.residentBirthDate = residentBirthDate;
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

    public String getSocialInsuranceType() {
        return socialInsuranceType;
    }

    public void setSocialInsuranceType(String socialInsuranceType) {
        this.socialInsuranceType = socialInsuranceType == null ? null : socialInsuranceType.trim();
    }

    public Double getSubsidyMoney() {
        return subsidyMoney;
    }

    public void setSubsidyMoney(Double subsidyMoney) {
        this.subsidyMoney = subsidyMoney;
    }

    public Date getSubsidyTime() {
        return subsidyTime;
    }

    public void setSubsidyTime(Date subsidyTime) {
        this.subsidyTime = subsidyTime;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getRetireState() {
        return retireState;
    }

    public void setRetireState(Integer retireState) {
        this.retireState = retireState;
    }

    public String getStrSubsidyTime() {
        return strSubsidyTime;
    }

    public void setStrSubsidyTime(String strSubsidyTime) {
        this.strSubsidyTime = strSubsidyTime;
    }
}