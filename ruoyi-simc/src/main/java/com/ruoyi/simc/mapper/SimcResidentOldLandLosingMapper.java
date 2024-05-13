package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcResidentOldLandLosing;

public interface SimcResidentOldLandLosingMapper {
    int deleteByPrimaryKey(String residentIdCardNo);

    int insert(SimcResidentOldLandLosing record);

    int insertSelective(SimcResidentOldLandLosing record);

    SimcResidentOldLandLosing selectByPrimaryKey(String residentIdCardNo);

    int updateByPrimaryKeySelective(SimcResidentOldLandLosing record);

    int updateByPrimaryKey(SimcResidentOldLandLosing record);
}