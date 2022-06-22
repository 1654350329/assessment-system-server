package com.tree.clouds.assessment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.model.vo.PageParam;
import com.tree.clouds.assessment.service.*;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 首页数据 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/front-page")
@Api(tags = "首页数据")
public class FrontPageController {

    @Autowired
    private IndicatorReportService indicatorReportService;
    @Autowired
    private UnitManageService unitManageService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private MatterListService matterListService;
    @Autowired
    private RoleManageService roleManageService;


    @PostMapping("/matterListPage")
    @ApiOperation(value = "待办列表")
    @Log("待办列表")
    public RestResponse<IPage<MatterList>> matterListPage(@RequestBody PageParam pageVO) {
        IPage<MatterList> page = matterListService.matterListPage(pageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/updateMatter/{id}")
    @ApiOperation(value = "修改待办列表状态")
    @Log("修改待办列表状态")
    public RestResponse<Boolean> updateMatter(@PathVariable String id) {
        matterListService.updateMatter(id);
        return RestResponse.ok(true);
    }


    @Log("首页数据")
    @PostMapping("/getAllData")
    @ApiOperation(value = "首页数据")
    public RestResponse<Map<String, Object>> getAllData() {
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> code = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        String unitId = LoginUserUtil.getUnitId();


        Map<String, Object> data = new LinkedHashMap<>();
        //别人审我
        //上报材料总数(本单位)
        int materialCount = indicatorReportService.getMaterial(0, unitId);
//        int materialCount = indicatorReportService.getMaterial(0, unitId);
        data.put("上报材料总数(本单位)", materialCount);
        //已审核材料总数(本单位)
        int completeMaterialCount = indicatorReportService.getReportSum(1, unitId);
        data.put("已审核指标总数(本单位)", completeMaterialCount);
        int unMaterialCount = indicatorReportService.getReportSum(0, unitId);
        //未审核数(本单位)
        int unnatural = unMaterialCount - completeMaterialCount;
        data.put("未审核指标数(本单位)", unnatural);
        //审核失败(本单位)
        int errorNumber = indicatorReportService.getReportSum(2, unitId);
        data.put("被驳回指标数(本单位)", errorNumber);
        //我审人家


        int reportCount = 0;
        int unReportCount = 0;
        if (code.contains("ROLE_admin") || code.contains("ROLE_user_admin")) {
            //上报材料总数
            reportCount = indicatorReportService.getMaterial(null, unitId, 0);
            //未审核指标数
            int Count = indicatorReportService.getReportSum(null, unitId, 0);
            int userReportCount = indicatorReportService.getReportSum(null, unitId, 1);
            //评分
            int Count2 = indicatorReportService.getReportSum(null, unitId, 5);
            int userReportCount2 = indicatorReportService.getReportSum(null, unitId, 6);
            unReportCount = (Count - userReportCount) + (Count2 - userReportCount2);
        }
        data.put("上报材料总数", reportCount);

        data.put("未审核指标数", unReportCount);
        List<UnitManage> unitManages = unitManageService.getListByType(0, null);
        //各单位上报材料数
        Map<String, Integer> unitMaterialMap = new LinkedHashMap<>();
        for (UnitManage unitManage : unitManages) {
            int unitCount = indicatorReportService.getMaterial(unitManage.getUnitId(), unitId, 0);
            unitMaterialMap.put(unitManage.getUnitName(), unitCount);
        }
        data.put("unitMaterialMap", unitMaterialMap);
        data.put("key", unitMaterialMap.keySet());
        data.put("value", unitMaterialMap.values());

        return RestResponse.ok(data);
    }


}
