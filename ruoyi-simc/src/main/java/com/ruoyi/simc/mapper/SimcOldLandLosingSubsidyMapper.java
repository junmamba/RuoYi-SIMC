package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcOldLandLosingSubsidy;

public interface SimcOldLandLosingSubsidyMapper {
    int deleteByPrimaryKey(Integer residentIdCardNo);

    int insert(SimcOldLandLosingSubsidy record);

    int insertSelective(SimcOldLandLosingSubsidy record);

    SimcOldLandLosingSubsidy selectByPrimaryKey(Integer residentIdCardNo);

    int updateByPrimaryKeySelective(SimcOldLandLosingSubsidy record);

    int updateByPrimaryKey(SimcOldLandLosingSubsidy record);
}