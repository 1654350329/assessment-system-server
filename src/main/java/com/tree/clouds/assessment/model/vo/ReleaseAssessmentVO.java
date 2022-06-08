package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReleaseAssessmentVO {
    @ApiModelProperty(value = "年份")
    private String assessmentYear;

    @ApiModelProperty(value = "填报截止日期")
    private String expirationDate;

    @ApiModelProperty(value = "指标类型 0下属单位 1区县考核")
    private Integer indicatorsType;
}
