package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcOldAgriculturalInsuranceCancel;

import java.util.List;

public interface SimcOldAgriculturalInsuranceCancelMapper {
    int deleteByPrimaryKey(String residentIdCardNo);

    int insert(SimcOldAgriculturalInsuranceCancel record);

    int insertSelective(SimcOldAgriculturalInsuranceCancel record);

    SimcOldAgriculturalInsuranceCancel selectByPrimaryKey(String residentIdCardNo);

    int updateByPrimaryKeySelective(SimcOldAgriculturalInsuranceCancel record);

    int updateByPrimaryKey(SimcOldAgriculturalInsuranceCancel record);

    List<SimcOldAgriculturalInsuranceCancel> selectList(SimcOldAgriculturalInsuranceCancel record);
}