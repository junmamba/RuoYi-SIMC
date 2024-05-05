package com.ruoyi.simc.service.impl;

import com.ruoyi.simc.domain.SimcDistrict;
import com.ruoyi.simc.domain.SimcResidentSocialInsurance;
import com.ruoyi.simc.mapper.SimcDistrictMapper;
import com.ruoyi.simc.mapper.SimcResidentSocialInsuranceMapper;
import com.ruoyi.simc.service.ISimcResidentSocialInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 11:13
 */
@Service
public class SimcResidentSocialInsuranceServiceImpl implements ISimcResidentSocialInsuranceService {
    @Autowired
    private SimcResidentSocialInsuranceMapper simcResidentSocialInsuranceMapper;

    @Override
    public List<SimcResidentSocialInsurance> selectList(SimcResidentSocialInsurance rsi) {
        return this.simcResidentSocialInsuranceMapper.selectList(rsi);
    }
}
