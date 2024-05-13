package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcResidentSocialInsurance;

import java.util.List;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-03 23:20
 */
public interface ISimcResidentSocialInsuranceService {
    /**
     * 查询居民社会保险信息
     *
     * @param rsi 岗位信息
     * @return 居民社会保险列表
     */
    public List<SimcResidentSocialInsurance> selectList(SimcResidentSocialInsurance rsi) throws Exception;
}
