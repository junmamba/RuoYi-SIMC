package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcResidentOldLandLosing;

import java.util.List;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-14 09:46
 */
public interface ISimcResidentOldLandLosingService {
    /**
     * 查询居民老失地档案列表
     *
     * @param roll 参数
     * @return 居民老失地档案列表
     */
    public List<SimcResidentOldLandLosing> selectList(SimcResidentOldLandLosing roll) throws Exception;
}
