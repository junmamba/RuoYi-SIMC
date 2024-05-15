package com.ruoyi.web.controller.simc;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.simc.domain.SimcResidentSocialInsuranceSubsidy;
import com.ruoyi.simc.domain.SimcResidentSocialInsuranceSubsidyImportRowData;
import com.ruoyi.simc.service.ISimcImportService;
import com.ruoyi.simc.service.ISimcResidentSocialInsuranceSubsidyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 居民社保补贴档案操作处理
 *
 * @author junmamba
 */
@RestController
@RequestMapping("/simc/rsis")
public class RsisController extends BaseController {
    @Autowired
    private ISimcResidentSocialInsuranceSubsidyService simcResidentSocialInsuranceSubsidyService;

    @Autowired
    private ISimcImportService simcImportService;

    /**
     * 获取居民社保列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SimcResidentSocialInsuranceSubsidy params) throws Exception {
        startPage();
        List<SimcResidentSocialInsuranceSubsidy> list = this.simcResidentSocialInsuranceSubsidyService.selectList(params);
        return getDataTable(list);
    }

    @Log(title = "居民社会保险补贴管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SimcResidentSocialInsuranceSubsidyImportRowData> util = new ExcelUtil<SimcResidentSocialInsuranceSubsidyImportRowData>(SimcResidentSocialInsuranceSubsidyImportRowData.class);
        List<SimcResidentSocialInsuranceSubsidyImportRowData> rowDataList = util.importExcel(file.getInputStream(), 0);
        String message = this.simcImportService.importResidentSocialInsuranceSubsidyDataList(rowDataList, getUserId());
        return success(message);
    }

    /**
     * 删除
     */
    @Log(title = "居民社会保险补贴管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{subsidyLogIds}")
    public AjaxResult remove(@PathVariable Long[] subsidyLogIds) {
        return toAjax(simcResidentSocialInsuranceSubsidyService.delete(subsidyLogIds));
    }
}
