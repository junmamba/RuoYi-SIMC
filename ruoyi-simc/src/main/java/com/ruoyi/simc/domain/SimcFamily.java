package com.ruoyi.simc.domain;

import java.util.Date;

public class SimcFamily {
    private String familyNo;

    private String familyName;

    private String familyHead;

    private Long townshipDistrictId;

    private Long villageDistrictId;

    private Long groupDistrictId;

    private String remark;

    private Long createUserId;

    private Date createTime;

    private Long modifyUserId;

    private Date modifyTime;

    public String getFamilyNo() {
        return familyNo;
    }

    public void setFamilyNo(String familyNo) {
        this.familyNo = familyNo == null ? null : familyNo.trim();
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName == null ? null : familyName.trim();
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead == null ? null : familyHead.trim();
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