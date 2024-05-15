package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcResidentSocialInsuranceSubsidy;

import java.util.List;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-11 21:09
 */
public interface ISimcResidentSocialInsuranceSubsidyService {
    /**
     * 查询社保补贴信息
     *
     * @param rsis 参数
     * @return 居民社会保险补贴列表
     */
    public List<SimcResidentSocialInsuranceSubsidy> selectList(SimcResidentSocialInsuranceSubsidy rsis) throws Exception;

    /**
     * 批量删除
     *
     * @param subsidyLogIds
     * @return 结果
     */
    public int delete(Long[] subsidyLogIds);
}
