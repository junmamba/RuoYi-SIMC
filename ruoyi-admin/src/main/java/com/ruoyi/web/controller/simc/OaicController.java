package com.ruoyi.web.controller.simc;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.simc.domain.SimcOldAgriculturalInsuranceCancel;
import com.ruoyi.simc.domain.SimcOldAgriculturalInsuranceCancelImportRowData;
import com.ruoyi.simc.service.ISimcImportService;
import com.ruoyi.simc.service.ISimcOldAgriculturalInsuranceCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 老农保退保管理
 *
 * @author junmamba
 */
@RestController
@RequestMapping("/simc/oaic")
public class OaicController extends BaseController {
    @Autowired
    private ISimcOldAgriculturalInsuranceCancelService oldAgriculturalInsuranceCancelService;

    @Autowired
    private ISimcImportService simcImportService;

    /**
     * 获取老农保退保列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SimcOldAgriculturalInsuranceCancel params) throws Exception {
        startPage();
        List<SimcOldAgriculturalInsuranceCancel> list = this.oldAgriculturalInsuranceCancelService.selectList(params);
        return getDataTable(list);
    }

    @Log(title = "老农保退保管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SimcOldAgriculturalInsuranceCancelImportRowData> util = new ExcelUtil<SimcOldAgriculturalInsuranceCancelImportRowData>(SimcOldAgriculturalInsuranceCancelImportRowData.class);
        List<SimcOldAgriculturalInsuranceCancelImportRowData> rowDataList = util.importExcel(file.getInputStream(), 1);
        String message = this.simcImportService.importSimcOldAgriculturalInsuranceCancelDataList(rowDataList, getUserId());
        return success(message);
    }

    @GetMapping(value = {"/{residentIdCardNo}"})
    public AjaxResult getInfo(@PathVariable(value = "residentIdCardNo", required = true) String residentIdCardNo) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, this.oldAgriculturalInsuranceCancelService.selectByResidentIdCardNo(residentIdCardNo));
        return ajax;
    }

    /**
     * 操作
     */
    @Log(title = "老农保退保管理", businessType = BusinessType.UPDATE)
    @PostMapping("/oper")
    public AjaxResult oper(@Validated @RequestBody SimcOldAgriculturalInsuranceCancel oldAgriculturalInsuranceCancel) throws Exception {
        this.oldAgriculturalInsuranceCancelService.oper(oldAgriculturalInsuranceCancel, getUserId());
        return toAjax(1);
    }

    /**
     * 删除
     */
    @Log(title = "老农保退保管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{residentIdCardNos}")
    public AjaxResult remove(@PathVariable String[] residentIdCardNos) {
        return toAjax(oldAgriculturalInsuranceCancelService.delete(residentIdCardNos));
    }
}
