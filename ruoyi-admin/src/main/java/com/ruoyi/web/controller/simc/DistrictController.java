package com.ruoyi.web.controller.simc;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.simc.domain.SimcDistrict;
import com.ruoyi.simc.service.ISimcDistrictService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行政区域信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/simc/district")
public class DistrictController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISimcDistrictService districtService;

    /**
     * 获取柴桑区行政区域列表
     */
    @GetMapping("/listChaiSangDistrict")
    public AjaxResult listChaiSangDistrict() throws Exception {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("subordinateDistricts", districtService.queryChaiSangSubordinateDistrict());
        return ajax;
    }

    /**
     * 获取行政区域列表
     */
    @GetMapping("/list")
    public AjaxResult list(SimcDistrict district) {
        List<SimcDistrict> districts = districtService.selectSimcDistrictList(district);
        return success(districts);
    }

    /**
     * 查询列表（排除节点）
     */
    @GetMapping("/list/exclude/{districtId}")
    public AjaxResult excludeChild(@PathVariable(value = "districtId", required = false) Long districtId) {
        List<SimcDistrict> districts = districtService.selectSimcDistrictList(new SimcDistrict());
        districts.removeIf(d -> d.getDistrictId().intValue() == districtId);
        return success(districts);
    }

    /**
     * 根据行政区域编号获取详细信息
     */
    @GetMapping(value = "/getDistrictByDistrictId/{districtId}")
    public AjaxResult getDistrictByDistrictId(@PathVariable Long districtId) {
        return success(districtService.selectSimcDistrictByDistrictId(districtId));
    }

    /**
     * 新增行政区域
     */
    @Log(title = "行政区域管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SimcDistrict district) throws Exception {
        return toAjax(districtService.insertSimcDistrict(district));
    }

    /**
     * 修改行政区域
     */
    @Log(title = "行政区域管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SimcDistrict district) throws Exception {
        return toAjax(districtService.updateSimcDistrict(district));
    }

    /**
     * 删除行政区域
     */
    @Log(title = "行政区域管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{districtId}")
    public AjaxResult remove(@PathVariable Long districtId) throws Exception {
        return toAjax(districtService.deleteDistrictById(districtId));
    }
}
