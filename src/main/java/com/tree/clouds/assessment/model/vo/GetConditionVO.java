package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetConditionVO {
    @ApiModelProperty("责任单位名称")
    private String unitName;

    @ApiModelProperty("考评年份")
    private String assessmentYear;
}
