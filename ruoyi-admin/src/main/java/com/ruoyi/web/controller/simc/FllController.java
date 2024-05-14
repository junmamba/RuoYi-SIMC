package com.ruoyi.web.controller.simc;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.simc.domain.SimcFamilyLandLosing;
import com.ruoyi.simc.service.ISimcFamilyLandLosingService;
import com.ruoyi.simc.service.ISimcResidentSocialInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 居民社保档案操作处理
 *
 * @author junmamba
 */
@RestController
@RequestMapping("/simc/fll")
public class FllController extends BaseController {
    @Autowired
    private ISimcFamilyLandLosingService simcFamilyLandLosingService;

    /**
     * 获取居民社保列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SimcFamilyLandLosing params) throws Exception {
        startPage();
        List<SimcFamilyLandLosing> list = this.simcFamilyLandLosingService.selectList(params);
        return getDataTable(list);
    }
}
