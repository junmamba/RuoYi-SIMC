package com.ruoyi.simc.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.simc.domain.SimcDistrict;
import com.ruoyi.simc.domain.SimcResidentOldLandLosing;
import com.ruoyi.simc.mapper.SimcResidentOldLandLosingMapper;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.simc.service.ISimcResidentOldLandLosingService;
import com.ruoyi.simc.util.SimcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-14 09:47
 */
@Service
@Transactional
public class SimcResidentOldLandLosingServiceImpl implements ISimcResidentOldLandLosingService {
    @Autowired
    private SimcResidentOldLandLosingMapper simcResidentOldLandLosingMapper;
    @Autowired
    private ISimcDistrictService simcDistrictService;

    @Override
    public List<SimcResidentOldLandLosing> selectList(SimcResidentOldLandLosing roll) throws Exception {
        Map<String, Long> districtParam = this.simcDistrictService.buildQueryDistrictParam(roll.getDistrictId());
        if (districtParam.containsKey("townshipDistrictId")) {
            roll.setResidentTownshipDistrictId(districtParam.get("townshipDistrictId"));
        }
        if (districtParam.containsKey("villageDistrictId")) {
            roll.setResidentVillageDistrictId(districtParam.get("villageDistrictId"));
        }
        if (districtParam.containsKey("groupDistrictId")) {
            roll.setResidentGroupDistrictId(districtParam.get("groupDistrictId"));
        }
        if (roll.getParams().containsKey("beginPayTime")) {
            roll.getParams().put("beginPayTime", roll.getParams().get("beginPayTime") + "-01");
        }
        if (roll.getParams().containsKey("endPayTime")) {
            roll.getParams().put("endPayTime", roll.getParams().get("endPayTime") + "-28");
        }
        List<SimcResidentOldLandLosing> list = this.simcResidentOldLandLosingMapper.selectList(roll);
        Set<Long> districtIds = new HashSet<>();
        for (int i = 0; null != list && i < list.size(); i++) {
            districtIds.add(list.get(i).getResidentTownshipDistrictId());
            districtIds.add(list.get(i).getResidentVillageDistrictId());
            districtIds.add(list.get(i).getResidentGroupDistrictId());
        }
        Date now = DateUtils.getNowDate();
        List<SimcDistrict> simcDistrictList = this.simcDistrictService.queryByDistrictIdList(new ArrayList<>(districtIds));
        for (int i = 0; null != list && i < list.size(); i++) {
            list.get(i).setDistrictName(simcDistrictService.getDistrictName(list.get(i).getResidentTownshipDistrictId(), list.get(i).getResidentVillageDistrictId(), list.get(i).getResidentGroupDistrictId(), simcDistrictList));
            if (null != list.get(i).getPayTime()) {
                list.get(i).setStrPayTime(DateUtils.parseDateToStr(DateUtils.YYYYMM, list.get(i).getPayTime()));
            }
            if (null != list.get(i).getTheFirstReceiveTime()) {
                list.get(i).setStrTheFirstReceiveTime(DateUtils.parseDateToStr(DateUtils.YYYYMM, list.get(i).getTheFirstReceiveTime()));
            }
            if (null != list.get(i).getQuitTime()) {
                list.get(i).setStrQuitTime(DateUtils.parseDateToStr(DateUtils.YYYYMMDD, list.get(i).getQuitTime()));
            }
            double receivedTotalFee = 0;
            if (null != list.get(i).getQuitTime()) {
                receivedTotalFee = SimcUtil.calReceivedTotalFee(list.get(i).getTheFirstReceiveTime(), list.get(i).getQuitTime(), SimcUtil.getTheFirstYearPerMonthReceiveMoney(list.get(i).getPayLevel()));
            } else {
                receivedTotalFee = SimcUtil.calReceivedTotalFee(list.get(i).getTheFirstReceiveTime(), now, SimcUtil.getTheFirstYearPerMonthReceiveMoney(list.get(i).getPayLevel()));
            }
            list.get(i).setReceivedTotalFee(receivedTotalFee);
        }
        return list;
    }
}
