package com.ruoyi.simc.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simc.domain.*;
import com.ruoyi.simc.mapper.*;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.simc.service.ISimcResidentSocialInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 11:13
 */
@Service
@Transactional
public class SimcResidentSocialInsuranceServiceImpl implements ISimcResidentSocialInsuranceService {
    @Autowired
    private SimcResidentSocialInsuranceMapper simcResidentSocialInsuranceMapper;

    @Autowired
    private SimcResidentMapper simcResidentMapper;

    @Autowired
    private SimcResidentSocialInsuranceSubsidyMapper simcResidentSocialInsuranceSubsidyMapper;

    @Autowired
    private SimcFamilyMapper simcFamilyMapper;

    @Autowired
    private SimcFamilyMemberMapper simcFamilyMemberMapper;

    @Autowired
    private SimcFamilyLandLosingMapper simcFamilyLandLosingMapper;

    @Autowired
    private ISimcDistrictService simcDistrictService;

    @Override
    public List<SimcResidentSocialInsurance> selectList(SimcResidentSocialInsurance rsi) throws Exception {
        Map<String, Long> districtParam = this.simcDistrictService.buildQueryDistrictParam(rsi.getDistrictId());
        if (districtParam.containsKey("townshipDistrictId")) {
            rsi.setResidentTownshipDistrictId(districtParam.get("townshipDistrictId"));
        }
        if (districtParam.containsKey("villageDistrictId")) {
            rsi.setResidentVillageDistrictId(districtParam.get("villageDistrictId"));
        }
        if (districtParam.containsKey("groupDistrictId")) {
            rsi.setResidentGroupDistrictId(districtParam.get("groupDistrictId"));
        }
        List<SimcResidentSocialInsurance> list = this.simcResidentSocialInsuranceMapper.selectList(rsi);

        Set<Long> districtIds = new HashSet<>();
        for (int i = 0; null != list && i < list.size(); i++) {
            districtIds.add(list.get(i).getResidentTownshipDistrictId());
            districtIds.add(list.get(i).getResidentVillageDistrictId());
            districtIds.add(list.get(i).getResidentGroupDistrictId());
            list.get(i).setStrSocialInsuranceJointApprovalTime(DateUtils.parseDateToStr("yyyyMMdd", list.get(i).getSocialInsuranceJointApprovalTime()));
        }
        LocalDate currentDate = LocalDate.now();
        List<SimcDistrict> simcDistrictList = this.simcDistrictService.queryByDistrictIdList(new ArrayList<>(districtIds));
        for (int i = 0; null != list && i < list.size(); i++) {
            list.get(i).setDistrictName(simcDistrictService.getDistrictName(list.get(i).getResidentTownshipDistrictId(), list.get(i).getResidentVillageDistrictId(), list.get(i).getResidentGroupDistrictId(), simcDistrictList));
            String birthDate = DateUtils.parseDateToStr(DateUtils.YYYYMMDD, list.get(i).getResidentBirthDate());
            LocalDate birthLocalDate = LocalDate.of(Integer.parseInt(birthDate.substring(0, 4)), Integer.parseInt(birthDate.substring(4, 6)), Integer.parseInt(birthDate.substring(6, 8)));
            int year = Period.between(birthLocalDate, currentDate).getYears();
            int month = Period.between(birthLocalDate, currentDate).getMonths();

            list.get(i).setAge(month > 0 ? year + "+" : String.valueOf(year));

            int retireAge = 60;
            if ("1".equals(list.get(i).getSocialInsuranceType()) && list.get(i).getResidentSex() == 2) {
                // 城镇职工 && 女性 --> 退休年龄年龄为55，其他均为60
                retireAge = 55;
            }
            int months = year * 12 + month;
            if (months >= retireAge * 12) {// 退休
                list.get(i).setRetireState(2);
            } else if (months >= (retireAge - 1) * 12 && months < retireAge * 12) {// 即将退休
                list.get(i).setRetireState(1);
            } else {// 未退休
                list.get(i).setRetireState(0);
            }
        }
        return list;
    }

    @Override
    public SimcResidentSocialInsurance selectByResidentIdCardNo(String residentIdCardNo) throws Exception {
        if (StringUtils.isBlank(residentIdCardNo)) {
            return null;
        }
        return this.simcResidentSocialInsuranceMapper.selectByPrimaryKey(residentIdCardNo);
    }

