package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReviewPageVO extends PageParam{
    @ApiModelProperty(value = "考核内容")
    private Integer assessmentCriteria;

    @ApiModelProperty(value = "责任单位")
    private String unitName;

    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;

    @ApiModelProperty(value = "复评状态 0待复评 1已复评")
    private Integer reviewStatus;
}
