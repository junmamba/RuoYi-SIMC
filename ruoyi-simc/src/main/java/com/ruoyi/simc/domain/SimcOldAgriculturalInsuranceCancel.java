package com.ruoyi.simc.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

public class SimcOldAgriculturalInsuranceCancel extends BaseEntity {
    private String residentIdCardNo;

    private String residentName;

    private String residentPhone;

    private Long residentTownshipDistrictId;

    private Long residentVillageDistrictId;

    private Long residentGroupDistrictId;

    private Double money;

    private String surrogateIdCardNo;

    private String surrogateName;

    private String bank;

    private String bankCode;

    private Date cancelTime;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    private Long districtId;
    private String districtName;
    private String strCancelTime;

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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getSurrogateIdCardNo() {
        return surrogateIdCardNo;
    }

    public void setSurrogateIdCardNo(String surrogateIdCardNo) {
        this.surrogateIdCardNo = surrogateIdCardNo == null ? null : surrogateIdCardNo.trim();
    }

    public String getSurrogateName() {
        return surrogateName;
    }

    public void setSurrogateName(String surrogateName) {
        this.surrogateName = surrogateName == null ? null : surrogateName.trim();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
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

    public String getStrCancelTime() {
        return strCancelTime;
    }

    public void setStrCancelTime(String strCancelTime) {
        this.strCancelTime = strCancelTime;
    }
}