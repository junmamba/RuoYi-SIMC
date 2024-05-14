package com.ruoyi.simc.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.ExcelRowData;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 13:49
 */
public class SimcResidentSocialInsuranceImportRowData extends ExcelRowData {
    private static final long serialVersionUID = 1L;

    @Excel(name = "姓名", cellType = Excel.ColumnType.STRING, prompt = "姓名")
    private String residentName;

    @Excel(name = "性别", cellType = Excel.ColumnType.STRING, prompt = "性别")
    private String residentSex;

    @Excel(name = "身份证号码", cellType = Excel.ColumnType.STRING, prompt = "身份证号码")
    private String residentIdCardNo;

    @Excel(name = "户主身份证号码", cellType = Excel.ColumnType.STRING, prompt = "户主身份证号码")
    private String headResidentIdCardNo;

    @Excel(name = "乡镇", cellType = Excel.ColumnType.STRING, prompt = "乡镇")
    private String residentTownshipDistrictName;

    @Excel(name = "村（社区）", cellType = Excel.ColumnType.STRING, prompt = "村（社区）")
    private String residentVillageDistrictName;

    @Excel(name = "组", cellType = Excel.ColumnType.STRING, prompt = "组")
    private String residentGroupDistrictName;

    @Excel(name = "电话", cellType = Excel.ColumnType.STRING, prompt = "电话")
    private String residentPhone;

    @Excel(name = "被征地时家庭人数", cellType = Excel.ColumnType.NUMERIC, prompt = "被征地时家庭人数")
    private Integer fllFamilyMemberNumber;

    @Excel(name = "符合纳入参保条件人数", cellType = Excel.ColumnType.NUMERIC, prompt = "符合纳入参保条件人数")
    private Integer fllEligibleMemberNumber;

    @Excel(name = "原耕地面积（亩）", cellType = Excel.ColumnType.NUMERIC, prompt = "原耕地面积（亩）")
    private Double originalAgriculturalAcreage;

    @Excel(name = "共被征耕地（亩）", cellType = Excel.ColumnType.NUMERIC, prompt = "共被征耕地（亩）")
    private Double fllTotalAgriculturalAcreage;

    @Excel(name = "现在承包耕地（亩）", cellType = Excel.ColumnType.NUMERIC, prompt = "现在承包耕地（亩）")
    private Double currentAgriculturalAcreage;

    @Excel(name = "现人均耕地面积", cellType = Excel.ColumnType.NUMERIC, prompt = "现人均耕地面积")
    private Double perAgriculturalAcreage;

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

    public String getHeadResidentIdCardNo() {
        return headResidentIdCardNo;
    }

    public void setHeadResidentIdCardNo(String headResidentIdCardNo) {
        this.headResidentIdCardNo = headResidentIdCardNo;
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
}
