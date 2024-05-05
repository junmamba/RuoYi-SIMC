package com.ruoyi.simc.domain;

public class SimcLandAcquisitionProject {
    private Integer projectId;

    private String projectName;

    private Integer isCityLevel;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Integer getIsCityLevel() {
        return isCityLevel;
    }

    public void setIsCityLevel(Integer isCityLevel) {
        this.isCityLevel = isCityLevel;
    }
}