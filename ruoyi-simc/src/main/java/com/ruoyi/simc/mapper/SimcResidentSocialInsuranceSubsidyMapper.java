package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcResidentSocialInsuranceSubsidy;

import java.util.List;

public interface SimcResidentSocialInsuranceSubsidyMapper {
    int deleteByPrimaryKey(Long subsidyLogId);

    int insert(SimcResidentSocialInsuranceSubsidy record);

    int insertSelective(SimcResidentSocialInsuranceSubsidy record);

    SimcResidentSocialInsuranceSubsidy selectByPrimaryKey(Long subsidyLogId);

    int updateByPrimaryKeySelective(SimcResidentSocialInsuranceSubsidy record);

    int updateByPrimaryKey(SimcResidentSocialInsuranceSubsidy record);

    List<SimcResidentSocialInsuranceSubsidy> selectList(SimcResidentSocialInsuranceSubsidy record);

    int updateByResidentIdCardNoSelective(SimcResidentSocialInsuranceSubsidy record);
}