package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcOldAgriculturalInsuranceCancelImportRowData;
import com.ruoyi.simc.domain.SimcResidentOldLandLosingImportRowData;
import com.ruoyi.simc.domain.SimcResidentSocialInsuranceImportRowData;
import com.ruoyi.simc.domain.SimcResidentSocialInsuranceSubsidyImportRowData;

import java.util.List;

public interface ISimcImportService {
    /**
     * 导入居民社会保险数据列表
     *
     * @param importRowDataList 数据列表
     * @param userId            操作员ID
     * @return 结果
     */
    public String importResidentSocialInsuranceDataList(List<SimcResidentSocialInsuranceImportRowData> importRowDataList, Long userId) throws Exception;

    /**
     * 导入居民社会保险补贴数据列表
     *
     * @param importRowDataList 数据列表
     * @param userId            操作员ID
     * @return 结果
     */
    public String importResidentSocialInsuranceSubsidyDataList(List<SimcResidentSocialInsuranceSubsidyImportRowData> importRowDataList, Long userId) throws Exception;

    /**
     * 导入居民老失地数据列表
     *
     * @param importRowDataList 数据列表
     * @param userId            操作员ID
     * @return 结果
     */
    public String importSimcResidentOldLandLosingDataList(List<SimcResidentOldLandLosingImportRowData> importRowDataList, Long userId) throws Exception;

    /**
     * 导入老农保退保列表
     *
     * @param importRowDataList 数据列表
     * @param userId            操作员ID
     * @return 结果
     */
    public String importSimcOldAgriculturalInsuranceCancelDataList(List<SimcOldAgriculturalInsuranceCancelImportRowData> importRowDataList, Long userId) throws Exception;
}
