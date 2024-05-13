package com.ruoyi.simc.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

public class SimcResidentSocialInsurance extends BaseEntity {
    @Excel(name = "身份证号码", sort = 3, type = Excel.Type.EXPORT)
    private String residentIdCardNo;

    @Excel(name = "姓名", sort = 1, type = Excel.Type.EXPORT)
    private String residentName;

    @Excel(name = "性别", sort = 2, readConverterExp = "1=男,2=女", type = Excel.Type.EXPORT)
    private Integer residentSex;

    private Date residentBirthDate;

    @Excel(name = "电话", sort = 5, type = Excel.Type.EXPORT)
    private String residentPhone;

    private Long residentTownshipDistrictId;

    private Long residentVillageDistrictId;

    private Long residentGroupDistrictId;

    private Long districtId;

    @Excel(name = "行政区域", sort = 4, type = Excel.Type.EXPORT)
    private String districtName;

    private Integer fllId;

    @Excel(name = "征地项目名称", sort = 6, type = Excel.Type.EXPORT)
    private String fllProjectName;

    @Excel(name = "征地时间", sort = 8, type = Excel.Type.EXPORT)
    private String fllTime;

    @Excel(name = "是否市级征地项目", sort = 7, readConverterExp = "0=否,1=是", type = Excel.Type.EXPORT)
    private Integer fllProjectIsCityLevel;

    @Excel(name = "参保类别", sort = 10, readConverterExp = "1=城镇职工,2=城乡居民", type = Excel.Type.EXPORT)
    private String socialInsuranceType;

    @Excel(name = "参保状态", sort = 11, readConverterExp = "1=在职,2=退休,3=死亡,4=退出,99=其他", type = Excel.Type.EXPORT)
    private String socialInsuranceState;

    private Date socialInsuranceJointApprovalTime;

    @Excel(name = "联审联批通过时间", sort = 9, type = Excel.Type.EXPORT)
    private String strSocialInsuranceJointApprovalTime;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    @Excel(name = "年龄", sort = 12, type = Excel.Type.EXPORT)
    private String age;

    @Excel(name = "退休状态", sort = 13, readConverterExp = "0=未退休,1=即将退休,2=退休", type = Excel.Type.EXPORT)
    private Integer retireState;

    @Excel(name = "补贴年月", sort = 14, type = Excel.Type.EXPORT)
    private String subsidyTime;

    @Excel(name = "补贴金额", sort = 15, type = Excel.Type.EXPORT)
    private String subsidyMoney;

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

    public String getSubsidyTime() {
        return subsidyTime;
    }

    public void setSubsidyTime(String subsidyTime) {
        this.subsidyTime = subsidyTime;
    }

    public String getSubsidyMoney() {
        return subsidyMoney;
    }

    public void setSubsidyMoney(String subsidyMoney) {
        this.subsidyMoney = subsidyMoney;
    }
}