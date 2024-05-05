package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcFamily;

public interface SimcFamilyMapper {
    int deleteByPrimaryKey(String familyNo);

    int insert(SimcFamily record);

    int insertSelective(SimcFamily record);

    SimcFamily selectByPrimaryKey(String familyNo);

    int updateByPrimaryKeySelective(SimcFamily record);

    int updateByPrimaryKey(SimcFamily record);
}