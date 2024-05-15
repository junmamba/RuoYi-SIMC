package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcFamilyLandLosing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimcFamilyLandLosingMapper {
    int deleteByPrimaryKey(Integer fllId);

    int insert(SimcFamilyLandLosing record);

    int insertSelective(SimcFamilyLandLosing record);

    SimcFamilyLandLosing selectByPrimaryKey(Integer fllId);

    int updateByPrimaryKeySelective(SimcFamilyLandLosing record);

    int updateByPrimaryKey(SimcFamilyLandLosing record);

    List<SimcFamilyLandLosing> selectByFamilyNo(@Param("familyNo") String familyNo, @Param("projectId") Integer projectId);

    List<SimcFamilyLandLosing> selectList(SimcFamilyLandLosing record);

    int updateByFamilyNoSelective(SimcFamilyLandLosing record);

}