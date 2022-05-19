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
        String unitId = null;
        String userId = null;
        if (!code.contains("ROLE_admin")) {
            unitId = LoginUserUtil.getUnitId();
        }
        if (!code.contains("ROLE_EXPERT")) {
            userId = LoginUserUtil.getUserId();
        }
        Map<String, Object> data = new LinkedHashMap<>();
        //材料总数
        int materialCount = indicatorReportService.getMaterial(0, unitId,userId);
        data.put("审核材料总数", materialCount);
        //已审核材料总数
        int completeMaterialCount = indicatorReportService.getMaterial(1, unitId, userId);
        data.put("已审核材料总数", materialCount);
        //未审核数
        int unnatural = materialCount - completeMaterialCount;
        data.put("未审核材料总数", unnatural);
        //审核失败
        int errorNumber = indicatorReportService.getMaterial(2, unitId, userId);
        data.put("材料驳回总数", errorNumber);
        List<UnitManage> unitManages = unitManageService.list();
        //责任单位数
        int unitNumber = unitManages.size();
        data.put("考核责任单位数", unitNumber);
        //责任人数
        int userNumber = userManageService.getListByRole("2").size();
        data.put("审核责任人数", userNumber);
        //各单位上报材料数
        Map<String, Integer> unitMaterialMap = new LinkedHashMap<>();
        for (UnitManage unitManage : unitManages) {
            int unitCount = indicatorReportService.getMaterial(0, unitManage.getUnitId(), userId);
            unitMaterialMap.put(unitManage.getUnitName(), unitCount);
        }
        data.put("unitMaterialMap", unitMaterialMap);
        data.put("key", unitMaterialMap.keySet());
        data.put("value", unitMaterialMap.values());

        return RestResponse.ok(data);
    }


}
