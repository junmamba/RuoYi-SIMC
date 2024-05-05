package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcFamilyMember;
import com.ruoyi.simc.domain.SimcFamilyMemberKey;

import java.util.List;

public interface SimcFamilyMemberMapper {
    int deleteByPrimaryKey(SimcFamilyMemberKey key);

    int insert(SimcFamilyMember record);

    int insertSelective(SimcFamilyMember record);

    SimcFamilyMember selectByPrimaryKey(SimcFamilyMemberKey key);

    int updateByPrimaryKeySelective(SimcFamilyMember record);

    int updateByPrimaryKey(SimcFamilyMember record);

    List<SimcFamilyMember> selectByFamilyNo(String familyNo);
}