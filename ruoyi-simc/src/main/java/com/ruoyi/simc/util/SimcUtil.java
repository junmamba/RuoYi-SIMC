package com.ruoyi.simc.util;

import com.ruoyi.common.utils.DateUtils;

import java.util.Date;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-15 11:02
 */
public class SimcUtil {
    public static Date getBirthDateFromIdCardNo(String idCardNo) throws Exception {
        return DateUtils.addHours(DateUtils.parseDate(idCardNo.substring(6, 14), DateUtils.YYYYMMDD), 1);
    }

    public static String convertSocialInsuranceType(String socialInsuranceType) {
        if ("城镇职工".equals(socialInsuranceType)) {
            return "1";
        } else
            return "2";
    }

    public static int convertProjectIsCityLevel(String projectIsCityLevel) {
        if ("是".equals(projectIsCityLevel)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int convertSex(String sex) {
        if ("男".equals(sex)) {
            return 1;
        } else if ("女".equals(sex)) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * 计算第一次领取时间
     *
     * @param birthDate
     * @param sex
     * @return
     */
    public static Date calTheFirstReceiveTime(Date birthDate, Integer sex) {
        int retirementAge = 60;
        if (2 == sex) {
            retirementAge = 50;
        }
        return DateUtils.getDateOfNextMonthFirstDay(DateUtils.addYears(birthDate, retirementAge));
    }

    public static double getTheFirstYearPerMonthReceiveMoney(String payLevel) throws Exception {
        if ("1".equals(payLevel)) {
            return 150;
        } else if ("2".equals(payLevel)) {
            return 160;
        } else if ("3".equals(payLevel)) {
            return 180;
        } else {
            throw new Exception("缴费档次值非法");
        }
    }

    public static String convertResidentOldLandLosingState(String residentOldLandLosingState) throws Exception {
        if ("正常".equals(residentOldLandLosingState)) {
            return "1";
        } else if ("改办新失地".equals(residentOldLandLosingState)) {
            return "2";
        } else if ("划转其他区".equals(residentOldLandLosingState)) {
            return "3";
        } else if ("退出".equals(residentOldLandLosingState)) {
            return "4";
        } else if ("死亡".equals(residentOldLandLosingState)) {
            return "5";
        } else {
            throw new Exception("状态值非法");
        }
    }

    public static String convertPayLevel(String strPayLevel) throws Exception {
        if ("一档".equals(strPayLevel)) {
            return "1";
        } else if ("二档".equals(strPayLevel)) {
            return "2";
        } else if ("三档".equals(strPayLevel)) {
            return "3";
        } else {
            throw new Exception("缴费档次值非法");
        }
    }

    /**
     * 计算退费金额
     *
     * @param payMoney
     * @param theFirstReceiveDate
     * @param receiveEndDate
     * @param payLevel
     * @return
     * @throws Exception
     */
    public static double calReturnFee(double payMoney, Date theFirstReceiveDate, Date receiveEndDate, String payLevel) throws Exception {
        double receivedTotalFee = calReceivedTotalFee(theFirstReceiveDate, receiveEndDate, getTheFirstYearPerMonthReceiveMoney(payLevel));
        double returnFee = payMoney - receivedTotalFee;
        return returnFee <= 0 ? 0L : returnFee;
    }

    /**
     * 计算已领取的总费用
     *
     * @param theFirstReceiveDate
     * @param receiveEndDate
     * @param theFirstYearPerMonthReceiveFee
     * @return
     * @throws Exception
     */
    public static double calReceivedTotalFee(Date theFirstReceiveDate, Date receiveEndDate, double theFirstYearPerMonthReceiveFee) throws Exception {
        Date receiveDate = DateUtils.dateTime(DateUtils.YYYYMMDD, (DateUtils.parseDateToStr(DateUtils.YYYYMMDD, theFirstReceiveDate)));
        receiveEndDate = DateUtils.dateTime(DateUtils.YYYYMMDD, (DateUtils.parseDateToStr(DateUtils.YYYYMMDD, receiveEndDate)));
        int total = 0;
        if (theFirstReceiveDate.compareTo(receiveEndDate) == 0) {// 当月，则默认发了
            return theFirstYearPerMonthReceiveFee;
        } else if (theFirstReceiveDate.compareTo(receiveEndDate) > 0) {// 如果首次领取时间晚于receiveEndDate，则说明还没领取，返回0
            return 0;
        } else {
            int count = 0;
            while (true) {
                int receiveDateOfMonth = DateUtils.getDateOfMonth(receiveDate);
                if (count >= 12 && receiveDateOfMonth == 7) {// 每年七月加15块钱
                    theFirstYearPerMonthReceiveFee += 15;// 没有领满一年不能加15
                }
                total += theFirstYearPerMonthReceiveFee;
                receiveDate = DateUtils.addMonths(receiveDate, 1);
                count++;
                if (receiveDate.compareTo(receiveEndDate) > 0) {
                    break;
                }
            }
        }
        return total;
    }
}
