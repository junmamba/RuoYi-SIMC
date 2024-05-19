package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcOldAgriculturalInsuranceCancel;

import java.util.List;

public interface ISimcOldAgriculturalInsuranceCancelService {
    /**
     * 查询老农保退保信息
     *
     * @param rsi
     * @return 老农保退保列表
     */
    public List<SimcOldAgriculturalInsuranceCancel> selectList(SimcOldAgriculturalInsuranceCancel rsi) throws Exception;

    /**
     * 查询老农保退保档案
     *
     * @param residentIdCardNo
     * @return
     * @throws Exception
     */
    public SimcOldAgriculturalInsuranceCancel selectByResidentIdCardNo(String residentIdCardNo) throws Exception;

    /**
     * 操作老农保退保档案
     *
     * @param * @throws Exception
     * @return
     * @throws Exception
     */
    public void oper(SimcOldAgriculturalInsuranceCancel oldAgriculturalInsuranceCancel, Long userId) throws Exception;

    /**
     * 批量删除
     *
     * @param residentIdCardNos
     * @return 结果
     */
    public int delete(String[] residentIdCardNos);
}
