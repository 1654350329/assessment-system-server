package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentCompleteVO {
    @ApiModelProperty("责任单位名称")
    private String unitId;

    @ApiModelProperty("考评年份")
    private String assessmentYear;

}
