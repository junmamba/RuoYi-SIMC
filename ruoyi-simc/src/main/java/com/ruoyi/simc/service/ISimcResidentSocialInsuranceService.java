package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcResidentOldLandLosing;
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

    /**
     * 查询居民社会保险档案
     *
     * @param residentIdCardNo
     * @return
     * @throws Exception
     */
    public SimcResidentSocialInsurance selectByResidentIdCardNo(String residentIdCardNo) throws Exception;

    /**
     * 操作居民社会保险档案
     *
     * @param      * @throws Exception
     * @return
     * @throws Exception
     */
    public void oper(SimcResidentSocialInsurance residentSocialInsurance, Long userId) throws Exception;
}
