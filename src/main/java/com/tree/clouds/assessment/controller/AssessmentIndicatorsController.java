package com.tree.clouds.assessment.controller;


import cn.hutool.core.bean.BeanUtil;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.tree.clouds.assessment.model.entity.AssessmentIndicatorsDetail;
import com.tree.clouds.assessment.model.entity.FileInfo;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.AssessmentIndicatorsDetailService;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import com.tree.clouds.assessment.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 考核指标配置 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/indicators")
@Api(value = "indicators", tags = "考核指标配置模块")
public class AssessmentIndicatorsController {
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;
    @Autowired
    private AssessmentIndicatorsDetailService assessmentIndicatorsDetailService;
    @Autowired
    private FileInfoService fileInfoService;


    @Log("考核指标配置目录树")
    @PostMapping("/indicatorsTree/{year}")
    @ApiOperation(value = "考核指标配置目录树")
    public RestResponse<List<IndicatorsTreeTreeVO>> indicatorsTree(@PathVariable Integer year) {
        List<IndicatorsTreeTreeVO> tree = assessmentIndicatorsService.indicatorsTree(year, 1);
        return RestResponse.ok(tree);
    }

    @Log("考核指标配置子集目录树")
    @PostMapping("/indicatorsChildrenTree/{id}")
    @ApiOperation(value = "考核指标配置子集目录树")
    public RestResponse<List<IndicatorsTreeTreeVO>> indicatorsChildrenTree(@PathVariable String id) {
        List<IndicatorsTreeTreeVO> tree = assessmentIndicatorsService.indicatorsChildrenTree(id);
        return RestResponse.ok(tree);
    }

    @Log("根据考核标准查询目录树")
    @PostMapping("/getTreeById/{id}")
    @ApiOperation(value = "根据考核标准查询目录树")

    public RestResponse<List<IndicatorsTreeTreeVO>> getTreeById(@PathVariable String id) {
        List<IndicatorsTreeTreeVO> tree = assessmentIndicatorsService.getTreeById(id);
        return RestResponse.ok(tree);
    }


    @Log("查看考评标准")
    @PostMapping("/evaluationStandard/{id}")
    @ApiOperation(value = "查看考评标准")

    public RestResponse<AssessmentIndicators> evaluationStandard(@PathVariable String id) {
        AssessmentIndicators assessmentIndicators = assessmentIndicatorsService.evaluationStandard(id);
        return RestResponse.ok(assessmentIndicators);
    }

    @Log("新增项目")
    @PostMapping("/addIndicators")
    @ApiOperation(value = "新增项目")

    public RestResponse<Boolean> addIndicators(@RequestBody UpdateIndicatorsVO updateIndicatorsVO) {
        assessmentIndicatorsService.addIndicators(updateIndicatorsVO);
        return RestResponse.ok(true);
    }

    @Log("编辑项目")
    @PostMapping("/updateIndicators")
    @ApiOperation(value = "编辑项目")

    public RestResponse<Boolean> updateIndicators(@RequestBody UpdateIndicatorsVO updateIndicatorsVO) {
        assessmentIndicatorsService.updateIndicators(updateIndicatorsVO);
        return RestResponse.ok(true);
    }

    @Log("删除项目,指标")
    @PostMapping("/deleteIndicators")
    @ApiOperation(value = "删除项目,指标,考评标准")

    public RestResponse<Boolean> deleteIndicators(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        assessmentIndicatorsService.deleteIndicators(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @Log("删除考核标准")
    @PostMapping("/deleteAssessment")
    @ApiOperation(value = "删除考核标准")

    public RestResponse<Boolean> deleteAssessment(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        assessmentIndicatorsDetailService.removeByIds(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @Log("新增指标任务或考评标准")
    @PostMapping("/addTask")
    @ApiOperation(value = "新增指标任务或考评标准")
    public RestResponse<Boolean> addTask(@Validated @RequestBody UpdateTaskVO updateTaskVO) {
        assessmentIndicatorsService.addTask(updateTaskVO);
        return RestResponse.ok(true);
    }

    @Log("编辑指标任务或考评标准或考核标准")
    @PostMapping("/updateTask")
    @ApiOperation(value = "编辑指标任务或考评标准或考核标准")
    public RestResponse<Boolean> updateTask(@Validated @RequestBody UpdateTaskVO updateTaskVO) {
        assessmentIndicatorsService.updateTask(updateTaskVO);
        return RestResponse.ok(true);
    }

    @Log("查看考核标准")
    @PostMapping("/getAssessmentIndicators/{id}")
    @ApiOperation(value = "查看考核标准")

    public RestResponse<AssessmentIndicatorsDetail> getAssessmentIndicators(@PathVariable String id) {
        AssessmentIndicatorsDetail assessmentIndicators = assessmentIndicatorsDetailService.getById(id);
        List<FileInfo> fileInfos = fileInfoService.getByBizIdsAndType(assessmentIndicators.getDetailId(), null);
//        List<FileInfoVO> collect = fileInfos.stream().map(fileInfo -> BeanUtil.toBean(fileInfo, FileInfoVO.class)).collect(Collectors.toList());
        assessmentIndicators.setFileInfoVOS(fileInfos);
        return RestResponse.ok(assessmentIndicators);
    }

    @Log("复制项目")
    @PostMapping("/copyTask/{year}")
    @ApiOperation(value = "复制项目根据年份")
    public RestResponse<Boolean> copyTask(@PathVariable Integer year) {
        assessmentIndicatorsService.copyTask(year);
        return RestResponse.ok(true);
    }

    @Log("导出项目")
    @GetMapping("/export/{year}")
    @ApiOperation(value = "导出项目根据年份")
    public void export(@PathVariable Integer year, HttpServletResponse response) {
        assessmentIndicatorsService.export(year,response);
    }

    @Log("配置总分值 总分值")
    @PostMapping("/getScoreSum/{year}")
    @ApiOperation(value = "配置总分值")
    public RestResponse<Integer> getScoreSum(@PathVariable Integer year) {
        int score = assessmentIndicatorsService.getScoreSum(year);
        return RestResponse.ok(score);
    }
}

