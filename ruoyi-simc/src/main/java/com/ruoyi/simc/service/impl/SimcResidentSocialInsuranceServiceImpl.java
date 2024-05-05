package com.ruoyi.simc.service.impl;

import com.ruoyi.simc.domain.SimcResidentSocialInsurance;
import com.ruoyi.simc.mapper.SimcResidentSocialInsuranceMapper;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.simc.service.ISimcResidentSocialInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 11:13
 */
@Service
public class SimcResidentSocialInsuranceServiceImpl implements ISimcResidentSocialInsuranceService {
    @Autowired
    private SimcResidentSocialInsuranceMapper simcResidentSocialInsuranceMapper;

    @Autowired
    private ISimcDistrictService simcDistrictService;

    @Override
    public List<SimcResidentSocialInsurance> selectList(SimcResidentSocialInsurance rsi) throws Exception {
        Map<String, Long> districtParam = this.simcDistrictService.buildQueryDistrictParam(rsi.getDistrictId());
        if (districtParam.containsKey("townshipDistrictId")){
            rsi.setResidentTownshipDistrictId(districtParam.get("townshipDistrictId"));
        }
        if (districtParam.containsKey("villageDistrictId")){
            rsi.setResidentVillageDistrictId(districtParam.get("villageDistrictId"));
        }
        if (districtParam.containsKey("groupDistrictId")){
            rsi.setResidentGroupDistrictId(districtParam.get("groupDistrictId"));
        }
        return this.simcResidentSocialInsuranceMapper.selectList(rsi);
    }
}
