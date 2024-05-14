package com.ruoyi.simc.service.impl;

import com.ruoyi.simc.domain.SimcFamilyLandLosing;
import com.ruoyi.simc.mapper.SimcFamilyLandLosingMapper;
import com.ruoyi.simc.service.ISimcFamilyLandLosingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-14 18:12
 */
@Service
public class SimcFamilyLandLosingServiceImpl implements ISimcFamilyLandLosingService {
    @Autowired
    private SimcFamilyLandLosingMapper simcFamilyLandLosingMapper;

    @Override
    public List<SimcFamilyLandLosing> selectList(SimcFamilyLandLosing fll) throws Exception {
        return this.simcFamilyLandLosingMapper.selectList(fll);
    }
}
