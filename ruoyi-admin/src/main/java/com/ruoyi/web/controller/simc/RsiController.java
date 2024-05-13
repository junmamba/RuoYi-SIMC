package com.ruoyi.web.controller.simc;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.simc.domain.SimcResidentSocialInsurance;
import com.ruoyi.simc.domain.SimcResidentSocialInsuranceImportRowData;
import com.ruoyi.simc.service.ISimcImportService;
import com.ruoyi.simc.service.ISimcResidentSocialInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    /**
     * 获取居民社保列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SimcResidentSocialInsurance params) throws Exception {
        startPage();
        List<SimcResidentSocialInsurance> list = this.residentSocialInsuranceService.selectList(params);
        return getDataTable(list);
    }

    @Log(title = "居民社保档案管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SimcResidentSocialInsuranceImportRowData> util = new ExcelUtil<SimcResidentSocialInsuranceImportRowData>(SimcResidentSocialInsuranceImportRowData.class);
        List<SimcResidentSocialInsuranceImportRowData> rowDataList = util.importExcel(file.getInputStream(), 2);
        String message = this.simcImportService.importResidentSocialInsuranceDataList(rowDataList, getUserId());
        return success(message);
    }

    @Log(title = "居民社保档案管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SimcResidentSocialInsurance params) throws Exception {
        List<SimcResidentSocialInsurance> list = this.residentSocialInsuranceService.selectList(params);
        ExcelUtil<SimcResidentSocialInsurance> util = new ExcelUtil<SimcResidentSocialInsurance>(SimcResidentSocialInsurance.class);
        util.exportExcel(response, list, "基本养老保险补贴汇总表");
    }
}
