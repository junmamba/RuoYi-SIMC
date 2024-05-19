package com.ruoyi.simc.mapper;

import com.ruoyi.common.core.domain.entity.SysDept;
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

    List<SimcDistrict> selectByDistrictIdList(@Param("districtIdList") List<Long> districtIdList);

    List<SimcDistrict> selectByParentDistrictIdList(@Param("parentDistrictIdList") List<Long> parentDistrictIdList);

    public List<SimcDistrict> selectDistrictList(SimcDistrict district);
}