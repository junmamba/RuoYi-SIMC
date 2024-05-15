package com.ruoyi.simc.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simc.domain.SimcDistrict;
import com.ruoyi.simc.domain.SimcResidentOldLandLosing;
import com.ruoyi.simc.mapper.SimcResidentOldLandLosingMapper;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.simc.service.ISimcResidentOldLandLosingService;
import com.ruoyi.simc.util.SimcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-14 09:47
 */
@Service
@Transactional
public class SimcResidentOldLandLosingServiceImpl implements ISimcResidentOldLandLosingService {
    @Autowired
    private SimcResidentOldLandLosingMapper simcResidentOldLandLosingMapper;
    @Autowired
    private ISimcDistrictService simcDistrictService;

    @Override
    public List<SimcResidentOldLandLosing> selectList(SimcResidentOldLandLosing roll) throws Exception {
        Map<String, Long> districtParam = this.simcDistrictService.buildQueryDistrictParam(roll.getDistrictId());
        if (districtParam.containsKey("townshipDistrictId")) {
            roll.setResidentTownshipDistrictId(districtParam.get("townshipDistrictId"));
        }
        if (districtParam.containsKey("villageDistrictId")) {
            roll.setResidentVillageDistrictId(districtParam.get("villageDistrictId"));
        }
        if (districtParam.containsKey("groupDistrictId")) {
            roll.setResidentGroupDistrictId(districtParam.get("groupDistrictId"));
        }
        if (roll.getParams().containsKey("beginPayTime")) {
            roll.getParams().put("beginPayTime", roll.getParams().get("beginPayTime") + "-01");
        }
        if (roll.getParams().containsKey("endPayTime")) {
            roll.getParams().put("endPayTime", roll.getParams().get("endPayTime") + "-28");
        }
        List<SimcResidentOldLandLosing> list = this.simcResidentOldLandLosingMapper.selectList(roll);
        Set<Long> districtIds = new HashSet<>();
        for (int i = 0; null != list && i < list.size(); i++) {
            districtIds.add(list.get(i).getResidentTownshipDistrictId());
            districtIds.add(list.get(i).getResidentVillageDistrictId());
            districtIds.add(list.get(i).getResidentGroupDistrictId());
        }
        Date now = DateUtils.getNowDate();
        List<SimcDistrict> simcDistrictList = this.simcDistrictService.queryByDistrictIdList(new ArrayList<>(districtIds));
        for (int i = 0; null != list && i < list.size(); i++) {
            list.get(i).setDistrictName(simcDistrictService.getDistrictName(list.get(i).getResidentTownshipDistrictId(), list.get(i).getResidentVillageDistrictId(), list.get(i).getResidentGroupDistrictId(), simcDistrictList));
            if (null != list.get(i).getPayTime()) {
                list.get(i).setStrPayTime(DateUtils.parseDateToStr(DateUtils.YYYYMM, list.get(i).getPayTime()));
            }
            if (null != list.get(i).getTheFirstReceiveTime()) {
                list.get(i).setStrTheFirstReceiveTime(DateUtils.parseDateToStr(DateUtils.YYYYMM, list.get(i).getTheFirstReceiveTime()));
            }
            if (null != list.get(i).getQuitTime()) {
                list.get(i).setStrQuitTime(DateUtils.parseDateToStr(DateUtils.YYYYMMDD, list.get(i).getQuitTime()));
            }
            if (null != list.get(i).getReturnFeeTime()) {
                list.get(i).setStrReturnFeeTime(DateUtils.parseDateToStr(DateUtils.YYYYMMDD, list.get(i).getReturnFeeTime()));
            }
            double receivedTotalFee = 0;
            if (null != list.get(i).getQuitTime()) {
                receivedTotalFee = SimcUtil.calReceivedTotalFee(list.get(i).getTheFirstReceiveTime(), list.get(i).getQuitTime(), SimcUtil.getTheFirstYearPerMonthReceiveMoney(list.get(i).getPayLevel()));
            } else {
                receivedTotalFee = SimcUtil.calReceivedTotalFee(list.get(i).getTheFirstReceiveTime(), now, SimcUtil.getTheFirstYearPerMonthReceiveMoney(list.get(i).getPayLevel()));
            }
            list.get(i).setReceivedTotalFee(receivedTotalFee);
        }
        return list;
    }

    @Override
    public SimcResidentOldLandLosing selectByResidentIdCardNo(String residentIdCardNo) throws Exception {
        if (StringUtils.isBlank(residentIdCardNo)) {
            return null;
        }
        return this.simcResidentOldLandLosingMapper.selectByPrimaryKey(residentIdCardNo);
    }

