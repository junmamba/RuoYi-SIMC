package com.ruoyi.simc.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simc.domain.SimcDistrict;
import com.ruoyi.simc.domain.SimcOldAgriculturalInsuranceCancel;
import com.ruoyi.simc.mapper.SimcOldAgriculturalInsuranceCancelMapper;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.simc.service.ISimcOldAgriculturalInsuranceCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-19 22:08
 */
@Service
@Transactional
public class SimcOldAgriculturalInsuranceCancelServiceImpl implements ISimcOldAgriculturalInsuranceCancelService {
    @Autowired
    private SimcOldAgriculturalInsuranceCancelMapper oldAgriculturalInsuranceCancelMapper;

    @Autowired
    private ISimcDistrictService simcDistrictService;

    @Override
    public List<SimcOldAgriculturalInsuranceCancel> selectList(SimcOldAgriculturalInsuranceCancel rsi) throws Exception {
        Map<String, Long> districtParam = this.simcDistrictService.buildQueryDistrictParam(rsi.getDistrictId());
        if (districtParam.containsKey("townshipDistrictId")) {
            rsi.setResidentTownshipDistrictId(districtParam.get("townshipDistrictId"));
        }
        if (districtParam.containsKey("villageDistrictId")) {
            rsi.setResidentVillageDistrictId(districtParam.get("villageDistrictId"));
        }
        List<SimcOldAgriculturalInsuranceCancel> list = this.oldAgriculturalInsuranceCancelMapper.selectList(rsi);
        Set<Long> districtIds = new HashSet<>();
        for (int i = 0; null != list && i < list.size(); i++) {
            districtIds.add(list.get(i).getResidentTownshipDistrictId());
            districtIds.add(list.get(i).getResidentVillageDistrictId());
        }
        List<SimcDistrict> simcDistrictList = this.simcDistrictService.queryByDistrictIdList(new ArrayList<>(districtIds));
        for (int i = 0; null != list && i < list.size(); i++) {
            list.get(i).setDistrictName(simcDistrictService.getDistrictName(list.get(i).getResidentTownshipDistrictId(), list.get(i).getResidentVillageDistrictId(), list.get(i).getResidentGroupDistrictId(), simcDistrictList));
            if (null != list.get(i).getCancelTime()) {
                list.get(i).setStrCancelTime(DateUtils.parseDateToStr(DateUtils.YYYYMMDD, list.get(i).getCancelTime()));
            }
        }
        return list;
    }

    @Override
    public SimcOldAgriculturalInsuranceCancel selectByResidentIdCardNo(String residentIdCardNo) throws Exception {
        if (StringUtils.isBlank(residentIdCardNo)) {
            return null;
        }
        return this.oldAgriculturalInsuranceCancelMapper.selectByPrimaryKey(residentIdCardNo);
    }

    @Override
    public void oper(SimcOldAgriculturalInsuranceCancel oldAgriculturalInsuranceCancel, Long userId) throws Exception {
        if (StringUtils.isBlank(oldAgriculturalInsuranceCancel.getResidentIdCardNo())) {
            throw new Exception("身份证号码不能为空");
        }
        if (oldAgriculturalInsuranceCancel.getResidentIdCardNo().length() != 18) {
            throw new Exception("身份证号码必须是18位");
        }
        if (StringUtils.isBlank(oldAgriculturalInsuranceCancel.getResidentName())) {
            throw new Exception("姓名不能为空");
        }
        if (StringUtils.isBlank(oldAgriculturalInsuranceCancel.getResidentPhone())) {
            throw new Exception("联系电话不能为空");
        }
        if (null == oldAgriculturalInsuranceCancel.getResidentTownshipDistrictId() || oldAgriculturalInsuranceCancel.getResidentTownshipDistrictId() <= 0) {
            throw new Exception("请选择乡镇");
        }
        if (null == oldAgriculturalInsuranceCancel.getResidentVillageDistrictId() || oldAgriculturalInsuranceCancel.getResidentVillageDistrictId() <= 0) {
            throw new Exception("请选择村（社区）");
        }
        if (null == oldAgriculturalInsuranceCancel.getMoney() || oldAgriculturalInsuranceCancel.getMoney() <= 0) {
            throw new Exception("金额");
        }
        if (StringUtils.isNotBlank(oldAgriculturalInsuranceCancel.getSurrogateIdCardNo()) && oldAgriculturalInsuranceCancel.getSurrogateIdCardNo().length() != 18) {
            throw new Exception("代领人身份证号码必须是18位");
        }
        SimcOldAgriculturalInsuranceCancel dbSimcOldAgriculturalInsuranceCancel = this.oldAgriculturalInsuranceCancelMapper.selectByPrimaryKey(oldAgriculturalInsuranceCancel.getResidentIdCardNo());
        if (null == dbSimcOldAgriculturalInsuranceCancel) {
            throw new Exception("根据身份证号码：" + dbSimcOldAgriculturalInsuranceCancel.getResidentIdCardNo() + "查询老农保退保档案数据");
        }

        SimcOldAgriculturalInsuranceCancel update = new SimcOldAgriculturalInsuranceCancel();
        update.setResidentIdCardNo(oldAgriculturalInsuranceCancel.getResidentIdCardNo());
        update.setResidentName(oldAgriculturalInsuranceCancel.getResidentName());
        update.setResidentPhone(oldAgriculturalInsuranceCancel.getResidentPhone());
        update.setResidentTownshipDistrictId(oldAgriculturalInsuranceCancel.getResidentTownshipDistrictId());
        update.setResidentVillageDistrictId(oldAgriculturalInsuranceCancel.getResidentVillageDistrictId());
        update.setMoney(oldAgriculturalInsuranceCancel.getMoney());
        update.setSurrogateName(oldAgriculturalInsuranceCancel.getSurrogateName());
        update.setSurrogateIdCardNo(oldAgriculturalInsuranceCancel.getSurrogateIdCardNo());
        update.setCancelTime(oldAgriculturalInsuranceCancel.getCancelTime());
        update.setBank(oldAgriculturalInsuranceCancel.getBank());
        update.setBankCode(oldAgriculturalInsuranceCancel.getBankCode());
        update.setRemark(oldAgriculturalInsuranceCancel.getRemark());
        update.setModifyUserId(userId);
        update.setModifyTime(DateUtils.getNowDate());
        this.oldAgriculturalInsuranceCancelMapper.updateByPrimaryKeySelective(update);
    }

    /**
     * 批量删除
     *
     * @param residentIdCardNos
     * @return 结果
     */
    public int delete(String[] residentIdCardNos) {
        for (int i = 0; null != residentIdCardNos && i < residentIdCardNos.length; i++) {
            this.oldAgriculturalInsuranceCancelMapper.deleteByPrimaryKey(residentIdCardNos[i]);
        }
        return 1;
    }
}
