package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcFamilyLandLosing;

import java.util.List;

public interface ISimcFamilyLandLosingService {
    /**
     * 查询家庭被征地信息
     *
     * @param fll 查询条件
     * @return 家庭被征地列表
     */
    public List<SimcFamilyLandLosing> selectList(SimcFamilyLandLosing fll) throws Exception;
}
