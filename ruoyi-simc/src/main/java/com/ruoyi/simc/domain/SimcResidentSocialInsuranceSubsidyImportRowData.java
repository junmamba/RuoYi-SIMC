package com.ruoyi.simc.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.ExcelRowData;

import java.util.Date;

public class SimcResidentSocialInsuranceSubsidyImportRowData extends ExcelRowData {
    private static final long serialVersionUID = 1L;

    @Excel(name = "姓名", cellType = Excel.ColumnType.STRING, prompt = "姓名")
    private String residentName;

    @Excel(name = "性别", cellType = Excel.ColumnType.STRING, prompt = "性别")
    private String residentSex;

    @Excel(name = "身份证号码", cellType = Excel.ColumnType.STRING, prompt = "身份证号码")
    private String residentIdCardNo;

    @Excel(name = "行政区域", cellType = Excel.ColumnType.STRING, prompt = "行政区域")
    private String districtName;

    @Excel(name = "电话", cellType = Excel.ColumnType.STRING, prompt = "电话")
    private String residentPhone;

    @Excel(name = "征地项目名称", cellType = Excel.ColumnType.STRING, prompt = "征地项目名称")
    private String fllProjectName;

    @Excel(name = "是否市级征地项目", cellType = Excel.ColumnType.STRING, prompt = "是否市级征地项目")
    private String fllProjectIsCityLevel;

    @Excel(name = "征地时间", cellType = Excel.ColumnType.STRING, prompt = "征地时间")
    private String fllTime;

    @Excel(name = "联审联批通过时间", cellType = Excel.ColumnType.STRING, prompt = "联审联批通过时间")
    private String socialInsuranceJointApprovalTime;

    @Excel(name = "参保类别", cellType = Excel.ColumnType.STRING, prompt = "参保类别")
    private String socialInsuranceType;

    @Excel(name = "参保状态", cellType = Excel.ColumnType.STRING, prompt = "参保状态")
    private String socialInsuranceState;

    @Excel(name = "年龄", cellType = Excel.ColumnType.STRING, prompt = "年龄")
    private String age;

    @Excel(name = "退休状态", cellType = Excel.ColumnType.STRING, prompt = "退休状态")
    private String retireState;

    @Excel(name = "补贴年月", cellType = Excel.ColumnType.STRING, prompt = "补贴年月")
    private String subsidyTime;

    @Excel(name = "补贴金额", cellType = Excel.ColumnType.STRING, prompt = "补贴金额")
    private Double subsidyMoney;

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
        this.residentIdCardNo = residentIdCardNo == null ? null : residentIdCardNo.trim();
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone;
    }

    public String getFllProjectName() {
        return fllProjectName;
    }

    public void setFllProjectName(String fllProjectName) {
        this.fllProjectName = fllProjectName;
    }

    public String getFllProjectIsCityLevel() {
        return fllProjectIsCityLevel;
    }

    public void setFllProjectIsCityLevel(String fllProjectIsCityLevel) {
        this.fllProjectIsCityLevel = fllProjectIsCityLevel;
    }

    public String getFllTime() {
        return fllTime;
    }

    public void setFllTime(String fllTime) {
        this.fllTime = fllTime;
    }

    public String getSocialInsuranceJointApprovalTime() {
        return socialInsuranceJointApprovalTime;
    }

    public void setSocialInsuranceJointApprovalTime(String socialInsuranceJointApprovalTime) {
        this.socialInsuranceJointApprovalTime = socialInsuranceJointApprovalTime;
    }

    public String getSocialInsuranceType() {
        return socialInsuranceType;
    }

    public void setSocialInsuranceType(String socialInsuranceType) {
        this.socialInsuranceType = socialInsuranceType;
    }

    public String getSocialInsuranceState() {
        return socialInsuranceState;
    }

    public void setSocialInsuranceState(String socialInsuranceState) {
        this.socialInsuranceState = socialInsuranceState;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRetireState() {
        return retireState;
    }

    public void setRetireState(String retireState) {
        this.retireState = retireState;
    }

    public String getSubsidyTime() {
        return subsidyTime;
    }

    public void setSubsidyTime(String subsidyTime) {
        this.subsidyTime = subsidyTime;
    }

    public Double getSubsidyMoney() {
        return subsidyMoney;
    }

    public void setSubsidyMoney(Double subsidyMoney) {
        this.subsidyMoney = subsidyMoney;
    }
}
