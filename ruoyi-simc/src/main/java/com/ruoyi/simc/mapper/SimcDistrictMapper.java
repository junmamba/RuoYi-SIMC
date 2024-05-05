package com.ruoyi.simc.mapper;

import com.ruoyi.simc.domain.SimcDistrict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimcDistrictMapper {
    int deleteByPrimaryKey(Long districtId);

    int insert(SimcDistrict record);

    int insertSelective(SimcDistrict record);

    SimcDistrict selectByPrimaryKey(Long districtId);

    int updateByPrimaryKeySelective(SimcDistrict record);

    int updateByPrimaryKey(SimcDistrict record);

    List<SimcDistrict> selectByParentDistrictIdAndDistrictNames(@Param("parentDistrictId") Long parentDistrictId, @Param("districtNames") List<String> districtNames);
}