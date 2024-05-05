package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcLandAcquisitionProject;

public interface SimcLandAcquisitionProjectMapper {
    int deleteByPrimaryKey(Integer projectId);

    int insert(SimcLandAcquisitionProject record);

    int insertSelective(SimcLandAcquisitionProject record);

    SimcLandAcquisitionProject selectByPrimaryKey(Integer projectId);

    int updateByPrimaryKeySelective(SimcLandAcquisitionProject record);

    int updateByPrimaryKey(SimcLandAcquisitionProject record);

    SimcLandAcquisitionProject selectByProjectName(String projectName);
}