package com.ruoyi.simc.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SimcDistrict extends BaseEntity {
    private Long districtId;

    private String districtName;

    private Long parentDistrictId;

    private Integer districtType;

    private String areaCode;

    private Integer sortId;

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
        this.districtName = districtName == null ? null : districtName.trim();
    }

    public Long getParentDistrictId() {
        return parentDistrictId;
    }

    public void setParentDistrictId(Long parentDistrictId) {
        this.parentDistrictId = parentDistrictId;
    }

    public Integer getDistrictType() {
        return districtType;
    }

    public void setDistrictType(Integer districtType) {
        this.districtType = districtType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }
}