package com.ruoyi.simc.domain;

import java.util.Date;

public class SimcResident {
    private String idCardNo;

    private String name;

    private Integer sex;

    private Date birthDate;

    private String phone;

    private String resideType;

    private Long townshipDistrictId;

    private Long villageDistrictId;

    private Long groupDistrictId;

    private String livingState;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getResideType() {
        return resideType;
    }

    public void setResideType(String resideType) {
        this.resideType = resideType == null ? null : resideType.trim();
    }

    public Long getTownshipDistrictId() {
        return townshipDistrictId;
    }

    public void setTownshipDistrictId(Long townshipDistrictId) {
        this.townshipDistrictId = townshipDistrictId;
    }

    public Long getVillageDistrictId() {
        return villageDistrictId;
    }

    public void setVillageDistrictId(Long villageDistrictId) {
        this.villageDistrictId = villageDistrictId;
    }

    public Long getGroupDistrictId() {
        return groupDistrictId;
    }

    public void setGroupDistrictId(Long groupDistrictId) {
        this.groupDistrictId = groupDistrictId;
    }

    public String getLivingState() {
        return livingState;
    }

    public void setLivingState(String livingState) {
        this.livingState = livingState == null ? null : livingState.trim();
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