    /**
     * 操作居民老失地档案
     *
     * @param simcResidentOldLandLosing
     * @return
     * @throws Exception
     */
    public void oper(SimcResidentOldLandLosing simcResidentOldLandLosing, Long userId) throws Exception {
        if (StringUtils.isBlank(simcResidentOldLandLosing.getId())) {
            throw new Exception("表单数据被篡改");
        }
        SimcResidentOldLandLosing residentOldLandLosing = this.simcResidentOldLandLosingMapper.selectByPrimaryKey(simcResidentOldLandLosing.getResidentIdCardNo());
        if (!simcResidentOldLandLosing.getId().equals(simcResidentOldLandLosing.getResidentIdCardNo())) {
            // 说明修改了身份证号码
            if (null != residentOldLandLosing) {
                throw new Exception("身份证号码：" + simcResidentOldLandLosing.getResidentIdCardNo() + "已经存在其他老失地档案数据");
            }
        } else {
            if (null == residentOldLandLosing) {
                throw new Exception("根据身份证号码：" + simcResidentOldLandLosing.getResidentIdCardNo() + "查询不到老失地档案数据");
            }
        }

        if (StringUtils.isBlank(simcResidentOldLandLosing.getResidentIdCardNo())) {
            throw new Exception("身份证号码不能为空");
        }
        if (simcResidentOldLandLosing.getResidentIdCardNo().length() != 18) {
            throw new Exception("身份证号码必须是18位");
        }
        if (StringUtils.isBlank(simcResidentOldLandLosing.getResidentName())) {
            throw new Exception("姓名不能为空");
        }
        String sex = simcResidentOldLandLosing.getResidentIdCardNo().substring(16, 17);
        if (!("1".equals(sex) || "2".equals(sex))) {
            throw new Exception("身份证号码第17位填写错误");
        }
        if (null == simcResidentOldLandLosing.getResidentTownshipDistrictId() || simcResidentOldLandLosing.getResidentTownshipDistrictId() <= 0) {
            throw new Exception("请选择乡镇");
        }
        if (null == simcResidentOldLandLosing.getResidentVillageDistrictId() || simcResidentOldLandLosing.getResidentVillageDistrictId() <= 0) {
            throw new Exception("请选择村（社区）");
        }
        if (null == simcResidentOldLandLosing.getResidentGroupDistrictId() || simcResidentOldLandLosing.getResidentGroupDistrictId() <= 0) {
            throw new Exception("请选择村");
        }

        if (!("1".equals(simcResidentOldLandLosing.getPayLevel()) || "2".equals(simcResidentOldLandLosing.getPayLevel()) || "3".equals(simcResidentOldLandLosing.getPayLevel()))) {
            throw new Exception("请选择正确的缴费档次");
        }
        if (null == simcResidentOldLandLosing.getPayTime()) {
            throw new Exception("请输入缴费时间");
        }
        if (null == simcResidentOldLandLosing.getPayMoney() || simcResidentOldLandLosing.getPayMoney() <= 0) {
            throw new Exception("请输入缴费金额");
        }

        if ("1".equals(simcResidentOldLandLosing.getState())) {// 正常状态
            if (null != simcResidentOldLandLosing.getQuitTime()) {
                throw new Exception("正常状态不允许选择退出时间");
            }
            if (!"1".equals(simcResidentOldLandLosing.getReturnFeeState())) {
                throw new Exception("正常状态时，退费状态必须为未退费");
            }
            if (null != simcResidentOldLandLosing.getReturnFeeTime()) {
                throw new Exception("正常状态不允许选择退费时间");
            }
            if (null != simcResidentOldLandLosing.getReturnFee() && simcResidentOldLandLosing.getReturnFee() > 0) {
                throw new Exception("正常状态不允许输入退费金额");
            }
        } else {// 退出状态
            if (null == simcResidentOldLandLosing.getQuitTime()) {
                throw new Exception("请选择退出时间");
            }
            if ("1".equals(simcResidentOldLandLosing.getReturnFeeState())) {// 未退费状态
                if (null != simcResidentOldLandLosing.getReturnFeeTime()) {
                    throw new Exception("未退费状态不允许选择退费时间");
                }
                if (null != simcResidentOldLandLosing.getReturnFee() && simcResidentOldLandLosing.getReturnFee() > 0) {
                    throw new Exception("未退费状态不允许输入退费金额");
                }
            } else if ("2".equals(simcResidentOldLandLosing.getReturnFeeState())) {// 已退费状态
                if (null == simcResidentOldLandLosing.getReturnFeeTime()) {
                    throw new Exception("已退费状态请选择退费时间");
                }
            }
            if (null != simcResidentOldLandLosing.getReturnFee() && simcResidentOldLandLosing.getReturnFee() < 0) {
                throw new Exception("退费金额必须不能小于0");
            }
        }

        simcResidentOldLandLosing.setResidentSex(Integer.parseInt(sex));

        // 生日
        simcResidentOldLandLosing.setResidentBirthDate(SimcUtil.getBirthDateFromIdCardNo(simcResidentOldLandLosing.getResidentIdCardNo()));
        // 首次领取时间
        simcResidentOldLandLosing.setTheFirstReceiveTime(SimcUtil.calTheFirstReceiveTime(simcResidentOldLandLosing.getResidentBirthDate(), simcResidentOldLandLosing.getResidentSex()));// 生日的下个月
        // 首月领取金额
        simcResidentOldLandLosing.setTheFirstYearPerMonthReceiveMoney(SimcUtil.getTheFirstYearPerMonthReceiveMoney(simcResidentOldLandLosing.getPayLevel()));
        if ("2".equals(simcResidentOldLandLosing.getReturnFeeState())//
                && null != simcResidentOldLandLosing.getQuitTime()
                && (null == simcResidentOldLandLosing.getReturnFee() || simcResidentOldLandLosing.getReturnFee() <= 0)) {
            double returnFee = SimcUtil.calReturnFee(simcResidentOldLandLosing.getPayMoney(),
                    simcResidentOldLandLosing.getTheFirstReceiveTime(),
                    simcResidentOldLandLosing.getQuitTime(),
                    simcResidentOldLandLosing.getPayLevel());
            simcResidentOldLandLosing.setReturnFee(returnFee);
        }
        simcResidentOldLandLosing.setModifyTime(DateUtils.getNowDate());
        simcResidentOldLandLosing.setModifyUserId(userId);
        this.simcResidentOldLandLosingMapper.updateById(simcResidentOldLandLosing);
    }

    /**
     * 批量删除
     *
     * @param residentIdCardNos
     * @return 结果
     */
    public int delete(String[] residentIdCardNos) {
        for (int i = 0; null != residentIdCardNos && i < residentIdCardNos.length; i++) {
            this.simcResidentOldLandLosingMapper.deleteByPrimaryKey(residentIdCardNos[i]);
        }
        return 1;
    }
}
