package com.tree.clouds.assessment.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReviewVO {
    @ApiModelProperty(value = "上报id")
    private String reportId;
    @ApiModelProperty(value = "考评年份")
    private Integer assessmentYear;

    @ApiModelProperty(value = "责任单位")
    private String unitName;

    @ApiModelProperty(value = "责任人")
    private String createdUser;

    @ApiModelProperty(value = "联系方式")
    private String phoneNumber;

    @ApiModelProperty(value = "考核id")
    private String indicatorsId;

    @ApiModelProperty(value = "考核项目")
    private String indicatorsName;

    @ApiModelProperty(value = "考核标准")
    private String assessmentCriteria;

    @ApiModelProperty(value = "评分状态 0未评 1已评")
    private Integer expertStatus;
}
