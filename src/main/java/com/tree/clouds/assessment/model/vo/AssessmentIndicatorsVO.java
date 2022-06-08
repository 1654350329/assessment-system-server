package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AssessmentIndicatorsVO {

    @ApiModelProperty(value = "指标主键")
    private String indicatorsId;

    @ApiModelProperty(value = "parentId")
    private String parentId;

    @ApiModelProperty(value = "指标名称")
    private String indicatorsName;

    @ApiModelProperty(value = "指标类型 0下属单位 1区县")
    private Integer indicatorsType;

    @ApiModelProperty(value = "考评方式 1线上 2线下")
    private Integer evaluationMethod;

    @ApiModelProperty(value = "牵头单位id")
    private String unitId;

    @ApiModelProperty(value = "牵头单位id")
    private List<String> unitIds;

    @ApiModelProperty(value = "目录级别 0顶级目录1项目2指标任务3考评标准")
    private Integer assessmentType;


    @ApiModelProperty(value = "状态")
    private Integer indicatorsStatus;

    @ApiModelProperty(value = "发布日期")
    private String releaseDate;
    @ApiModelProperty(value = "发布人")
    private String releaseUser;

    @ApiModelProperty(value = "截止日期")
    private String expirationDate;

    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;

    @ApiModelProperty(value = "附件")
    private List<FileInfoVO> fileInfoVOS;
}
