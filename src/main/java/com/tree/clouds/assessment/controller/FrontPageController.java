package com.tree.clouds.assessment.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.ScoreRecord;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.vo.TodoListVO;
import com.tree.clouds.assessment.service.IndicatorReportService;
import com.tree.clouds.assessment.service.ScoreRecordService;
import com.tree.clouds.assessment.service.UnitManageService;
import com.tree.clouds.assessment.service.UserManageService;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    private ScoreRecordService scoreRecordService;


    @Log("首页数据")
    @PostMapping("/getAllData")
    @ApiOperation(value = "首页数据")

    public RestResponse<Map<String, Object>> getAllData() {
        Map<String, Object> data = new LinkedHashMap<>();
        //材料总数
        int materialCount = indicatorReportService.getMaterial(0, null);
        data.put("审核材料总数", materialCount);
        //已审核材料总数
        int completeMaterialCount = indicatorReportService.getMaterial(1, null);
        data.put("已审核材料总数", materialCount);
        //未审核数
        int unnatural = materialCount - completeMaterialCount;
        data.put("未审核材料总数", unnatural);
        //审核失败
        int errorNumber = indicatorReportService.getMaterial(2, null);
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
            int unitCount = indicatorReportService.getMaterial(0, unitManage.getUnitId());
            unitMaterialMap.put(unitManage.getUnitName(), unitCount);
        }
        data.put("unitMaterialMap", unitMaterialMap);
        data.put("key", unitMaterialMap.keySet());
        data.put("value", unitMaterialMap.values());

        //待办列表
        List<RoleManage> roleManages = userManageService.getRoleById(LoginUserUtil.getUserId());
       // 单位账号
        for (RoleManage roleManage : roleManages) {
            if (roleManage.getRoleCode().equals("ROLE_up_user")) {
                List<IndicatorReport> list = indicatorReportService.list(new QueryWrapper<IndicatorReport>().eq(IndicatorReport.REPORT_STATUS, 2));
                if (CollUtil.isNotEmpty(list)){
                    TodoListVO todoListVO = new TodoListVO();
                    todoListVO.setDoName("材料驳回修改通知");
                    todoListVO.setType(0);
                    todoListVO.setTime(DateUtil.now());
                    data.put("账号待办",todoListVO);
                }
            }
            //专家
            if (roleManage.getRoleCode().equals("ROLE_EXPERT")) {
                List<ScoreRecord> list = scoreRecordService.list(new QueryWrapper<ScoreRecord>().eq(ScoreRecord.SCORE_TYPE, 0));
                if (CollUtil.isNotEmpty(list)){
                    TodoListVO todoListVO = new TodoListVO();
                    todoListVO.setDoName("重评通知");
                    todoListVO.setType(2);
                    todoListVO.setTime(DateUtil.now());
                    data.put("专家待办",todoListVO);
                }
            }
        }

        return RestResponse.ok(data);
    }

}
