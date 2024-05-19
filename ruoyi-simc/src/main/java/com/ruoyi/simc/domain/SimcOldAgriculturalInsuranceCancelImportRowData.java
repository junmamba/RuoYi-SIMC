package com.ruoyi.simc.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.ExcelRowData;

public class SimcOldAgriculturalInsuranceCancelImportRowData extends ExcelRowData {
    private static final long serialVersionUID = 1L;
    @Excel(name = "序号", cellType = Excel.ColumnType.STRING, prompt = "序号")
    private String no;

    @Excel(name = "退保人姓名", cellType = Excel.ColumnType.STRING, prompt = "退保人姓名")
    private String residentName;

    @Excel(name = "退保人身份证号码", cellType = Excel.ColumnType.STRING, prompt = "退保人身份证号码")
    private String residentIdCardNo;

    @Excel(name = "退保人手机号码", cellType = Excel.ColumnType.STRING, prompt = "退保人手机号码")
    private String residentPhone;

    @Excel(name = "乡镇(单位)", cellType = Excel.ColumnType.STRING, prompt = "乡镇(单位)")
    private String residentTownshipDistrictName;

    @Excel(name = "社区(二级单位)", cellType = Excel.ColumnType.STRING, prompt = "社区(二级单位)")
    private String residentVillageDistrictName;

    @Excel(name = "金额(元)", cellType = Excel.ColumnType.STRING, prompt = "金额(元)")
    private Double money;

    @Excel(name = "代领人姓名", cellType = Excel.ColumnType.STRING, prompt = "代领人姓名")
    private String surrogateName;

    @Excel(name = "代领人身份证号码", cellType = Excel.ColumnType.NUMERIC, prompt = "代领人身份证号码")
    private String surrogateIdCardNo;

    @Excel(name = "社保卡开户行", cellType = Excel.ColumnType.STRING, prompt = "社保卡开户行")
    private String bank;

    @Excel(name = "银行卡号", cellType = Excel.ColumnType.STRING, prompt = "银行卡号")
    private String bankCode;

    @Excel(name = "退保时间", cellType = Excel.ColumnType.STRING, prompt = "退保时间")
    private String strCancelTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getResidentIdCardNo() {
        return residentIdCardNo;
    }

    public void setResidentIdCardNo(String residentIdCardNo) {
        this.residentIdCardNo = residentIdCardNo;
    }

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getSurrogateName() {
        return surrogateName;
    }

    public void setSurrogateName(String surrogateName) {
        this.surrogateName = surrogateName;
    }

    public String getSurrogateIdCardNo() {
        return surrogateIdCardNo;
    }

    public void setSurrogateIdCardNo(String surrogateIdCardNo) {
        this.surrogateIdCardNo = surrogateIdCardNo;
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

    public String getStrCancelTime() {
        return strCancelTime;
    }

    public void setStrCancelTime(String strCancelTime) {
        this.strCancelTime = strCancelTime;
    }
}
