package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcResident;

public interface SimcResidentMapper {
    int deleteByPrimaryKey(String idCardNo);

    int insert(SimcResident record);

    int insertSelective(SimcResident record);

    SimcResident selectByPrimaryKey(String idCardNo);

    int updateByPrimaryKeySelective(SimcResident record);

    int updateByPrimaryKey(SimcResident record);
}