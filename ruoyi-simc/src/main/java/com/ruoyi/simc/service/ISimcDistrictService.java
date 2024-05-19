package com.ruoyi.simc.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.simc.domain.SimcDistrict;

import java.util.List;
import java.util.Map;
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

    /**
     * 查询行政区域
     *
     * @param districtIdList
     * @return
     * @throws Exception
     */
    public List<SimcDistrict> queryByDistrictIdList(List<Long> districtIdList) throws Exception;

    public String getDistrictName(Long townshipDistrictId, Long villageDistrictId, Long groupDistrictId, List<SimcDistrict> simcDistrictList);

    public SimcDistrict getSimcDistrict(List<SimcDistrict> simcDistrictList, Long districtId);

    /**
     * 查询柴桑区
     *
     * @return
     * @throws Exception
     */
    public List<TreeSelect> queryChaiSangSubordinateDistrict() throws Exception;

    public Map<String, Long> buildQueryDistrictParam(Long districtId) throws Exception;

    /**
     * 查询行政区域管理数据
     *
     * @param district 行政区域信息
     * @return 行政区域信息集合
     */
    public List<SimcDistrict> selectSimcDistrictList(SimcDistrict district);

    /**
     * 查询行政区域管理数据
     *
     * @param districtId
     * @return 行政区域信息集合
     */
    public SimcDistrict selectSimcDistrictByDistrictId(Long districtId);

    /**
     * 新增保存行政区域信息
     *
     * @param district 行政区域
     * @return 结果
     */
    public int insertSimcDistrict(SimcDistrict district) throws Exception;

    /**
     * 新增保存行政区域信息
     *
     * @param district 行政区域
     * @return 结果
     */
    public int updateSimcDistrict(SimcDistrict district) throws Exception;

    /**
     * 删除行政区域管理信息
     *
     * @param districtId 行政区域ID
     * @return 结果
     */
    public int deleteDistrictById(Long districtId) throws Exception;
}
