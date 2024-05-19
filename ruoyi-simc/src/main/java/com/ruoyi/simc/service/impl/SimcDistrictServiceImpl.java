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

    public List<SimcDistrict> createSubordinateDistrictId(long districtId, Set<String> subordinateDistrictNameSet) throws Exception {
        return createSubordinateDistrictId(districtId, subordinateDistrictNameSet, null);
    }

    /**
     * @param districtId
     * @param subordinateDistrictNameSet
     * @throws Exception
     */
    public List<SimcDistrict> createSubordinateDistrictId(long districtId, Set<String> subordinateDistrictNameSet, Map<String, Integer> sortInfo) throws Exception {
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
                if (null != sortInfo && sortInfo.containsKey(newTargetSubordinateDistrictName)) {
                    newTargetSubordinateDistrict.setSortId(sortInfo.get(newTargetSubordinateDistrictName));
                } else {
                    newTargetSubordinateDistrict.setSortId(++sortId);
                }
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

    public String getDistrictName(Long townshipDistrictId, Long villageDistrictId, Long groupDistrictId, List<SimcDistrict> simcDistrictList) {
        SimcDistrict townshipDistrict = getSimcDistrict(simcDistrictList, townshipDistrictId);
        SimcDistrict villageDistrict = getSimcDistrict(simcDistrictList, villageDistrictId);
        SimcDistrict groupDistrict = getSimcDistrict(simcDistrictList, groupDistrictId);
        String townshipDistrictName = null == townshipDistrict ? StringUtils.EMPTY : townshipDistrict.getDistrictName();
        String villageDistrictName = (null == villageDistrict ? StringUtils.EMPTY : villageDistrict.getDistrictName());
        String groupDistrictName = null == groupDistrict ? StringUtils.EMPTY : groupDistrict.getDistrictName();
        if (StringUtils.isBlank(groupDistrictName)) {
            return townshipDistrictName + "/" + villageDistrictName;
        } else {
            return townshipDistrictName + "/" + villageDistrictName + "/" + groupDistrictName;
        }
    }

    public SimcDistrict getSimcDistrict(List<SimcDistrict> simcDistrictList, Long districtId) {
        if (null == districtId || districtId <= 0) {
            return null;
        }
        for (int i = 0; null != simcDistrictList && i < simcDistrictList.size(); i++) {
            if (districtId.longValue() == simcDistrictList.get(i).getDistrictId()) {
                return simcDistrictList.get(i);
            }
        }
        return null;
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

    /**
     * 查询行政区域管理数据
     *
     * @param district 行政区域信息
     * @return 行政区域信息集合
     */
    public List<SimcDistrict> selectSimcDistrictList(SimcDistrict district) {
        return this.simcDistrictMapper.selectDistrictList(district);
    }

    /**
     * 查询行政区域管理数据
     *
     * @param districtId
     * @return 行政区域信息集合
     */
    public SimcDistrict selectSimcDistrictByDistrictId(Long districtId) {
        if (null == districtId || districtId <= 0) {
            return null;
        }
        return this.simcDistrictMapper.selectByPrimaryKey(districtId);
    }

    /**
     * 新增保存行政区域信息
     *
     * @param district 行政区域
     * @return 结果
     */
    public int insertSimcDistrict(SimcDistrict district) throws Exception {
        if (null == district) {
            throw new Exception("参数非法");
        }
        if (!(null == district.getDistrictId() || district.getDistrictId() <= 0)) {
            throw new Exception("行政区域编码必须为空");
        }
        if (null == district.getParentDistrictId() || district.getParentDistrictId() <= 0) {
            throw new Exception("上级行政区域编码不能为空");
        }
        if (StringUtils.isBlank(district.getDistrictName())) {
            throw new Exception("行政区域名称不能为空");
        }
        SimcDistrict newDbParentDistrict = this.simcDistrictMapper.selectByPrimaryKey(district.getParentDistrictId());
        if (null == newDbParentDistrict) {
            throw new Exception("无法获取到上级行政区域信息");
        }

        List<String> districtNames = new ArrayList<>();
        districtNames.add(district.getDistrictName().trim());
        List<SimcDistrict> childDistricts = this.simcDistrictMapper.selectByParentDistrictIdAndDistrictNames(district.getParentDistrictId(), districtNames);
        if (null != childDistricts && childDistricts.size() > 0) {
            throw new Exception("上级行政区域：【" + newDbParentDistrict.getDistrictName() + "】已经存在名称为【" + district.getDistrictName() + "】的行政区域，不允许添加！");
        }

        Map<String, Integer> sortInfo = new HashMap<>();
        sortInfo.put(district.getDistrictName().trim(), district.getSortId());
        createSubordinateDistrictId(district.getParentDistrictId(), new HashSet<>(districtNames), sortInfo);
        return 1;
    }

    /**
     * 新增保存行政区域信息
     *
     * @param district 行政区域
     * @return 结果
     */
    public int updateSimcDistrict(SimcDistrict district) throws Exception {
        if (null == district) {
            throw new Exception("参数非法");
        }
        if (null == district.getDistrictId() || district.getDistrictId() <= 0) {
            throw new Exception("行政区域编码不能为空");
        }
        if (null == district.getParentDistrictId() || district.getParentDistrictId() <= 0) {
            throw new Exception("上级行政区域编码不能为空");
        }
        if (StringUtils.isBlank(district.getDistrictName())) {
            throw new Exception("行政区域名称不能为空");
        }

        SimcDistrict dbDistrict = this.simcDistrictMapper.selectByPrimaryKey(district.getDistrictId());
        if (null == dbDistrict) {
            throw new Exception("根据行政编码查询不到行政编码数据");
        }

        SimcDistrict newDbParentDistrict = this.simcDistrictMapper.selectByPrimaryKey(district.getParentDistrictId());
        if (null == newDbParentDistrict) {
            throw new Exception("无法获取到上级行政区域信息");
        }

        if (dbDistrict.getParentDistrictId().longValue() != district.getParentDistrictId()) {
            // 说明变更了上级行政区域, 此时需要校验
            SimcDistrict oldDbParentDistrict = this.simcDistrictMapper.selectByPrimaryKey(dbDistrict.getParentDistrictId());
            if (null == oldDbParentDistrict) {
                throw new Exception("无法获取到原上级行政区域信息");
            }
            if (oldDbParentDistrict.getDistrictType().intValue() != newDbParentDistrict.getDistrictType()) {
                throw new Exception("当前选择的上级行政区域：【" + newDbParentDistrict.getDistrictName()
                        + "】类型为：【" + districtTypeName(newDbParentDistrict.getDistrictType())
                        + "】，不满足条件！<br/>"
                        + "请选择行政区域类型为：【" + districtTypeName(oldDbParentDistrict.getDistrictType())
                        + "】的行政区域作为【" + district.getDistrictName() + "】的上级行政区域！");
            }
        }

        if (!district.getDistrictName().trim().equals(dbDistrict.getDistrictName())) {// 说明改了名字, 判断parentDistrictId下面是否已经存在相同名字的下级行政区域
            List<String> districtNames = new ArrayList<>();
            districtNames.add(district.getDistrictName());
            List<SimcDistrict> childDistricts = this.simcDistrictMapper.selectByParentDistrictIdAndDistrictNames(district.getParentDistrictId(), districtNames);
            if (null != childDistricts && childDistricts.size() > 0) {
                throw new Exception("上级行政区域：【" + newDbParentDistrict.getDistrictName() + "】已经存在名称为【" + district.getDistrictName() + "】的行政区域，不允许将当前行政区域名称修改成【" + district.getDistrictName() + "】");
            }
        }
        district.setDistrictName(district.getDistrictName().trim());
        this.simcDistrictMapper.updateByPrimaryKeySelective(district);
        return 1;
    }

    private String districtTypeName(int districtType) throws Exception {
        if (1 == districtType) {
            return "省(含：直辖市)";
        } else if (2 == districtType) {
            return "市(含：直辖市市辖区)";
        } else if (3 == districtType) {
            return "区县";
        } else if (4 == districtType) {
            return "乡镇";
        } else if (5 == districtType) {
            return "村";
        } else if (6 == districtType) {
            return "组";
        } else {
            throw new Exception("无效的行政区域类型");
        }
    }

    /**
     * 删除行政区域管理信息
     *
     * @param districtId 行政区域ID
     * @return 结果
     */
    public int deleteDistrictById(Long districtId) throws Exception {
        if (null == districtId || districtId <= 0) {
            throw new Exception("行政区域编码不能为空");
        }
        SimcDistrict simcDistrict = this.simcDistrictMapper.selectByPrimaryKey(districtId);
        if (null == simcDistrict) {
            throw new Exception("根据行政编码查询不到行政编码数据");
        }
        if (simcDistrict.getDistrictType() != 6) {
            throw new Exception("系统仅支持删除组行政区域");
        }
        // 查询是否存在有效数据
        return 1;
    }
}
