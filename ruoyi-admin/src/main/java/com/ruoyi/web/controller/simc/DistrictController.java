package com.ruoyi.web.controller.simc;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.simc.service.ISimcDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-05 16:29
 */
@RestController
@RequestMapping("/simc/district")
public class DistrictController {
    @Autowired
    private ISimcDistrictService simcDistrictService;

    /**
     * 获取居民社保列表
     */
    @GetMapping("/listChaiSangDistrict")
    public AjaxResult listChaiSangDistrict() throws Exception {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("subordinateDistricts", simcDistrictService.queryChaiSangSubordinateDistrict());
        return ajax;
    }
}
