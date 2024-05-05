package com.ruoyi.simc.service.impl;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.simc.domain.SimcDistrict;
import com.ruoyi.simc.mapper.SimcDistrictMapper;
import com.ruoyi.simc.service.ISimcDistrictService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 20:59
 */
@Transactional
@Service
public class SimcDistrictServiceImpl implements ISimcDistrictService {
    private static final int district_type_1 = 1;// 省(含：直辖市)
    private static final int district_type_2 = 2;// 市(含：直辖市市辖区)
    private static final int district_type_3 = 3;// 区县
    private static final int district_type_4 = 4;// 乡镇
    private static final int district_type_5 = 5;// 村
    private static final int district_type_6 = 6;// 组

    private Object lock = new Object();

    @Autowired
    private SimcDistrictMapper simcDistrictMapper;

    /**
     * @param districtId
     * @param subordinateDistrictNameSet
     * @throws Exception
     */
    public List<SimcDistrict> createSubordinateDistrictId(long districtId, Set<String> subordinateDistrictNameSet) throws Exception {
        if (CollectionUtils.isEmpty(subordinateDistrictNameSet)) {
            throw new Exception("需要添加的下级行政区域为空");
        }
        SimcDistrict simcDistrict = simcDistrictMapper.selectByPrimaryKey(districtId);
        if (null == simcDistrict) {
            throw new Exception("根据条件查询不到行政区域");
        }
        if (district_type_1 == simcDistrict.getDistrictType()) {
            throw new Exception("<省(含：直辖市)>行政区域不允许添加下级行政区域");
        }
        if (district_type_2 == simcDistrict.getDistrictType()) {
            throw new Exception("<市(含：直辖市市辖区)>行政区域不允许添加下级行政区域");
        }
        if (district_type_6 == simcDistrict.getDistrictType()) {
            throw new Exception("<组>行政区域不允许添加下级行政区域");
        }

        List<SimcDistrict> resultList = new ArrayList<SimcDistrict>();
        synchronized (lock) {
            List<SimcDistrict> allSubordinateDistrictList = this.simcDistrictMapper.selectByParentDistrictIdAndDistrictNames(districtId, null);
            Set<String> allSubordinateDistrictIdSet = new HashSet<String>();
            int sortId = 0;
            for (SimcDistrict subordinateDistrict : allSubordinateDistrictList) {
                allSubordinateDistrictIdSet.add(String.valueOf(subordinateDistrict.getDistrictId()));
                if (subordinateDistrict.getSortId() > sortId) {
                    sortId = subordinateDistrict.getSortId();
                }
            }

            List<String> newTargetSubordinateDistrictNameList = new ArrayList<String>();
            Iterator<String> iterator = subordinateDistrictNameSet.iterator();
            while (iterator.hasNext()) {
                String subordinateDistrictName = iterator.next();
                SimcDistrict newSubordinateDistrict = null;
                for (SimcDistrict subordinateDistrict : allSubordinateDistrictList) {
                    if (subordinateDistrictName.equals(subordinateDistrict.getDistrictName())) {
                        newSubordinateDistrict = subordinateDistrict;
                        break;
                    }
                }
                if (null == newSubordinateDistrict) {
                    newTargetSubordinateDistrictNameList.add(subordinateDistrictName);
                } else {
                    resultList.add(newSubordinateDistrict);
                }
            }
            if (newTargetSubordinateDistrictNameList.size() == 0) {// 没有需要添加的，直接返回
                return resultList;
            }

            // districtId格式说明
            // 省(含：直辖市)：前两位
            // 市(含：直辖市市辖区)：第3、4位
            // 区县：第5、6位
            // 乡镇：第7、8、9位
            // 村：第10、11、12位
            // 组：第13、14、15位
            int bit = 3;

            int startIndex = 0;
            String strDistrictId = String.valueOf(districtId);

            for (String newTargetSubordinateDistrictName : newTargetSubordinateDistrictNameList) {
                String strSubordinateDistrictId = null;

                while (true) {
                    startIndex++;
                    if (startIndex > 1000) {
                        throw new Exception("生成行政区域编号失败");
                    }
                    if (district_type_3 == simcDistrict.getDistrictType()) {// 如果是<区县>添加下级行政区域，直接在村后面加
                        strSubordinateDistrictId = strDistrictId.substring(0, 6) + StringUtils.leftPad(String.valueOf(startIndex), bit, '0') + "000";
                    } else if (district_type_4 == simcDistrict.getDistrictType()) {// 如果是<乡镇>添加下级行政区域，直接在村后面加
                        strSubordinateDistrictId = strDistrictId.substring(0, 9) + StringUtils.leftPad(String.valueOf(startIndex), bit, '0');
                    } else if (district_type_5 == simcDistrict.getDistrictType()) {// 如果是<村>添加下级行政区域，直接在村后面加
                        strSubordinateDistrictId = strDistrictId + StringUtils.leftPad(String.valueOf(startIndex), bit, '0');
                    }
                    if (!allSubordinateDistrictIdSet.contains(strSubordinateDistrictId)) {
                        break;
                    }
                }
                allSubordinateDistrictIdSet.add(strSubordinateDistrictId);

                SimcDistrict newTargetSubordinateDistrict = new SimcDistrict();
                newTargetSubordinateDistrict.setDistrictId(Long.parseLong(strSubordinateDistrictId));
                newTargetSubordinateDistrict.setDistrictName(newTargetSubordinateDistrictName);
                newTargetSubordinateDistrict.setParentDistrictId(districtId);
                newTargetSubordinateDistrict.setDistrictType(simcDistrict.getDistrictType() + 1);
                newTargetSubordinateDistrict.setAreaCode(simcDistrict.getAreaCode());
                newTargetSubordinateDistrict.setSortId(++sortId);
                this.simcDistrictMapper.insert(newTargetSubordinateDistrict);
                resultList.add(newTargetSubordinateDistrict);
            }
        }

        return resultList;
    }