    /**
     * 操作居民社会保险档案
     *
     * @param
     * @return
     * @throws Exception
     */
    public void oper(SimcResidentSocialInsurance residentSocialInsurance, Long userId) throws Exception {
        if (StringUtils.isBlank(residentSocialInsurance.getResidentIdCardNo())) {
            throw new Exception("身份证号码不能为空");
        }
        if (residentSocialInsurance.getResidentIdCardNo().length() != 18) {
            throw new Exception("身份证号码必须是18位");
        }
        if (StringUtils.isBlank(residentSocialInsurance.getResidentName())) {
            throw new Exception("姓名不能为空");
        }
        if (null == residentSocialInsurance.getResidentTownshipDistrictId() || residentSocialInsurance.getResidentTownshipDistrictId() <= 0) {
            throw new Exception("请选择乡镇");
        }
        if (null == residentSocialInsurance.getResidentVillageDistrictId() || residentSocialInsurance.getResidentVillageDistrictId() <= 0) {
            throw new Exception("请选择村（社区）");
        }
        if (null == residentSocialInsurance.getResidentGroupDistrictId() || residentSocialInsurance.getResidentGroupDistrictId() <= 0) {
            throw new Exception("请选择村");
        }
        if (null == residentSocialInsurance.getSocialInsuranceType()) {
            throw new Exception("请选择参保类型");
        }
        if (!("1".equals(residentSocialInsurance.getSocialInsuranceType()) || "2".equals(residentSocialInsurance.getSocialInsuranceType()))) {
            throw new Exception("参保类型必须为城镇职工或城乡居民");
        }
        if (null == residentSocialInsurance.getSocialInsuranceState()) {
            throw new Exception("请选择参保状态");
        }
        if (!("1".equals(residentSocialInsurance.getSocialInsuranceType())
                || "2".equals(residentSocialInsurance.getSocialInsuranceType())
                || "3".equals(residentSocialInsurance.getSocialInsuranceType())
                || "4".equals(residentSocialInsurance.getSocialInsuranceType())
                || "99".equals(residentSocialInsurance.getSocialInsuranceType()))) {
            throw new Exception("参保类型必须为正常、退休、死亡、退出或其他");
        }
        if (null == residentSocialInsurance.getSocialInsuranceJointApprovalTime()) {
            throw new Exception("请选择审批通过时间");
        }

        Date now = DateUtils.getNowDate();

        SimcResidentSocialInsurance dbSimcResidentSocialInsurance = this.simcResidentSocialInsuranceMapper.selectByPrimaryKey(residentSocialInsurance.getResidentIdCardNo());
        if (null == dbSimcResidentSocialInsurance) {
            throw new Exception("根据身份证号码：" + residentSocialInsurance.getResidentIdCardNo() + "查询不到社保档案数据");
        }
        if ("2".equals(residentSocialInsurance.getSocialInsuranceState())) {// 如果选择了退休，则需要校验
            int retireAge = 60;
            if ("1".equals(residentSocialInsurance.getSocialInsuranceType()) && dbSimcResidentSocialInsurance.getResidentSex() == 2) {
                // 城镇职工 && 女性 --> 退休年龄年龄为55，其他均为60
                retireAge = 55;
            }
            String socialInsuranceType = "1".equals(residentSocialInsurance.getSocialInsuranceType()) ? "城镇职工" : "城乡居民";
            String sex = (2 == dbSimcResidentSocialInsurance.getResidentSex() ? "女性" : "男性");
            Date date = DateUtils.addMonths(dbSimcResidentSocialInsurance.getResidentBirthDate(), retireAge * 12);
            if (date.compareTo(now) > 0) {
                throw new Exception(sex + socialInsuranceType + "需要满足" + retireAge + "才可以将参保状态设置为退休");
            }
        }


        dbSimcResidentSocialInsurance = new SimcResidentSocialInsurance();
        dbSimcResidentSocialInsurance.setResidentIdCardNo(residentSocialInsurance.getResidentIdCardNo());
        dbSimcResidentSocialInsurance.setResidentName(residentSocialInsurance.getResidentName());
        dbSimcResidentSocialInsurance.setResidentTownshipDistrictId(residentSocialInsurance.getResidentTownshipDistrictId());
        dbSimcResidentSocialInsurance.setResidentVillageDistrictId(residentSocialInsurance.getResidentVillageDistrictId());
        dbSimcResidentSocialInsurance.setResidentGroupDistrictId(residentSocialInsurance.getResidentGroupDistrictId());
        dbSimcResidentSocialInsurance.setSocialInsuranceType(residentSocialInsurance.getSocialInsuranceType());
        dbSimcResidentSocialInsurance.setSocialInsuranceState(residentSocialInsurance.getSocialInsuranceState());
        dbSimcResidentSocialInsurance.setResidentPhone(residentSocialInsurance.getResidentPhone());
        dbSimcResidentSocialInsurance.setSocialInsuranceJointApprovalTime(residentSocialInsurance.getSocialInsuranceJointApprovalTime());
        dbSimcResidentSocialInsurance.setRemark(residentSocialInsurance.getRemark());
        dbSimcResidentSocialInsurance.setModifyUserId(userId);
        dbSimcResidentSocialInsurance.setModifyTime(now);
        this.simcResidentSocialInsuranceMapper.updateByPrimaryKeySelective(dbSimcResidentSocialInsurance);

        SimcResident simcResident = new SimcResident();
        simcResident.setIdCardNo(residentSocialInsurance.getResidentIdCardNo());
        simcResident.setName(residentSocialInsurance.getResidentName());
        simcResident.setPhone(residentSocialInsurance.getResidentPhone());
        simcResident.setTownshipDistrictId(residentSocialInsurance.getResidentTownshipDistrictId());
        simcResident.setVillageDistrictId(residentSocialInsurance.getResidentVillageDistrictId());
        simcResident.setGroupDistrictId(residentSocialInsurance.getResidentGroupDistrictId());
        if ("3".equals(dbSimcResidentSocialInsurance.getSocialInsuranceState())) {
            simcResident.setLivingState("0");
        }
        simcResident.setModifyUserId(userId);
        simcResident.setModifyTime(now);
        this.simcResidentMapper.updateByPrimaryKeySelective(simcResident);

        SimcResidentSocialInsuranceSubsidy modifySubsidy = new SimcResidentSocialInsuranceSubsidy();
        modifySubsidy.setResidentIdCardNo(residentSocialInsurance.getResidentIdCardNo());
        modifySubsidy.setResidentName(residentSocialInsurance.getResidentName());
        modifySubsidy.setResidentPhone(residentSocialInsurance.getResidentPhone());
        modifySubsidy.setResidentTownshipDistrictId(residentSocialInsurance.getResidentTownshipDistrictId());
        modifySubsidy.setResidentVillageDistrictId(residentSocialInsurance.getResidentVillageDistrictId());
        modifySubsidy.setResidentGroupDistrictId(residentSocialInsurance.getResidentGroupDistrictId());
        modifySubsidy.setSocialInsuranceType(residentSocialInsurance.getSocialInsuranceType());
        modifySubsidy.setModifyUserId(userId);
        modifySubsidy.setModifyTime(now);
        this.simcResidentSocialInsuranceSubsidyMapper.updateByResidentIdCardNoSelective(modifySubsidy);

        SimcFamily dbSimcFamily = this.simcFamilyMapper.selectByPrimaryKey(residentSocialInsurance.getResidentIdCardNo());
        if (null != dbSimcFamily) {// 说明是户主
            SimcFamily modifySimcFamily = new SimcFamily();
            modifySimcFamily.setFamilyNo(residentSocialInsurance.getResidentIdCardNo());
            modifySimcFamily.setFamilyName("<" + residentSocialInsurance.getResidentName() + ">家");
            modifySimcFamily.setFamilyHeadResidentName(residentSocialInsurance.getResidentName());
            modifySimcFamily.setTownshipDistrictId(residentSocialInsurance.getResidentTownshipDistrictId());
            modifySimcFamily.setVillageDistrictId(residentSocialInsurance.getResidentVillageDistrictId());
            modifySimcFamily.setGroupDistrictId(residentSocialInsurance.getResidentGroupDistrictId());
            modifySimcFamily.setModifyUserId(userId);
            modifySimcFamily.setModifyTime(now);
            this.simcFamilyMapper.updateByPrimaryKeySelective(modifySimcFamily);
        }

        SimcFamilyMember modifySimcFamilyMember = new SimcFamilyMember();
        modifySimcFamilyMember.setMemberName(residentSocialInsurance.getResidentName());
        modifySimcFamilyMember.setModifyOpId(userId);
        modifySimcFamilyMember.setModifyTime(now);
        modifySimcFamilyMember.setMemberCardNo(residentSocialInsurance.getResidentIdCardNo());
        simcFamilyMemberMapper.updateByMemberCardNoSelective(modifySimcFamilyMember);


        SimcFamilyLandLosing simcFamilyLandLosing = new SimcFamilyLandLosing();
        simcFamilyLandLosing.setFamilyNo(residentSocialInsurance.getResidentIdCardNo());
        simcFamilyLandLosing.setHeadResidentName(residentSocialInsurance.getResidentName());
        simcFamilyLandLosing.setModifyUserId(userId);
        simcFamilyLandLosing.setModifyTime(now);
        this.simcFamilyLandLosingMapper.updateByFamilyNoSelective(simcFamilyLandLosing);
    }
}
