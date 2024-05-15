package com.ruoyi.simc.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.simc.domain.*;
import com.ruoyi.simc.mapper.*;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.simc.service.ISimcImportService;
import com.ruoyi.simc.util.SimcUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;

import java.util.*;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 16:52
 */
@Service
@Transactional
public class SimcImportServiceImpl implements ISimcImportService {
    @Autowired
    private SimcResidentSocialInsuranceMapper simcResidentSocialInsuranceMapper;

    @Autowired
    private SimcResidentMapper simcResidentMapper;

    @Autowired
    private SimcLandAcquisitionProjectMapper simcLandAcquisitionProjectMapper;

    @Autowired
    private ISimcDistrictService simcDistrictService;

    @Autowired
    private SimcFamilyMapper simcFamilyMapper;

    @Autowired
    private SimcFamilyMemberMapper simcFamilyMemberMapper;

    @Autowired
    private SimcFamilyLandLosingMapper simcFamilyLandLosingMapper;

    @Autowired
    private SimcResidentSocialInsuranceSubsidyMapper simcResidentSocialInsuranceSubsidyMapper;

    @Autowired
    private SimcResidentOldLandLosingMapper simcResidentOldLandLosingMapper;

    /**
     * 导入居民社会保险数据列表
     *
     * @param importRowDataList 数据列表
     * @param userId            是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importResidentSocialInsuranceDataList(List<SimcResidentSocialInsuranceImportRowData> importRowDataList, Long userId) throws Exception {
        Date currentDate = DateUtils.getNowDate();
        // @part 1: 梳理数据
        Map<String, Map<String, Object>> laProjectImportData = new HashMap<>();
        Map<String, Map<String, Object>> townshipDistrictMap = new HashMap<>();
        Map<String, Map<String, Object>> villageDistrictMap = new HashMap<>();
        Map<String, Map<String, Object>> groupDistrictMap = new HashMap<>();
        Map<String, List<SimcResidentSocialInsuranceImportRowData>> familyImportData = new HashMap<>();
        Map<Integer, List<String>> rowFailInfos = new LinkedHashMap<>();

        combImportData(importRowDataList, rowFailInfos, laProjectImportData, townshipDistrictMap, villageDistrictMap, groupDistrictMap, familyImportData);
        if (MapUtils.isNotEmpty(rowFailInfos)) {
            return wrapRowFailInfos(rowFailInfos, 1);
        }

        // @part 2: 生成数据
        // @part 2.1: 初始化行政区
        Map<String, Map<String, Object>> chaiSangQuDistrictMap = new HashMap<>();
        Map<String, Object> chaiSangQuDistrict = new HashMap<>();
        chaiSangQuDistrict.put("DistrictName", ISimcDistrictService.CHAI_SANG_TOWNSHIP_DISTRICT_NAME);
        chaiSangQuDistrict.put("DistrictId", ISimcDistrictService.CHAI_SANG_TOWNSHIP_DISTRICT_ID);
        chaiSangQuDistrictMap.put(ISimcDistrictService.CHAI_SANG_TOWNSHIP_DISTRICT_NAME, chaiSangQuDistrict);
        initAndSaveDistrict(townshipDistrictMap, chaiSangQuDistrictMap, "乡镇", "市(含：直辖市市辖区)", false);
        initAndSaveDistrict(villageDistrictMap, townshipDistrictMap, "村（社区）", "乡镇", false);
        initAndSaveDistrict(groupDistrictMap, villageDistrictMap, "组", "村（社区）", true);// 组不存在是，新增

        // @part 2.2: 生成项目数据
        Iterator<String> laProjectIterator = laProjectImportData.keySet().iterator();
        while (laProjectIterator.hasNext()) {
            String laProjectName = laProjectIterator.next();
            Map<String, Object> laProjectMap = laProjectImportData.get(laProjectName);
            SimcLandAcquisitionProject simcLandAcquisitionProject = simcLandAcquisitionProjectMapper.selectByProjectName(laProjectName);
            if (null == simcLandAcquisitionProject) {
                simcLandAcquisitionProject = new SimcLandAcquisitionProject();
                simcLandAcquisitionProject.setProjectName((String) laProjectMap.get("ProjectName"));
                simcLandAcquisitionProject.setIsCityLevel((Integer) laProjectMap.get("IsCityLevel"));
                simcLandAcquisitionProjectMapper.insert(simcLandAcquisitionProject);
            }
            laProjectMap.put("ProjectId", simcLandAcquisitionProject.getProjectId());
        }


        // @part 2.3: 保存家庭被征地信息
        Map<String, Integer> familyLandLosing = saveSimcFamilyLandLosing(familyImportData, townshipDistrictMap, villageDistrictMap, groupDistrictMap, laProjectImportData, userId, currentDate);
        StringBuilder simcResidentOldLandLosingSB = new StringBuilder();
        StringBuilder simcResidentSocialInsuranceSB = new StringBuilder();

        for (SimcResidentSocialInsuranceImportRowData importRowData : importRowDataList) {
            String residentIdCardNo = importRowData.getResidentIdCardNo();

            // @part 1: 居民信息表
            SimcResident simcResident = this.simcResidentMapper.selectByPrimaryKey(residentIdCardNo);
            if (null == simcResident) {
                simcResident = new SimcResident();
                simcResident.setIdCardNo(residentIdCardNo);
                simcResident.setName(importRowData.getResidentName());
                simcResident.setSex(SimcUtil.convertSex(importRowData.getResidentSex()));
                simcResident.setBirthDate(SimcUtil.getBirthDateFromIdCardNo(residentIdCardNo));
                simcResident.setPhone(importRowData.getResidentPhone());
                simcResident.setResideType("1");
                simcResident.setTownshipDistrictId(getDistrictId(townshipDistrictMap, importRowData.getResidentTownshipDistrictName()));
                simcResident.setVillageDistrictId(getDistrictId(villageDistrictMap, importRowData.getResidentTownshipDistrictName() + "_" + importRowData.getResidentVillageDistrictName()));
                simcResident.setGroupDistrictId(getDistrictId(groupDistrictMap, importRowData.getResidentTownshipDistrictName() + "_" + importRowData.getResidentVillageDistrictName() + "_" + importRowData.getResidentGroupDistrictName()));
                simcResident.setLivingState("1");// 存活
                simcResident.setCreateUserId(userId);
                simcResident.setCreateTime(currentDate);
                this.simcResidentMapper.insert(simcResident);
            } else {// 存在, 进行提示
                // ......
            }

            // @part 2: 居民社会保险信息表
            SimcResidentSocialInsurance simcResidentSocialInsurance = this.simcResidentSocialInsuranceMapper.selectByPrimaryKey(residentIdCardNo);
            if (null == simcResidentSocialInsurance) {
                simcResidentSocialInsurance = new SimcResidentSocialInsurance();
                simcResidentSocialInsurance.setResidentIdCardNo(residentIdCardNo);
                simcResidentSocialInsurance.setResidentName(importRowData.getResidentName());
                simcResidentSocialInsurance.setResidentSex(SimcUtil.convertSex(importRowData.getResidentSex()));
                simcResidentSocialInsurance.setResidentBirthDate(SimcUtil.getBirthDateFromIdCardNo(residentIdCardNo));
                simcResidentSocialInsurance.setResidentPhone(importRowData.getResidentPhone());
                simcResidentSocialInsurance.setResidentTownshipDistrictId(getDistrictId(townshipDistrictMap, importRowData.getResidentTownshipDistrictName()));
                simcResidentSocialInsurance.setResidentVillageDistrictId(getDistrictId(villageDistrictMap, importRowData.getResidentTownshipDistrictName() + "_" + importRowData.getResidentVillageDistrictName()));
                simcResidentSocialInsurance.setResidentGroupDistrictId(getDistrictId(groupDistrictMap, importRowData.getResidentTownshipDistrictName() + "_" + importRowData.getResidentVillageDistrictName() + "_" + importRowData.getResidentGroupDistrictName()));
                simcResidentSocialInsurance.setFllId((familyLandLosing.get(importRowData.getHeadResidentIdCardNo() + "_" + importRowData.getFllProjectName())));
                simcResidentSocialInsurance.setFllProjectName(importRowData.getFllProjectName());
                simcResidentSocialInsurance.setFllTime(importRowData.getFllTime());
                simcResidentSocialInsurance.setFllProjectIsCityLevel(SimcUtil.convertProjectIsCityLevel(importRowData.getFllProjectIsCityLevel()));
                simcResidentSocialInsurance.setSocialInsuranceType(SimcUtil.convertSocialInsuranceType(importRowData.getSocialInsuranceType()));
                simcResidentSocialInsurance.setSocialInsuranceState("1");
                simcResidentSocialInsurance.setSocialInsuranceJointApprovalTime(DateUtils.parseDate(importRowData.getSocialInsuranceJointApprovalTime(), DateUtils.YYYYMMDD));
                simcResidentSocialInsurance.setCreateUserId(userId);
                simcResidentSocialInsurance.setCreateTime(currentDate);
                this.simcResidentSocialInsuranceMapper.insert(simcResidentSocialInsurance);
            } else {// 存在, 进行提示
                // ......
                simcResidentSocialInsuranceSB.append("姓名：").append(importRowData.getResidentName()).append("，身份证号码：").append(residentIdCardNo).append("</br>；");

            }

            SimcResidentOldLandLosing simcResidentOldLandLosing = this.simcResidentOldLandLosingMapper.selectByPrimaryKey(residentIdCardNo);
            if (simcResidentOldLandLosing != null) {
                simcResidentOldLandLosingSB.append("姓名：").append(importRowData.getResidentName()).append("，身份证号码：").append(residentIdCardNo).append("</br>；");
            }
        }

        StringBuilder sb = new StringBuilder("温馨提示</br>导入成功");
        if (simcResidentSocialInsuranceSB.length() > 0) {
            sb.append("<br/>如下名单已经存在于社保档案中：<br/>").append(simcResidentSocialInsuranceSB.substring(0, simcResidentSocialInsuranceSB.length() - 1));
        }

        if (simcResidentOldLandLosingSB.length() > 0) {
            sb.append("<br/>如下名单存在于老失地档案中：<br/>").append(simcResidentOldLandLosingSB.substring(0, simcResidentOldLandLosingSB.length() - 1));
        }
        return sb.toString();
    }

    private Long getDistrictId(Map<String, Map<String, Object>> districtMap, String key) {
        Map<String, Object> district = districtMap.get(key);
        return (Long) district.get("DistrictId");
    }

    /**
     * 梳理导入数据
     *
     * @param importRowDataList
     */
    private void combImportData(List<SimcResidentSocialInsuranceImportRowData> importRowDataList,
                                Map<Integer, List<String>> rowFailInfos,
                                Map<String, Map<String, Object>> laProjectImportData,
                                Map<String, Map<String, Object>> townshipDistrictMap,
                                Map<String, Map<String, Object>> villageDistrictMap,
                                Map<String, Map<String, Object>> groupDistrictMap,
                                Map<String, List<SimcResidentSocialInsuranceImportRowData>> familyImportData) {
        Set<String> residentIdCardNoSet = new HashSet<>();
        for (SimcResidentSocialInsuranceImportRowData importRowData : importRowDataList) {
            Integer rowIndex = importRowData.getRowIndex();
            String residentIdCardNo = importRowData.getResidentIdCardNo();
            if (StringUtils.isBlank(importRowData.getResidentName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "姓名为空");
            }
            if (StringUtils.isBlank(residentIdCardNo)) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码为空");
            } else {
                if (residentIdCardNo.length() != 18) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码不是18位身份证");
                } else {
                    String sex = residentIdCardNo.substring(16, 17);
                    if (!("1".equals(sex) || "2".equals(sex))) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码第17位填写错误");
                    } else {
                        importRowData.setResidentSex("1".equals(sex) ? "男" : "女");
                    }
                }
                if (residentIdCardNoSet.contains(residentIdCardNo)) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "存在多条身份证号码为" + residentIdCardNo + "的数据");
                } else {
                    residentIdCardNoSet.add(residentIdCardNo);
                }
            }
            if (StringUtils.isBlank(importRowData.getHeadResidentIdCardNo())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "户主身份证号码不能为空");
            } else {
                if (importRowData.getHeadResidentIdCardNo().length() != 18) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "户主身份证号码不是18位身份证");
                }
            }
            if (StringUtils.isBlank(importRowData.getResidentTownshipDistrictName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "乡镇为空");
            }
            if (StringUtils.isBlank(importRowData.getResidentVillageDistrictName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "村（社区）为空");
            }
            if (StringUtils.isBlank(importRowData.getResidentGroupDistrictName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "组为空");
            }
            if (null == importRowData.getFllFamilyMemberNumber() || importRowData.getFllFamilyMemberNumber() <= 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "被征地时家庭人数不能为空且必须大于0");
            }
            if (null == importRowData.getFllEligibleMemberNumber() || importRowData.getFllEligibleMemberNumber() <= 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "符合纳入参保条件人数不能为空且必须大于0");
            }
            if (null == importRowData.getOriginalAgriculturalAcreage() || importRowData.getOriginalAgriculturalAcreage() <= 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "原耕地面积（亩）不能为空且必须大于0");
            }
            if (null == importRowData.getFllTotalAgriculturalAcreage() || importRowData.getFllTotalAgriculturalAcreage() <= 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "共被征耕地（亩）不能为空且必须大于0");
            }
            if (null == importRowData.getCurrentAgriculturalAcreage() || importRowData.getFllTotalAgriculturalAcreage() < 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "现在承包耕地（亩）不能为空");
            }
            if (null == importRowData.getPerAgriculturalAcreage() || importRowData.getPerAgriculturalAcreage() < 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "现人均耕地面积不能为空");
            }
            if (StringUtils.isBlank(importRowData.getFllProjectName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "征地项目名称不能为空");
            }
            if (StringUtils.isBlank(importRowData.getFllProjectIsCityLevel())) {
                importRowData.setFllProjectIsCityLevel("否");
            }
            if (!("是".equals(importRowData.getFllProjectIsCityLevel()) || "否".equals(importRowData.getFllProjectIsCityLevel()))) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "是否市级征地项目取值范围：是、否");
            }

            if (StringUtils.isBlank(importRowData.getFllTime())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "征地时间不能为空");
            } else {
                if (importRowData.getFllTime().length() != 6) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "征地时间格式应该为yyyyMM");
                } else {
                    try {
                        DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getFllTime() + "01");
                    } catch (Exception e) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "参保时间格式应该为yyyyMM");
                    }
                }
            }

            if (StringUtils.isBlank(importRowData.getSocialInsuranceJointApprovalTime())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "联审联批通过时间不能为空");
            } else {
                if (importRowData.getSocialInsuranceJointApprovalTime().length() != 8) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "征地时间格式应该为yyyyMMdd");
                } else {
                    try {
                        DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getSocialInsuranceJointApprovalTime());
                    } catch (Exception e) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "参保时间格式应该为yyyyMMdd");
                    }
                }
            }

            if (StringUtils.isBlank(importRowData.getSocialInsuranceType())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "参保类别不能为空");
            } else if (!("城镇职工".equals(importRowData.getSocialInsuranceType()) || "城乡居民".equals(importRowData.getSocialInsuranceType()))) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "参保类别设值错误，取值范围：城镇职工、城乡居民");
            }

            // @part 1: 梳理项目数据
            Map<String, Object> laProjectData = laProjectImportData.get(importRowData.getFllProjectName());
            if (null == laProjectData) {
                laProjectData = new HashMap<String, Object>();
                laProjectData.put("ProjectName", importRowData.getFllProjectName());
                laProjectData.put("IsCityLevel", SimcUtil.convertProjectIsCityLevel(importRowData.getFllProjectIsCityLevel()));
                laProjectImportData.put(importRowData.getFllProjectName(), laProjectData);
            }
            // @part 2: 梳理行政区域数据
            combDistrict(importRowData.getResidentTownshipDistrictName(),
                    importRowData.getResidentVillageDistrictName(),
                    importRowData.getResidentGroupDistrictName(),
                    townshipDistrictMap,
                    villageDistrictMap,
                    groupDistrictMap);
            // @part 3: 梳理家庭数据
            List<SimcResidentSocialInsuranceImportRowData> familySimcResidentSocialInsuranceImportRowDataList = familyImportData.get(importRowData.getHeadResidentIdCardNo());
            if (null == familySimcResidentSocialInsuranceImportRowDataList) {
                familySimcResidentSocialInsuranceImportRowDataList = new ArrayList<>();
                familyImportData.put(importRowData.getHeadResidentIdCardNo(), familySimcResidentSocialInsuranceImportRowDataList);
            }
            familySimcResidentSocialInsuranceImportRowDataList.add(importRowData);
        }
    }

    /**
     * 保存家庭被征收地信息
     *
     * @param familyImportData
     * @param townshipDistrictMap
     * @param villageDistrictMap
     * @param groupDistrictMap
     * @param laProjectImportData
     * @param createUserId
     * @param createTime
     * @return
     * @throws Exception
     */
    private Map<String, Integer> saveSimcFamilyLandLosing(Map<String, List<SimcResidentSocialInsuranceImportRowData>> familyImportData,
                                                          Map<String, Map<String, Object>> townshipDistrictMap,
                                                          Map<String, Map<String, Object>> villageDistrictMap,
                                                          Map<String, Map<String, Object>> groupDistrictMap,
                                                          Map<String, Map<String, Object>> laProjectImportData,
                                                          Long createUserId,
                                                          Date createTime) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        Iterator<String> iterator = familyImportData.keySet().iterator();
        while (iterator.hasNext()) {
            String familyNO = iterator.next();
            SimcResidentSocialInsuranceImportRowData familyHeadImportRowData = null;
            Map<String, SimcResidentSocialInsuranceImportRowData> projectLandAcquisition = new HashMap<>();// 家庭项目征地

            List<SimcResidentSocialInsuranceImportRowData> familySimcResidentSocialInsuranceImportRowDataList = familyImportData.get(familyNO);
            Map<String, String> memberInfoMap = new HashMap<>();
            for (SimcResidentSocialInsuranceImportRowData familySimcResidentSocialInsuranceImportRowData : familySimcResidentSocialInsuranceImportRowDataList) {
                memberInfoMap.put(familySimcResidentSocialInsuranceImportRowData.getResidentIdCardNo(), familySimcResidentSocialInsuranceImportRowData.getResidentName());

                if (familySimcResidentSocialInsuranceImportRowData.getResidentIdCardNo().equals(familyNO)) {
                    familyHeadImportRowData = familySimcResidentSocialInsuranceImportRowData;
                }

                String fllProjectName = familySimcResidentSocialInsuranceImportRowData.getFllProjectName();
                SimcResidentSocialInsuranceImportRowData projectLandAcquisitionRowData = projectLandAcquisition.get(fllProjectName);
                if (null == projectLandAcquisitionRowData) {
                    projectLandAcquisition.put(fllProjectName, familySimcResidentSocialInsuranceImportRowData);
                } else {// 如果存在多条，则校验征地项目数据是否一致
                    if (!projectLandAcquisitionRowData.getFllFamilyMemberNumber().equals(familySimcResidentSocialInsuranceImportRowData.getFllFamilyMemberNumber())) {// 被征地时家庭人数
                        throw new Exception("数据列<户主身份证号码>列值<" + familyNO + ">涉及的数据列<征地项目名称>列值<" + fllProjectName + ">存在<被征地时家庭人数>不一致问题");
                    }

                    if (!projectLandAcquisitionRowData.getFllEligibleMemberNumber().equals(familySimcResidentSocialInsuranceImportRowData.getFllEligibleMemberNumber())) {// 符合纳入参保条件人数
                        throw new Exception("数据列<户主身份证号码>列值<" + familyNO + ">涉及的数据列<征地项目名称>列值<" + fllProjectName + ">存在<符合纳入参保条件人数>不一致问题");
                    }

                    if (!projectLandAcquisitionRowData.getOriginalAgriculturalAcreage().equals(familySimcResidentSocialInsuranceImportRowData.getOriginalAgriculturalAcreage())) {// 原耕地面积（亩）
                        throw new Exception("数据列<户主身份证号码>列值<" + familyNO + ">涉及的数据列<征地项目名称>列值<" + fllProjectName + ">存在<原耕地面积（亩）>不一致问题");
                    }

                    if (!projectLandAcquisitionRowData.getFllTotalAgriculturalAcreage().equals(familySimcResidentSocialInsuranceImportRowData.getFllTotalAgriculturalAcreage())) {// 共被征耕地（亩）
                        throw new Exception("数据列<户主身份证号码>列值<" + familyNO + ">涉及的数据列<征地项目名称>列值<" + fllProjectName + ">存在<共被征耕地（亩）>不一致问题");
                    }

                    if (!projectLandAcquisitionRowData.getCurrentAgriculturalAcreage().equals(familySimcResidentSocialInsuranceImportRowData.getCurrentAgriculturalAcreage())) {// 现在承包耕地（亩）
                        throw new Exception("数据列<户主身份证号码>列值<" + familyNO + ">涉及的数据列<征地项目名称>列值<" + fllProjectName + ">存在<现在承包耕地（亩）>不一致问题");
                    }

                    if (!projectLandAcquisitionRowData.getPerAgriculturalAcreage().equals(familySimcResidentSocialInsuranceImportRowData.getPerAgriculturalAcreage())) {// 现人均耕地面积
                        throw new Exception("数据列<户主身份证号码>列值<" + familyNO + ">涉及的数据列<征地项目名称>列值<" + fllProjectName + ">存在<现人均耕地面积>不一致问题");
                    }

                    if (!projectLandAcquisitionRowData.getFllTime().equals(familySimcResidentSocialInsuranceImportRowData.getFllTime())) {// 征地时间
                        throw new Exception("数据列<户主身份证号码>列值<" + familyNO + ">涉及的数据列<征地项目名称>列值<" + fllProjectName + ">存在<征地时间>不一致问题");
                    }
                }
            }

            // 保存家庭信息
            SimcFamily simcFamily = this.simcFamilyMapper.selectByPrimaryKey(familyNO);
            Map<String, String> familyHead = new HashMap<>();
            if (null != simcFamily) {
                if (null != familyHeadImportRowData) {// 更新
                    simcFamily.setFamilyName("<" + familyHeadImportRowData.getResidentName() + "> 家");
                    simcFamily.setFamilyHead(familyNO);
                    simcFamily.setFamilyHeadResidentName(familyHeadImportRowData.getResidentName());
                    simcFamily.setTownshipDistrictId(getDistrictId(townshipDistrictMap, familyHeadImportRowData.getResidentTownshipDistrictName()));
                    simcFamily.setVillageDistrictId(getDistrictId(villageDistrictMap, familyHeadImportRowData.getResidentTownshipDistrictName() + "_" + familyHeadImportRowData.getResidentVillageDistrictName()));
                    simcFamily.setGroupDistrictId(getDistrictId(groupDistrictMap, familyHeadImportRowData.getResidentTownshipDistrictName() + "_" + familyHeadImportRowData.getResidentVillageDistrictName() + "_" + familyHeadImportRowData.getResidentGroupDistrictName()));
                    simcFamily.setModifyUserId(createUserId);
                    simcFamily.setModifyTime(createTime);
                    this.simcFamilyMapper.updateByPrimaryKey(simcFamily);
                } else {// 家庭存在，但是导入的数据中有不存在户主的信息，则不操作

                }

                familyHead.put(familyNO, simcFamily.getFamilyHeadResidentName());
            } else {// 新增家庭
                if (null == familyHeadImportRowData) {// 家庭不存在，但是导入的数据中有不存在户主的信息，则取第一行数据
                    familyHeadImportRowData = familySimcResidentSocialInsuranceImportRowDataList.get(0);
                }
                simcFamily = new SimcFamily();
                simcFamily.setFamilyNo(familyNO);
                simcFamily.setFamilyName("<" + familyHeadImportRowData.getResidentName() + "> 家");
                simcFamily.setFamilyHeadResidentName(familyHeadImportRowData.getResidentName());
                simcFamily.setFamilyHead(familyNO);
                simcFamily.setTownshipDistrictId(getDistrictId(townshipDistrictMap, familyHeadImportRowData.getResidentTownshipDistrictName()));
                simcFamily.setVillageDistrictId(getDistrictId(villageDistrictMap, familyHeadImportRowData.getResidentTownshipDistrictName() + "_" + familyHeadImportRowData.getResidentVillageDistrictName()));
                simcFamily.setGroupDistrictId(getDistrictId(groupDistrictMap, familyHeadImportRowData.getResidentTownshipDistrictName() + "_" + familyHeadImportRowData.getResidentVillageDistrictName() + "_" + familyHeadImportRowData.getResidentGroupDistrictName()));
                simcFamily.setCreateUserId(createUserId);
                simcFamily.setCreateTime(createTime);
                this.simcFamilyMapper.insert(simcFamily);
                familyHead.put(familyNO, familyHeadImportRowData.getResidentName());
            }

            // 保存家庭成员信息
            List<SimcFamilyMember> allSimcFamilyMemberList = this.simcFamilyMemberMapper.selectByFamilyNo(familyNO);
            Iterator<String> memberInfoIte = memberInfoMap.keySet().iterator();
            while (memberInfoIte.hasNext()) {
                String memberIdCardNo = memberInfoIte.next();
                String memberName = memberInfoMap.get(memberIdCardNo);
                SimcFamilyMember simcFamilyMember = null;
                for (int i = 0; null != allSimcFamilyMemberList && i < allSimcFamilyMemberList.size(); i++) {
                    if (memberIdCardNo.equals(allSimcFamilyMemberList.get(i).getMemberCardNo())) {
                        simcFamilyMember = allSimcFamilyMemberList.get(i);
                        break;
                    }
                }
                if (null == simcFamilyMember) {
                    simcFamilyMember = new SimcFamilyMember();
                    simcFamilyMember.setFamilyNo(familyNO);
                    simcFamilyMember.setMemberCardNo(memberIdCardNo);
                    simcFamilyMember.setMemberName(memberName);
                    simcFamilyMember.setCreateUserId(createUserId);
                    simcFamilyMember.setCreateTime(createTime);
                    this.simcFamilyMemberMapper.insert(simcFamilyMember);
                }
            }

            // 保存家庭被征收地信息
            Iterator<String> projectLandAcquisitionIte = projectLandAcquisition.keySet().iterator();
            while (projectLandAcquisitionIte.hasNext()) {
                String projectName = projectLandAcquisitionIte.next();
                SimcResidentSocialInsuranceImportRowData simcResidentSocialInsuranceImportRowData = projectLandAcquisition.get(projectName);
                Map<String, Object> laProjectData = laProjectImportData.get(projectName);
                Integer projectId = (Integer) laProjectData.get("ProjectId");
                Integer isCityLevel = (Integer) laProjectData.get("IsCityLevel");

                List<SimcFamilyLandLosing> familyLandLosingList = this.simcFamilyLandLosingMapper.selectByFamilyNo(familyNO, projectId);
                SimcFamilyLandLosing simcFamilyLandLosing = null;
                if (CollectionUtils.isEmpty(familyLandLosingList)) {// 新增
                    simcFamilyLandLosing = new SimcFamilyLandLosing();
                    simcFamilyLandLosing.setFamilyNo(familyNO);
                    simcFamilyLandLosing.setHeadResidentName(familyHead.get(familyNO));
                    simcFamilyLandLosing.setFllFamilyMemberNumber(simcResidentSocialInsuranceImportRowData.getFllFamilyMemberNumber());
                    simcFamilyLandLosing.setFllEligibleMemberNumber(simcResidentSocialInsuranceImportRowData.getFllEligibleMemberNumber());
                    simcFamilyLandLosing.setOriginalAgriculturalAcreage(simcResidentSocialInsuranceImportRowData.getOriginalAgriculturalAcreage());
                    simcFamilyLandLosing.setFllTotalAgriculturalAcreage(simcResidentSocialInsuranceImportRowData.getFllTotalAgriculturalAcreage());
                    simcFamilyLandLosing.setCurrentAgriculturalAcreage(simcResidentSocialInsuranceImportRowData.getCurrentAgriculturalAcreage());
                    simcFamilyLandLosing.setPerAgriculturalAcreage(simcResidentSocialInsuranceImportRowData.getPerAgriculturalAcreage());
                    simcFamilyLandLosing.setProjectId(projectId);
                    simcFamilyLandLosing.setProjectName(projectName);
                    simcFamilyLandLosing.setProjectIsCityLevel(isCityLevel);
                    simcFamilyLandLosing.setFllTime(simcResidentSocialInsuranceImportRowData.getFllTime());
                    simcFamilyLandLosing.setCreateUserId(createUserId);
                    simcFamilyLandLosing.setCreateTime(createTime);
                    this.simcFamilyLandLosingMapper.insert(simcFamilyLandLosing);
                } else {// 已经存在了就不操作了
                    simcFamilyLandLosing = familyLandLosingList.get(0);
                }
                result.put(familyNO + "_" + projectName, simcFamilyLandLosing.getFllId());
            }
        }
        return result;
    }

    /**
     * 初始化并保存行政区域信息
     *
     * @param districtMap
     * @param parentDistrictMap
     * @param districtColName
     * @param parentDistrictColName
     * @param createWhenNonExist
     * @throws Exception
     */
    private void initAndSaveDistrict(Map<String, Map<String, Object>> districtMap,
                                     Map<String, Map<String, Object>> parentDistrictMap,
                                     String districtColName,
                                     String parentDistrictColName,
                                     boolean createWhenNonExist) throws Exception {
        Set<String> keySet = districtMap.keySet();
        Iterator<String> keyIte = keySet.iterator();
        Map<Long, List<Map<String, Object>>> map = new HashMap<>();
        while (keyIte.hasNext()) {
            String key = keyIte.next();
            Map<String, Object> district = districtMap.get(key);
            String districtName = (String) district.get("DistrictName");
            String parentDistrictName = (String) district.get("ParentDistrictName");
            String parentKey = key;
            if (key.indexOf("_") > 0) {
                parentKey = parentKey.substring(0, parentKey.lastIndexOf("_"));
            } else {
                parentKey = parentDistrictName;
            }

            Map<String, Object> parentDistrict = parentDistrictMap.get(parentKey);// 上级行政区域数据
            if (null == parentDistrict) {
                throw new Exception("数据列<" + districtColName + ">列值<" + districtName + ">对应的上级行政区域不存在，请检查改行数据列<" + parentDistrictColName + ">列值<" + parentDistrictName + ">是否存在");
            }
            Long parentDistrictId = (Long) parentDistrict.get("DistrictId");
            district.put("ParentDistrictId", parentDistrictId);// 设置district上级行政区域编号

            List<Map<String, Object>> subordinateDistrictList = map.get(parentDistrictId);
            if (null == subordinateDistrictList) {
                subordinateDistrictList = new ArrayList<>();
                map.put(parentDistrictId, subordinateDistrictList);
            }
            subordinateDistrictList.add(district);
        }

        Iterator<Long> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            Long parentDistrictId = ite.next();
            List<Map<String, Object>> subordinateDistrictList = map.get(parentDistrictId);
            List<String> subordinateDistrictNameList = new ArrayList<>();
            for (Map<String, Object> subordinateDistrict : subordinateDistrictList) {
                subordinateDistrictNameList.add((String) subordinateDistrict.get("DistrictName"));
            }
            List<SimcDistrict> allSubordinateDistrictList = null;
            if (createWhenNonExist) {
                allSubordinateDistrictList = this.simcDistrictService.createSubordinateDistrictId(parentDistrictId, new HashSet<>(subordinateDistrictNameList));
            } else {
                allSubordinateDistrictList = this.simcDistrictService.queryByParentDistrictIdAndDistrictNames(parentDistrictId, subordinateDistrictNameList);
            }
            for (Map<String, Object> subordinateDistrict : subordinateDistrictList) {
                String districtName = (String) subordinateDistrict.get("DistrictName");
                String parentDistrictName = (String) subordinateDistrict.get("ParentDistrictName");

                SimcDistrict simcDistrict = null;
                for (int i = 0; null != allSubordinateDistrictList && i < allSubordinateDistrictList.size(); i++) {
                    if (districtName.equals(allSubordinateDistrictList.get(i).getDistrictName())) {
                        simcDistrict = allSubordinateDistrictList.get(i);
                        break;
                    }
                }
                if (null == simcDistrict) {
                    throw new Exception("数据列<" + parentDistrictColName + ">列值<" + parentDistrictName + ">对应的下级行政区域不存在，请检查改行数据列<" + districtColName + ">列值<" + districtName + ">是否存在");
                }
                subordinateDistrict.put("DistrictId", simcDistrict.getDistrictId());
            }
        }
    }

    /**
     * 梳理行政区域
     *
     * @param townshipDistrictName
     * @param villageDistrictName
     * @param groupDistrictName
     * @param townshipDistrictMap
     * @param villageDistrictMap
     * @param groupDistrictMap
     */
    private void combDistrict(String townshipDistrictName,
                              String villageDistrictName,
                              String groupDistrictName,
                              Map<String, Map<String, Object>> townshipDistrictMap,
                              Map<String, Map<String, Object>> villageDistrictMap,
                              Map<String, Map<String, Object>> groupDistrictMap) {
        combDistrict(townshipDistrictMap, townshipDistrictName, townshipDistrictName, ISimcDistrictService.CHAI_SANG_TOWNSHIP_DISTRICT_NAME);
        combDistrict(villageDistrictMap, townshipDistrictName + "_" + villageDistrictName, villageDistrictName, townshipDistrictName);
        combDistrict(groupDistrictMap, townshipDistrictName + "_" + villageDistrictName + "_" + groupDistrictName, groupDistrictName, villageDistrictName);
    }

    /**
     * 梳理行政区域
     *
     * @param districtMap
     * @param districtName
     * @param parentDistrictName
     */
    private void combDistrict(Map<String, Map<String, Object>> districtMap, String key, String districtName, String parentDistrictName) {
        Map<String, Object> district = districtMap.get(key);
        if (null == district) {
            district = new HashMap<>();
            district.put("DistrictName", districtName);
            district.put("ParentDistrictName", parentDistrictName);
            districtMap.put(key, district);
        }
    }

    @Override
    public String importResidentSocialInsuranceSubsidyDataList(List<SimcResidentSocialInsuranceSubsidyImportRowData> importRowDataList, Long userId) throws Exception {
        Date currentDate = DateUtils.getNowDate();
        if (CollectionUtils.isEmpty(importRowDataList)) {
            throw new Exception("导入的数据为空");
        }
        Map<String, List<SimcResidentSocialInsuranceSubsidyDetail>> residentSubsidyDetail = new LinkedHashMap<>();
        Map<String, SimcResidentSocialInsurance> residentSimcResidentSocialInsurance = new HashMap<>();
        Map<Integer, List<String>> rowFailInfos = new LinkedHashMap<>();
        for (SimcResidentSocialInsuranceSubsidyImportRowData importRowData : importRowDataList) {
            Integer rowIndex = importRowData.getRowIndex();
            String residentIdCardNo = importRowData.getResidentIdCardNo();
            String strSubsidyTime = importRowData.getSubsidyTime();
            Double subsidyMoney = importRowData.getSubsidyMoney();
            if (StringUtils.isBlank(residentIdCardNo)) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码为空");
            } else {
                if (residentIdCardNo.length() != 18) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码不是18位身份证");
                } else {
                    if (!residentSimcResidentSocialInsurance.containsKey(residentIdCardNo)) {
                        SimcResidentSocialInsurance simcResidentSocialInsurance = this.simcResidentSocialInsuranceMapper.selectByPrimaryKey(residentIdCardNo);
                        if (null == simcResidentSocialInsurance) {
                            addRowIndexFailInfo(rowFailInfos, rowIndex, "居民[" + residentIdCardNo + "]不在社保档案中");
                        }
                        residentSimcResidentSocialInsurance.put(residentIdCardNo, simcResidentSocialInsurance);
                    }
                }
            }
            if (null == subsidyMoney || subsidyMoney <= 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴金额不能为空或者负数");
            }
            if (StringUtils.isBlank(strSubsidyTime)) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份为空");
            } else if (strSubsidyTime.contains("-")) {
                String[] subsidyTimes = strSubsidyTime.split("-");
                if (subsidyTimes.length != 2) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份使用了区间范围类型，但格式非法");
                } else {
                    subsidyTimes[0] = subsidyTimes[0].trim();
                    subsidyTimes[1] = subsidyTimes[1].trim();
                    boolean isValid = true;
                    if (subsidyTimes[0].length() != 6) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份使用了区间范围类型，开始月份应该采用yyyyMM格式");
                        isValid = false;
                    }
                    if (subsidyTimes[1].length() != 6) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份使用了区间范围类型，结束月份应采用yyyyMM格式");
                        isValid = false;
                    }
                    if (subsidyTimes[0].equals(subsidyTimes[1])) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份使用了区间范围类型，开始月份等于结束月份");
                        isValid = false;
                    }
                    try {
                        if (Integer.parseInt(subsidyTimes[0]) > Integer.parseInt(subsidyTimes[1])) {
                            addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份使用了区间范围类型，但开始结束月份大于结束月份");
                            isValid = false;
                        }
                    } catch (Exception e) {
                        isValid = false;
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份使用了区间范围类型，开始月份或结束月份值类型非法");
                    }
                    Date subsidyTime = null;
                    try {
                        subsidyTime = DateUtils.dateTime(DateUtils.YYYYMMDD, subsidyTimes[0] + "01");
                    } catch (Exception e) {
                        isValid = false;
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份使用了区间范围类型，开始月份格式非法");
                    }
                    String end = subsidyTimes[1] + "01";
                    while (isValid && true) {
                        if (!(addResidentSubsidyDetail(residentSubsidyDetail, residentIdCardNo, subsidyTime, subsidyMoney))) {
                            addRowIndexFailInfo(rowFailInfos, rowIndex, "表格中存在多条月份=[" + (DateUtils.parseDateToStr(DateUtils.YYYYMMDD, subsidyTime).substring(0, 6)) + "]的补贴数据");
                        } else if (isSubsidy(residentIdCardNo, subsidyTime)) {
                            addRowIndexFailInfo(rowFailInfos, rowIndex, "居民[" + residentIdCardNo + "]" + (DateUtils.parseDateToStr(DateUtils.YYYYMMDD, subsidyTime).substring(0, 6)) + "已补贴");
                        }
                        if (end.equals(DateUtils.parseDateToStr(DateUtils.YYYYMMDD, subsidyTime))) {
                            break;
                        } else {
                            subsidyTime = DateUtils.addMonths(subsidyTime, 1);
                        }
                    }
                }
            } else {
                strSubsidyTime = strSubsidyTime.trim();
                if (strSubsidyTime.length() != 6) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "补贴月份格式非法，应该采用yyyyMM格式");
                } else {
                    Date subsidyTime = DateUtils.dateTime(DateUtils.YYYYMMDD, strSubsidyTime + "01");
                    if (!(addResidentSubsidyDetail(residentSubsidyDetail, residentIdCardNo, subsidyTime, subsidyMoney))) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "表格中存在多条月份=[" + strSubsidyTime + "]的补贴数据");
                    } else if (isSubsidy(residentIdCardNo, subsidyTime)) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "居民[" + residentIdCardNo + "] " + strSubsidyTime + "已补贴");
                    }
                }
            }
        }
        if (MapUtils.isNotEmpty(rowFailInfos)) {
            return wrapRowFailInfos(rowFailInfos, 1);
        }

        Iterator<String> ite = residentSubsidyDetail.keySet().iterator();
        while (ite.hasNext()) {
            String residentIdCardNo = ite.next();
            List<SimcResidentSocialInsuranceSubsidyDetail> list = residentSubsidyDetail.get(residentIdCardNo);
            SimcResidentSocialInsurance simcResidentSocialInsurance = residentSimcResidentSocialInsurance.get(residentIdCardNo);
            for (int i = 0; i < list.size(); i++) {
                SimcResidentSocialInsuranceSubsidy subsidy = new SimcResidentSocialInsuranceSubsidy();
                subsidy.setResidentIdCardNo(residentIdCardNo);
                subsidy.setResidentName(simcResidentSocialInsurance.getResidentName());
                subsidy.setResidentSex(simcResidentSocialInsurance.getResidentSex());
                subsidy.setResidentBirthDate(simcResidentSocialInsurance.getResidentBirthDate());
                subsidy.setResidentPhone(simcResidentSocialInsurance.getResidentPhone());
                subsidy.setResidentTownshipDistrictId(simcResidentSocialInsurance.getResidentTownshipDistrictId());
                subsidy.setResidentVillageDistrictId(simcResidentSocialInsurance.getResidentVillageDistrictId());
                subsidy.setResidentGroupDistrictId(simcResidentSocialInsurance.getResidentGroupDistrictId());
                subsidy.setSocialInsuranceType(simcResidentSocialInsurance.getSocialInsuranceType());
                subsidy.setSubsidyMoney(list.get(i).getSubsidyMoney());
                subsidy.setSubsidyTime(list.get(i).getSubsidyTime());
                subsidy.setCreateUserId(userId);
                subsidy.setCreateTime(currentDate);
                this.simcResidentSocialInsuranceSubsidyMapper.insert(subsidy);
            }
        }
        return "温馨提示</br>导入成功";
    }

    private boolean isSubsidy(String residentIdCardNo, Date subsidyTime) {
        SimcResidentSocialInsuranceSubsidy subsidy = new SimcResidentSocialInsuranceSubsidy();
        subsidy.setResidentIdCardNo(residentIdCardNo);
        subsidy.getParams().put("beginSubsidyTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, subsidyTime));
        subsidy.getParams().put("endSubsidyTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(subsidyTime, 20)));
        List<SimcResidentSocialInsuranceSubsidy> list = this.simcResidentSocialInsuranceSubsidyMapper.selectList(subsidy);
        if (CollectionUtils.isNotEmpty(list)) {
            return true;
        } else {
            return false;
        }
    }

    private String wrapRowFailInfos(Map<Integer, List<String>> rowFailInfos, int startIndex) {
        if (MapUtils.isEmpty(rowFailInfos)) {
            return null;
        }
        Iterator<Integer> ite = rowFailInfos.keySet().iterator();
        StringBuilder sb = new StringBuilder("温馨提示<br/>");
        while (ite.hasNext()) {
            Integer rowIndex = ite.next();
            List<String> failInfos = rowFailInfos.get(rowIndex);
            if (CollectionUtils.isEmpty(failInfos)) {
                continue;
            }
            sb.append("第").append(rowIndex + startIndex).append("行数据校验失败，原因：");
            for (int i = 0; i < failInfos.size(); i++) {
                String failInfo = failInfos.get(i);
                sb.append(failInfo);
                if (i != failInfos.size() - 1) {
                    sb.append("; ");
                }
            }
            sb.append("<br/>");
        }
        return sb.toString();
    }

    private boolean addResidentSubsidyDetail(Map<String, List<SimcResidentSocialInsuranceSubsidyDetail>> residentSubsidyDetail, String residentIdCardNo, Date subsidyTime, Double subsidyMoney) {
        List<SimcResidentSocialInsuranceSubsidyDetail> detailList = residentSubsidyDetail.get(residentIdCardNo);
        if (null == detailList) {
            detailList = new ArrayList<>();
            residentSubsidyDetail.put(residentIdCardNo, detailList);
        } else {
            boolean isAdd = false;
            for (SimcResidentSocialInsuranceSubsidyDetail detail : detailList) {
                if (detail.getSubsidyTime().compareTo(subsidyTime) == 0) {
                    isAdd = true;
                    break;
                }
            }
            if (isAdd) {// 如果已经添加过，就不再添加直接返回
                return false;
            }
        }
        detailList.add(new SimcResidentSocialInsuranceSubsidyDetail(subsidyTime, subsidyMoney));
        return true;
    }

    private void addRowIndexFailInfo(Map<Integer, List<String>> rowFailInfos, Integer rowIndex, String failInfo) {
        List<String> failInfos = rowFailInfos.get(rowIndex);
        if (null == failInfos) {
            failInfos = new ArrayList<>();
            rowFailInfos.put(rowIndex, failInfos);
        }
        failInfos.add(failInfo);
    }

    /**
     * 导入居民老失地数据列表
     *
     * @param importRowDataList 数据列表
     * @param userId            操作员ID
     * @return 结果
     */
    public String importSimcResidentOldLandLosingDataDataList(List<SimcResidentOldLandLosingImportRowData> importRowDataList, Long userId) throws Exception {
        Date currentDate = DateUtils.getNowDate();
        if (CollectionUtils.isEmpty(importRowDataList)) {
            throw new Exception("导入的数据为空");
        }
        Map<String, Map<String, Object>> townshipDistrictMap = new HashMap<>();
        Map<String, Map<String, Object>> villageDistrictMap = new HashMap<>();
        Map<String, Map<String, Object>> groupDistrictMap = new HashMap<>();
        Map<Integer, List<String>> rowFailInfos = new LinkedHashMap<>();
        Set<String> residentIdCardNoSet = new HashSet<>();
        for (SimcResidentOldLandLosingImportRowData importRowData : importRowDataList) {
            Integer rowIndex = importRowData.getRowIndex();
            String residentIdCardNo = importRowData.getResidentIdCardNo();
            if (StringUtils.isBlank(importRowData.getResidentName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "姓名为空");
            }
            if (StringUtils.isBlank(residentIdCardNo)) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码为空");
            } else {
                if (residentIdCardNo.length() != 18) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码不是18位身份证");
                } else {
                    String sex = residentIdCardNo.substring(16, 17);
                    if (!("1".equals(sex) || "2".equals(sex))) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "身份证号码第17位填写错误");
                    } else {
                        importRowData.setResidentSex("1".equals(sex) ? "男" : "女");
                    }
                }
                if (residentIdCardNoSet.contains(residentIdCardNo)) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "存在多条身份证号码为" + residentIdCardNo + "的数据");
                } else {
                    residentIdCardNoSet.add(residentIdCardNo);
                }
            }
            if (StringUtils.isBlank(importRowData.getResidentTownshipDistrictName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "乡镇为空");
            }
            if (StringUtils.isBlank(importRowData.getResidentVillageDistrictName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "村（社区）为空");
            }
            if (StringUtils.isBlank(importRowData.getResidentGroupDistrictName())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "组为空");
            }
            combDistrict(importRowData.getResidentTownshipDistrictName(),
                    importRowData.getResidentVillageDistrictName(),
                    importRowData.getResidentGroupDistrictName(),
                    townshipDistrictMap,
                    villageDistrictMap,
                    groupDistrictMap);
            if (StringUtils.isBlank(importRowData.getStrPayTime())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "缴费时间为空");
            } else {
                try {
                    DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrPayTime() + "01");
                } catch (Exception e) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "缴费时间格式错误，格式应为yyyyMM");
                }
            }
            if (StringUtils.isBlank(importRowData.getStrPayLevel())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "缴费档次为空");
            } else if (!("一档".equals(importRowData.getStrPayLevel()) || "二档".equals(importRowData.getStrPayLevel()) || "三档".equals(importRowData.getStrPayLevel()))) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "缴费档次取值范围：一档、二档、三档");
            }
            if (null == importRowData.getPayMoney() || importRowData.getPayMoney() <= 0) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "缴费金额不能为空且必须大于0");
            }

            if (StringUtils.isNotBlank(importRowData.getStrSocialInsuranceTime())) {
                if (importRowData.getStrSocialInsuranceTime().length() != 6) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "参保时间格式应该为yyyyMM");
                } else {
                    try {
                        DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrSocialInsuranceTime() + "01");
                    } catch (Exception e) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "参保时间格式应该为yyyyMM");
                    }
                }
            }
            if (StringUtils.isBlank(importRowData.getStrState())) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "状态为空");
            } else if (!("正常".equals(importRowData.getStrState()) || "改办新失地".equals(importRowData.getStrState()) || "划转其他区".equals(importRowData.getStrState()) || "退出".equals(importRowData.getStrState()) || "死亡".equals(importRowData.getStrState()))) {
                addRowIndexFailInfo(rowFailInfos, rowIndex, "状态取值范围：正常、改办新失地、划转其他区、退出、死亡");
            }

            if ("改办新失地".equals(importRowData.getStrState()) || "划转其他区".equals(importRowData.getStrState()) || "退出".equals(importRowData.getStrState()) || "死亡".equals(importRowData.getStrState())) {
                if (StringUtils.isBlank(importRowData.getStrQuitTime())) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "状态为：[" + importRowData.getStrState() + "]时，退出时间必填，格式为yyyyMMdd");
                } else {
                    try {
                        DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrQuitTime());
                    } catch (Exception e) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "状态为：[" + importRowData.getStrState() + "]时，退出时间必填，格式为yyyyMMdd");
                    }
                }
            }
            if ("正常".equals(importRowData.getStrState())) {
                if (StringUtils.isNotBlank(importRowData.getStrReturnFeeState())) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "状态为：[" + importRowData.getStrState() + "]时，不需要填写[是否已退费]列值");
                }
                if (StringUtils.isNotBlank(importRowData.getStrReturnFeeTime())) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "状态为：[" + importRowData.getStrState() + "]时，不需要填写[退费时间]列值");
                }
            } else {
                if (StringUtils.isBlank(importRowData.getStrReturnFeeState())) {
                    importRowData.setStrReturnFeeState("否");
                }
                if (!("是".equals(importRowData.getStrReturnFeeState()) || "否".equals(importRowData.getStrReturnFeeState()))) {
                    addRowIndexFailInfo(rowFailInfos, rowIndex, "是否已退费取值范围：是、否");
                }
                if ("是".equals(importRowData.getStrReturnFeeState())) {
                    if (StringUtils.isBlank(importRowData.getStrReturnFeeTime())) {
                        addRowIndexFailInfo(rowFailInfos, rowIndex, "是否已退费为：[" + importRowData.getStrReturnFeeState() + "]时，需要填写[退费时间]列值，格式为：yyyyMMdd");
                    } else {
                        try {
                            DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrReturnFeeTime());
                        } catch (Exception e) {
                            addRowIndexFailInfo(rowFailInfos, rowIndex, "是否已退费为：[" + importRowData.getStrReturnFeeState() + "]时，需要填写[退费时间]列值，格式为：yyyyMMdd");
                        }
                    }
                }
            }
        }

        if (MapUtils.isNotEmpty(rowFailInfos)) {
            return wrapRowFailInfos(rowFailInfos, 1);
        }

        // @part 2: 生成数据
        // @part 2.1: 初始化行政区
        Map<String, Map<String, Object>> chaiSangQuDistrictMap = new HashMap<>();
        Map<String, Object> chaiSangQuDistrict = new HashMap<>();
        chaiSangQuDistrict.put("DistrictName", ISimcDistrictService.CHAI_SANG_TOWNSHIP_DISTRICT_NAME);
        chaiSangQuDistrict.put("DistrictId", ISimcDistrictService.CHAI_SANG_TOWNSHIP_DISTRICT_ID);
        chaiSangQuDistrictMap.put(ISimcDistrictService.CHAI_SANG_TOWNSHIP_DISTRICT_NAME, chaiSangQuDistrict);
        initAndSaveDistrict(townshipDistrictMap, chaiSangQuDistrictMap, "乡镇", "市(含：直辖市市辖区)", false);
        initAndSaveDistrict(villageDistrictMap, townshipDistrictMap, "村（社区）", "乡镇", false);
        initAndSaveDistrict(groupDistrictMap, villageDistrictMap, "组", "村（社区）", true);// 组不存在是，新增

        for (SimcResidentOldLandLosingImportRowData importRowData : importRowDataList) {
            SimcResidentOldLandLosing simcResidentOldLandLosing = this.simcResidentOldLandLosingMapper.selectByPrimaryKey(importRowData.getResidentIdCardNo());
            boolean isCreate = true;
            if (null == simcResidentOldLandLosing) {
                simcResidentOldLandLosing = new SimcResidentOldLandLosing();
            } else {
                isCreate = false;
            }
            simcResidentOldLandLosing.setResidentIdCardNo(importRowData.getResidentIdCardNo());
            simcResidentOldLandLosing.setResidentName(importRowData.getResidentName());
            simcResidentOldLandLosing.setResidentSex(SimcUtil.convertSex(importRowData.getResidentSex()));
            simcResidentOldLandLosing.setResidentBirthDate(SimcUtil.getBirthDateFromIdCardNo(importRowData.getResidentIdCardNo()));
            simcResidentOldLandLosing.setResidentPhone(importRowData.getResidentPhone());
            simcResidentOldLandLosing.setResidentTownshipDistrictId(getDistrictId(townshipDistrictMap, importRowData.getResidentTownshipDistrictName()));
            simcResidentOldLandLosing.setResidentVillageDistrictId(getDistrictId(villageDistrictMap, importRowData.getResidentTownshipDistrictName() + "_" + importRowData.getResidentVillageDistrictName()));
            simcResidentOldLandLosing.setResidentGroupDistrictId(getDistrictId(groupDistrictMap, importRowData.getResidentTownshipDistrictName() + "_" + importRowData.getResidentVillageDistrictName() + "_" + importRowData.getResidentGroupDistrictName()));
            simcResidentOldLandLosing.setPayTime(DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrPayTime() + "01"));
            simcResidentOldLandLosing.setPayLevel(SimcUtil.convertPayLevel(importRowData.getStrPayLevel()));
            simcResidentOldLandLosing.setPayMoney(importRowData.getPayMoney());
            simcResidentOldLandLosing.setBank(importRowData.getBank());
            simcResidentOldLandLosing.setBankCode(importRowData.getBankCode());
            simcResidentOldLandLosing.setTheFirstReceiveTime(SimcUtil.calTheFirstReceiveTime(simcResidentOldLandLosing.getResidentBirthDate(), simcResidentOldLandLosing.getResidentSex()));// 生日的下个月
            simcResidentOldLandLosing.setTheFirstYearPerMonthReceiveMoney(SimcUtil.getTheFirstYearPerMonthReceiveMoney(simcResidentOldLandLosing.getPayLevel()));
            simcResidentOldLandLosing.setState(SimcUtil.convertResidentOldLandLosingState(importRowData.getStrState()));
            if (StringUtils.isNotBlank(importRowData.getStrQuitTime())) {
                simcResidentOldLandLosing.setQuitTime(DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrQuitTime()));
            }
            simcResidentOldLandLosing.setSocialInsuranceRemark(importRowData.getSocialInsuranceRemark());
            if (StringUtils.isNotBlank(importRowData.getStrSocialInsuranceTime())) {
                simcResidentOldLandLosing.setSocialInsuranceTime(DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrSocialInsuranceTime() + "01"));
            }

            if ("是".equals(importRowData.getStrReturnFeeState())) {
                simcResidentOldLandLosing.setReturnFeeState("2");
            } else {
                simcResidentOldLandLosing.setReturnFeeState("1");
            }

            if (StringUtils.isNotBlank(importRowData.getStrReturnFeeTime())) {
                simcResidentOldLandLosing.setReturnFeeTime(DateUtils.dateTime(DateUtils.YYYYMMDD, importRowData.getStrReturnFeeTime()));
            }

            // 已退费，根据退出时间计算退费金额
            if ("2".equals(simcResidentOldLandLosing.getReturnFeeState()) && simcResidentOldLandLosing.getQuitTime() != null) {
                double returnFee = SimcUtil.calReturnFee(simcResidentOldLandLosing.getPayMoney(),
                                                         simcResidentOldLandLosing.getTheFirstReceiveTime(),
                                                         simcResidentOldLandLosing.getQuitTime(),
                                                         simcResidentOldLandLosing.getPayLevel());
                simcResidentOldLandLosing.setReturnFee(returnFee);
            }
            if (isCreate) {
                simcResidentOldLandLosing.setCreateUserId(userId);
                simcResidentOldLandLosing.setCreateTime(currentDate);
                this.simcResidentOldLandLosingMapper.insert(simcResidentOldLandLosing);
            } else {
                simcResidentOldLandLosing.setModifyUserId(userId);
                simcResidentOldLandLosing.setModifyTime(currentDate);
                this.simcResidentOldLandLosingMapper.updateByPrimaryKey(simcResidentOldLandLosing);
            }
        }
        return "温馨提示</br>导入成功";
    }
}
