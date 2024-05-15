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

    /**
     * 查询居民老失地档案
     *
     * @param residentIdCardNo
     * @return
     * @throws Exception
     */
    public SimcResidentOldLandLosing selectByResidentIdCardNo(String residentIdCardNo) throws Exception;

    /**
     * 操作居民老失地档案
     *
     * @param simcResidentOldLandLosing
     * @return
     * @throws Exception
     */
    public void oper(SimcResidentOldLandLosing simcResidentOldLandLosing, Long userId) throws Exception;

    /**
     * 批量删除
     *
     * @param residentIdCardNos
     * @return 结果
     */
    public int delete(String[] residentIdCardNos);
}
