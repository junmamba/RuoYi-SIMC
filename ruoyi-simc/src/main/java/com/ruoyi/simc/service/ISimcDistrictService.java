package com.ruoyi.simc.service;

import com.ruoyi.simc.domain.SimcDistrict;

import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 20:55
 */
public interface ISimcDistrictService {
    public static final long CHAI_SANG_TOWNSHIP_DISTRICT_ID = 360404000000L;
    public static final String CHAI_SANG_TOWNSHIP_DISTRICT_NAME = "柴桑区";

    /**
     * @param districtId
     * @param subordinateDistrictNameSet
     * @throws Exception
     */
    public List<SimcDistrict> createSubordinateDistrictId(long districtId, Set<String> subordinateDistrictNameSet) throws Exception;

    /**
     * 查询柴桑区乡镇行政区域列表
     *
     * @param townshipDistrictNameList
     * @return
     * @throws Exception
     */
    public List<SimcDistrict> queryByParentDistrictIdAndDistrictNames(long parentDistrictId, List<String> townshipDistrictNameList) throws Exception;
}
