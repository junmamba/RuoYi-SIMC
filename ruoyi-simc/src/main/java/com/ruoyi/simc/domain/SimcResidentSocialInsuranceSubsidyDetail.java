package com.ruoyi.simc.domain;

import java.util.Date;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-12 19:29
 */
public class SimcResidentSocialInsuranceSubsidyDetail {
    private Date subsidyTime;
    private Double subsidyMoney;

    public SimcResidentSocialInsuranceSubsidyDetail(Date subsidyTime, Double subsidyMoney) {
        this.subsidyTime = subsidyTime;
        this.subsidyMoney = subsidyMoney;
    }

    public Date getSubsidyTime() {
        return subsidyTime;
    }

    public void setSubsidyTime(Date subsidyTime) {
        this.subsidyTime = subsidyTime;
    }

    public Double getSubsidyMoney() {
        return subsidyMoney;
    }

    public void setSubsidyMoney(Double subsidyMoney) {
        this.subsidyMoney = subsidyMoney;
    }
}
