package com.tree.clouds.assessment.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.entity.EvaluationEvel;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.vo.EvaluationEvelVO;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.model.vo.UnitManagePageVO;
import com.tree.clouds.assessment.service.EvaluationEvelService;
import com.tree.clouds.assessment.service.UnitManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 评价等级表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/evaluation-evel")
@Api(tags = "评价等级模块")
public class EvaluationEvelController {


    @Autowired
    private EvaluationEvelService evaluationEvelService;

    @PostMapping("/evaluationEvelPage")
    @ApiOperation(value = "评价等级模块分页查询")
    @Log("评价等级分页查询")
    @PreAuthorize("hasRole('ROLE_admin')")
    public RestResponse<IPage<EvaluationEvel>> evaluationEvelPage(@RequestBody EvaluationEvelVO pageVO) {
        IPage<EvaluationEvel> page = evaluationEvelService.evaluationEvelPage(pageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addEval")
    @ApiOperation(value = "添加评价等级")
    @Log("添加评价等级")
    @PreAuthorize("hasRole('ROLE_admin')")
    public RestResponse<Boolean> addEvaluation(@RequestBody  EvaluationEvel evaluationEvel) {
        evaluationEvelService.save(evaluationEvel);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateEval")
    @ApiOperation(value = "修改评价等级")
    @Log("修改评价等级")
    @PreAuthorize("hasRole('ROLE_admin')")
    public RestResponse<Boolean> updateEvaluation(@RequestBody  EvaluationEvel evaluationEvel) {
        evaluationEvelService.updateById(evaluationEvel);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteEval")
    @ApiOperation(value = "刪除评价等级")
    @Log("刪除评价等级")
    @PreAuthorize("hasRole('ROLE_admin')")
    public RestResponse<Boolean> deleteEvaluation(@Validated @RequestBody PublicIdsReqVO publicIdReqVO) {
        evaluationEvelService.deleteEvaluation(publicIdReqVO.getIds());
        return RestResponse.ok(true);
    }


}

