package com.ruoyi.simc.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.ExcelRowData;

public class SimcResidentOldLandLosingImportRowData extends ExcelRowData {
    private static final long serialVersionUID = 1L;

    @Excel(name = "姓名", cellType = Excel.ColumnType.STRING, prompt = "姓名")
    private String residentName;

    @Excel(name = "性别", cellType = Excel.ColumnType.STRING, prompt = "性别")
    private String residentSex;

    @Excel(name = "身份证号码", cellType = Excel.ColumnType.STRING, prompt = "身份证号码")
    private String residentIdCardNo;

    @Excel(name = "乡镇", cellType = Excel.ColumnType.STRING, prompt = "乡镇")
    private String residentTownshipDistrictName;

    @Excel(name = "村（社区）", cellType = Excel.ColumnType.STRING, prompt = "村（社区）")
    private String residentVillageDistrictName;

    @Excel(name = "组", cellType = Excel.ColumnType.STRING, prompt = "组")
    private String residentGroupDistrictName;

    @Excel(name = "电话", cellType = Excel.ColumnType.STRING, prompt = "电话")
    private String residentPhone;

    @Excel(name = "缴费时间", cellType = Excel.ColumnType.STRING, prompt = "缴费时间")
    private String strPayTime;

    @Excel(name = "缴费档次", cellType = Excel.ColumnType.STRING, prompt = "缴费档次")
    private String strPayLevel;

    @Excel(name = "缴费金额", cellType = Excel.ColumnType.NUMERIC, prompt = "缴费金额")
    private Double payMoney;

    @Excel(name = "开户行", cellType = Excel.ColumnType.STRING, prompt = "开户行")
    private String bank;

    @Excel(name = "银行卡号", cellType = Excel.ColumnType.STRING, prompt = "银行卡号")
    private String bankCode;

    @Excel(name = "状态", cellType = Excel.ColumnType.STRING, prompt = "状态")
    private String strState;

    @Excel(name = "退出时间", cellType = Excel.ColumnType.STRING, prompt = "退出时间")
    private String strQuitTime;

    @Excel(name = "参保情况", cellType = Excel.ColumnType.STRING, prompt = "参保情况")
    private String socialInsuranceRemark;

    @Excel(name = "参保时间", cellType = Excel.ColumnType.STRING, prompt = "参保时间")
    private String strSocialInsuranceTime;

    @Excel(name = "是否已退费", cellType = Excel.ColumnType.STRING, prompt = "是否已退费")
    private String strReturnFeeState;

    @Excel(name = "退费时间", cellType = Excel.ColumnType.STRING, prompt = "退费时间")
    private String strReturnFeeTime;

    @Excel(name = "退费金额", cellType = Excel.ColumnType.STRING, prompt = "退费金额")
    private Double returnFee;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getResidentSex() {
        return residentSex;
    }

    public void setResidentSex(String residentSex) {
        this.residentSex = residentSex;
    }

    public String getResidentIdCardNo() {
        return residentIdCardNo;
    }

    public void setResidentIdCardNo(String residentIdCardNo) {
        this.residentIdCardNo = residentIdCardNo;
    }

    public String getResidentTownshipDistrictName() {
        return residentTownshipDistrictName;
    }

    public void setResidentTownshipDistrictName(String residentTownshipDistrictName) {
        this.residentTownshipDistrictName = residentTownshipDistrictName;
    }

    public String getResidentVillageDistrictName() {
        return residentVillageDistrictName;
    }

    public void setResidentVillageDistrictName(String residentVillageDistrictName) {
        this.residentVillageDistrictName = residentVillageDistrictName;
    }

    public String getResidentGroupDistrictName() {
        return residentGroupDistrictName;
    }

    public void setResidentGroupDistrictName(String residentGroupDistrictName) {
        this.residentGroupDistrictName = residentGroupDistrictName;
    }

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone;
    }

    public String getStrPayTime() {
        return strPayTime;
    }

    public void setStrPayTime(String strPayTime) {
        this.strPayTime = strPayTime;
    }

    public String getStrPayLevel() {
        return strPayLevel;
    }

    public void setStrPayLevel(String strPayLevel) {
        this.strPayLevel = strPayLevel;
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
        this.bank = bank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
    }

    public String getStrQuitTime() {
        return strQuitTime;
    }

    public void setStrQuitTime(String strQuitTime) {
        this.strQuitTime = strQuitTime;
    }

    public String getSocialInsuranceRemark() {
        return socialInsuranceRemark;
    }

    public void setSocialInsuranceRemark(String socialInsuranceRemark) {
        this.socialInsuranceRemark = socialInsuranceRemark;
    }

    public String getStrSocialInsuranceTime() {
        return strSocialInsuranceTime;
    }

    public void setStrSocialInsuranceTime(String strSocialInsuranceTime) {
        this.strSocialInsuranceTime = strSocialInsuranceTime;
    }

    public String getStrReturnFeeState() {
        return strReturnFeeState;
    }

    public void setStrReturnFeeState(String strReturnFeeState) {
        this.strReturnFeeState = strReturnFeeState;
    }

    public String getStrReturnFeeTime() {
        return strReturnFeeTime;
    }

    public void setStrReturnFeeTime(String strReturnFeeTime) {
        this.strReturnFeeTime = strReturnFeeTime;
    }

    public Double getReturnFee() {
        return returnFee;
    }

    public void setReturnFee(Double returnFee) {
        this.returnFee = returnFee;
    }
}
