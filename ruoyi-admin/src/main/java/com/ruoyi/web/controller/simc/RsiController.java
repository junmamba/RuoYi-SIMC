package com.ruoyi.web.controller.simc;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.simc.domain.SimcDistrict;
import com.ruoyi.simc.domain.SimcResidentSocialInsurance;
import com.ruoyi.simc.domain.SimcResidentSocialInsuranceImportRowData;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.simc.service.ISimcImportService;
import com.ruoyi.simc.service.ISimcResidentSocialInsuranceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 居民社保档案操作处理
 *
 * @author junmamba
 */
@RestController
@RequestMapping("/simc/rsi")
public class RsiController extends BaseController {
    @Autowired
    private ISimcResidentSocialInsuranceService residentSocialInsuranceService;
    @Autowired
    private ISimcImportService simcImportService;

    @Autowired
    private ISimcDistrictService simcDistrictService;

    /**
     * 获取居民社保列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SimcResidentSocialInsurance params) throws Exception {
        startPage();
        List<SimcResidentSocialInsurance> list = this.residentSocialInsuranceService.selectList(params);
        Set<Long> districtIds = new HashSet<>();
        for (int i = 0; null != list && i < list.size(); i++) {
            districtIds.add(list.get(i).getResidentTownshipDistrictId());
            districtIds.add(list.get(i).getResidentVillageDistrictId());
            districtIds.add(list.get(i).getResidentGroupDistrictId());
            list.get(i).setStrSocialInsuranceJointApprovalTime(DateUtils.parseDateToStr("yyyyMMdd", list.get(i).getSocialInsuranceJointApprovalTime()));
        }

        List<SimcDistrict> simcDistrictList = this.simcDistrictService.queryByDistrictIdList(new ArrayList<>(districtIds));
        for (int i = 0; null != list && i < list.size(); i++) {
            list.get(i).setDistrictName(getDistrictName(list.get(i).getResidentTownshipDistrictId(),
                                                        list.get(i).getResidentVillageDistrictId(),
                                                        list.get(i).getResidentGroupDistrictId(),
                                                        simcDistrictList));
        }
        return getDataTable(list);
    }

    private String getDistrictName(Long townshipDistrictId, Long villageDistrictId, Long groupDistrictId, List<SimcDistrict> simcDistrictList) {
        SimcDistrict townshipDistrict = getSimcDistrict(simcDistrictList, townshipDistrictId);
        SimcDistrict villageDistrict = getSimcDistrict(simcDistrictList, villageDistrictId);
        SimcDistrict groupDistrict = getSimcDistrict(simcDistrictList, groupDistrictId);
        String townshipDistrictName = null == townshipDistrict ? StringUtils.EMPTY : townshipDistrict.getDistrictName();
        String villageDistrictName = (null == villageDistrict ? StringUtils.EMPTY : villageDistrict.getDistrictName());
        String groupDistrictName = null == groupDistrict ? StringUtils.EMPTY : groupDistrict.getDistrictName();
        return townshipDistrictName + "/" + villageDistrictName + "/" + groupDistrictName;
    }

    private SimcDistrict getSimcDistrict(List<SimcDistrict> simcDistrictList, Long districtId) {
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

    @Log(title = "居民社保档案管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SimcResidentSocialInsuranceImportRowData> util = new ExcelUtil<SimcResidentSocialInsuranceImportRowData>(SimcResidentSocialInsuranceImportRowData.class);
        List<SimcResidentSocialInsuranceImportRowData> rowDataList = util.importExcel(file.getInputStream(), 2);
        String message = this.simcImportService.importResidentSocialInsuranceDataList(rowDataList, getUserId());
        return success(message);
    }
}
