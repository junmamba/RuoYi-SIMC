package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcResidentSocialInsurance;

import java.util.List;

public interface SimcResidentSocialInsuranceMapper {
    int deleteByPrimaryKey(String residentIdCardNo);

    int insert(SimcResidentSocialInsurance record);

    int insertSelective(SimcResidentSocialInsurance record);

    SimcResidentSocialInsurance selectByPrimaryKey(String residentIdCardNo);

    int updateByPrimaryKeySelective(SimcResidentSocialInsurance record);

    int updateByPrimaryKey(SimcResidentSocialInsurance record);

    List<SimcResidentSocialInsurance> selectList(SimcResidentSocialInsurance record);

}