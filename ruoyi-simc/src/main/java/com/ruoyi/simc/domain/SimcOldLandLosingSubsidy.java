package com.ruoyi.simc.domain;

import java.util.Date;

public class SimcOldLandLosingSubsidy {
    private Integer residentIdCardNo;

    private String residentName;

    private Integer residentSex;

    private Date residentBirthDate;

    private String residentPhone;

    private Long residentTownshipDistrictId;

    private Long residentVillageDistrictId;

    private Long residentGroupDistrictId;

    private Date payTime;

    private String payLevel;

    private Double payMonmey;

    private Date annuityDrawTime;

    private String annuityDrawLevel;

    private Double annuityMoney;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    public Integer getResidentIdCardNo() {
        return residentIdCardNo;
    }

    public void setResidentIdCardNo(Integer residentIdCardNo) {
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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(String payLevel) {
        this.payLevel = payLevel == null ? null : payLevel.trim();
    }

    public Double getPayMonmey() {
        return payMonmey;
    }

    public void setPayMonmey(Double payMonmey) {
        this.payMonmey = payMonmey;
    }

    public Date getAnnuityDrawTime() {
        return annuityDrawTime;
    }

    public void setAnnuityDrawTime(Date annuityDrawTime) {
        this.annuityDrawTime = annuityDrawTime;
    }

    public String getAnnuityDrawLevel() {
        return annuityDrawLevel;
    }

    public void setAnnuityDrawLevel(String annuityDrawLevel) {
        this.annuityDrawLevel = annuityDrawLevel == null ? null : annuityDrawLevel.trim();
    }

    public Double getAnnuityMoney() {
        return annuityMoney;
    }

    public void setAnnuityMoney(Double annuityMoney) {
        this.annuityMoney = annuityMoney;
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