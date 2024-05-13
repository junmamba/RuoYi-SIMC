package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcResidentSocialInsuranceImportRowData;
import com.ruoyi.simc.domain.SimcResidentSocialInsuranceSubsidyImportRowData;

import java.util.List;

public interface ISimcImportService {
    /**
     * 导入居民社会保险数据列表
     *
     * @param importRowDataList 数据列表
     * @param userId            是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importResidentSocialInsuranceDataList(List<SimcResidentSocialInsuranceImportRowData> importRowDataList, Long userId) throws Exception;

    /**
     * 导入居民社会保险补贴数据列表
     *
     * @param importRowDataList 数据列表
     * @param userId            是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importResidentSocialInsuranceSubsidyDataList(List<SimcResidentSocialInsuranceSubsidyImportRowData> importRowDataList, Long userId) throws Exception;

}
