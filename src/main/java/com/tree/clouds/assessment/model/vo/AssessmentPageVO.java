package com.tree.clouds.assessment.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentPageVO extends PageParam {

    @ApiModelProperty(value = "指标名称")
    private String indicatorsName;

    @ApiModelProperty(value = "状态")
    private String indicatorsStatus;

    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;

    @ApiModelProperty("超期")
    private Boolean overdue;

    @ApiModelProperty("责任单位")
    private String unitName;

    @ApiModelProperty("责任单位id")
    @JsonIgnore
    private String unitId;

    @ApiModelProperty(value = "指标类型 0下属单位 1区县考核")
    private int indicatorsType;
}
