package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PerformancePageVO extends PageParam{
    @ApiModelProperty(value = "责任单位")
    private String unitName;
    @ApiModelProperty(value = "考评年度")
    private String assessmentYear;
    @ApiModelProperty(value = "评价等级")
    private String evelName;
    @ApiModelProperty(value = "状态(进度)")
    private String reportProgress;
}
