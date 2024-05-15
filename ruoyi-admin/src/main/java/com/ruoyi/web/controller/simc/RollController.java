package com.ruoyi.web.controller.simc;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.simc.domain.SimcResidentOldLandLosing;
import com.ruoyi.simc.domain.SimcResidentOldLandLosingImportRowData;
import com.ruoyi.simc.service.ISimcImportService;
import com.ruoyi.simc.service.ISimcResidentOldLandLosingService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 居民老失地档案
 *
 * @author junmamba
 */
@RestController
@RequestMapping("/simc/roll")
public class RollController extends BaseController {
    @Autowired
    private ISimcResidentOldLandLosingService simcResidentOldLandLosingService;

    @Autowired
    private ISimcImportService simcImportService;

    /**
     * 获取居民老失地列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SimcResidentOldLandLosing params) throws Exception {
        startPage();
        List<SimcResidentOldLandLosing> list = this.simcResidentOldLandLosingService.selectList(params);
        return getDataTable(list);
    }

    @Log(title = "居民老失地档案管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SimcResidentOldLandLosingImportRowData> util = new ExcelUtil<SimcResidentOldLandLosingImportRowData>(SimcResidentOldLandLosingImportRowData.class);
        List<SimcResidentOldLandLosingImportRowData> rowDataList = util.importExcel(file.getInputStream(), 1);
        String message = this.simcImportService.importSimcResidentOldLandLosingDataDataList(rowDataList, getUserId());
        return success(message);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/{residentIdCardNo}"})
    public AjaxResult getInfo(@PathVariable(value = "residentIdCardNo", required = true) String residentIdCardNo) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, this.simcResidentOldLandLosingService.selectByResidentIdCardNo(residentIdCardNo));
        return ajax;
    }

    /**
     * 操作
     */
    @Log(title = "居民老失地档案管理", businessType = BusinessType.UPDATE)
    @PostMapping("/oper")
    public AjaxResult oper(@Validated @RequestBody SimcResidentOldLandLosing residentOldLandLosing) throws Exception {
        this.simcResidentOldLandLosingService.oper(residentOldLandLosing, getUserId());
        return toAjax(1);
    }

    /**
     * 删除
     */
    @Log(title = "居民老失地档案管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{residentIdCardNos}")
    public AjaxResult remove(@PathVariable String[] residentIdCardNos) {
        return toAjax(simcResidentOldLandLosingService.delete(residentIdCardNos));
    }
}