    @Override
    public List<SimcDistrict> queryByParentDistrictIdAndDistrictNames(long parentDistrictId, List<String> townshipDistrictNameList) throws Exception {
        return this.simcDistrictMapper.selectByParentDistrictIdAndDistrictNames(parentDistrictId, townshipDistrictNameList);
    }

    /**
     * 查询行政区域
     *
     * @param districtIdList
     * @return
     * @throws Exception
     */
    public List<SimcDistrict> queryByDistrictIdList(List<Long> districtIdList) throws Exception {
        if (CollectionUtils.isEmpty(districtIdList)) {
            return null;
        }
        return this.simcDistrictMapper.selectByDistrictIdList(districtIdList);
    }

    /**
     * 查询柴桑区下级行政区域
     *
     * @return
     * @throws Exception
     */
    public List<TreeSelect> queryChaiSangSubordinateDistrict() throws Exception {
        // @part 1: 所有乡镇
        List<SimcDistrict> townshipDistrictList = this.simcDistrictMapper.selectByParentDistrictIdAndDistrictNames(CHAI_SANG_TOWNSHIP_DISTRICT_ID, null);
        if (CollectionUtils.isEmpty(townshipDistrictList)) {
            return new ArrayList<>();
        }
        List<Long> townshipDistrictIdList = new ArrayList<>();
        for (SimcDistrict townshipDistrict : townshipDistrictList) {
            townshipDistrictIdList.add(townshipDistrict.getDistrictId());
        }

        // @part 2: 所有村（社区）
        List<SimcDistrict> villageDistrictList = this.simcDistrictMapper.selectByParentDistrictIdList(townshipDistrictIdList);
        List<Long> villageDistrictIdList = new ArrayList<>();
        for (SimcDistrict villageDistrict : villageDistrictList) {
            villageDistrictIdList.add(villageDistrict.getDistrictId());
        }

        // @part 3: 所有组
        List<SimcDistrict> groupDistrictList = this.simcDistrictMapper.selectByParentDistrictIdList(villageDistrictIdList);

        List<TreeSelect> treeList = new ArrayList<>();
        for (SimcDistrict townshipDistrict : townshipDistrictList) {
            TreeSelect tree = new TreeSelect();
            treeList.add(tree);
            tree.setId(townshipDistrict.getDistrictId());
            tree.setLabel(townshipDistrict.getDistrictName());

            List<TreeSelect> villageTreeList = new ArrayList<>();
            tree.setChildren(villageTreeList);
            for (SimcDistrict villageDistrict : villageDistrictList) {
                if (villageDistrict.getParentDistrictId().longValue() != townshipDistrict.getDistrictId()) {
                    continue;
                }
                TreeSelect villageTree = new TreeSelect();
                villageTree.setId(villageDistrict.getDistrictId());
                villageTree.setLabel(villageDistrict.getDistrictName());
                villageTreeList.add(villageTree);
                List<TreeSelect> groupTreeList = new ArrayList<>();
                villageTree.setChildren(groupTreeList);
                for (SimcDistrict groupDistrict : groupDistrictList) {
                    if (groupDistrict.getParentDistrictId().longValue() != villageDistrict.getDistrictId()) {
                        continue;
                    }
                    TreeSelect groupTree = new TreeSelect();
                    groupTree.setId(groupDistrict.getDistrictId());
                    groupTree.setLabel(groupDistrict.getDistrictName());
                    groupTreeList.add(groupTree);
                }
            }
        }
        return treeList;
    }

    public Map<String, Long> buildQueryDistrictParam(Long districtId) throws Exception {
        Map<String, Long> param = new HashMap();
        if (null == districtId) {
            return param;
        }

        SimcDistrict simcDistrict = this.simcDistrictMapper.selectByPrimaryKey(districtId);
        if (null == simcDistrict
                || !(simcDistrict.getDistrictType() == 4 || simcDistrict.getDistrictType() == 5 || simcDistrict.getDistrictType() == 6)) {
            param.put("townshipDistrictId", -1L);
            param.put("villageDistrictId", -1L);
            param.put("groupDistrictId", -1L);
        }
        if (simcDistrict.getDistrictType() == 4) {
            param.put("townshipDistrictId", districtId);
        } else if (simcDistrict.getDistrictType() == 5) {
            param.put("townshipDistrictId", simcDistrict.getParentDistrictId());
            param.put("villageDistrictId", simcDistrict.getDistrictId());
        } else if (simcDistrict.getDistrictType() == 6) {
            SimcDistrict villageSimcDistrict = this.simcDistrictMapper.selectByPrimaryKey(simcDistrict.getParentDistrictId());
            param.put("townshipDistrictId", null == villageSimcDistrict ? -1L : villageSimcDistrict.getParentDistrictId());
            param.put("villageDistrictId", simcDistrict.getParentDistrictId());
            param.put("groupDistrictId", districtId);
        }
        return param;
    }
}
