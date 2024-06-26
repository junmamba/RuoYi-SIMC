package com.ruoyi.simc.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

public class SimcResidentOldLandLosing extends BaseEntity {
    private String residentIdCardNo;

    private String residentName;

    private Integer residentSex;

    private Date residentBirthDate;

    private String residentPhone;

    private Long residentTownshipDistrictId;

    private Long residentVillageDistrictId;

    private Long residentGroupDistrictId;

    private Date payTime;

    private String payLevel;

    private Double payMoney;

    private String bank;

    private String bankCode;

    private Date theFirstReceiveTime;

    private Double theFirstYearPerMonthReceiveMoney;

    private String state;

    private Date quitTime;

    private String socialInsuranceRemark;

    private Date socialInsuranceTime;

    private String returnFeeState;

    private Double returnFee;

    private Date returnFeeTime;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    private String id;
    private Long districtId;
    private String districtName;
    private String strPayTime;
    private String strTheFirstReceiveTime;
    private String strQuitTime;
    private String strReturnFeeTime;
    private Double receivedTotalFee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
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

    public Date getTheFirstReceiveTime() {
        return theFirstReceiveTime;
    }

    public void setTheFirstReceiveTime(Date theFirstReceiveTime) {
        this.theFirstReceiveTime = theFirstReceiveTime;
    }

    public Double getTheFirstYearPerMonthReceiveMoney() {
        return theFirstYearPerMonthReceiveMoney;
    }

    public void setTheFirstYearPerMonthReceiveMoney(Double theFirstYearPerMonthReceiveMoney) {
        this.theFirstYearPerMonthReceiveMoney = theFirstYearPerMonthReceiveMoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(Date quitTime) {
        this.quitTime = quitTime;
    }

    public String getSocialInsuranceRemark() {
        return socialInsuranceRemark;
    }

    public void setSocialInsuranceRemark(String socialInsuranceRemark) {
        this.socialInsuranceRemark = socialInsuranceRemark == null ? null : socialInsuranceRemark.trim();
    }

    public Date getSocialInsuranceTime() {
        return socialInsuranceTime;
    }

    public void setSocialInsuranceTime(Date socialInsuranceTime) {
        this.socialInsuranceTime = socialInsuranceTime;
    }

    public String getReturnFeeState() {
        return returnFeeState;
    }

    public void setReturnFeeState(String returnFeeState) {
        this.returnFeeState = returnFeeState == null ? null : returnFeeState.trim();
    }

    public Double getReturnFee() {
        return returnFee;
    }

    public void setReturnFee(Double returnFee) {
        this.returnFee = returnFee;
    }

    public Date getReturnFeeTime() {
        return returnFeeTime;
    }

    public void setReturnFeeTime(Date returnFeeTime) {
        this.returnFeeTime = returnFeeTime;
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

    public String getStrPayTime() {
        return strPayTime;
    }

    public void setStrPayTime(String strPayTime) {
        this.strPayTime = strPayTime;
    }

    public String getStrTheFirstReceiveTime() {
        return strTheFirstReceiveTime;
    }

    public void setStrTheFirstReceiveTime(String strTheFirstReceiveTime) {
        this.strTheFirstReceiveTime = strTheFirstReceiveTime;
    }

    public String getStrQuitTime() {
        return strQuitTime;
    }

    public void setStrQuitTime(String strQuitTime) {
        this.strQuitTime = strQuitTime;
    }

    public String getStrReturnFeeTime() {
        return strReturnFeeTime;
    }

    public void setStrReturnFeeTime(String strReturnFeeTime) {
        this.strReturnFeeTime = strReturnFeeTime;
    }

    public Double getReceivedTotalFee() {
        return receivedTotalFee;
    }

    public void setReceivedTotalFee(Double receivedTotalFee) {
        this.receivedTotalFee = receivedTotalFee;
    }
}