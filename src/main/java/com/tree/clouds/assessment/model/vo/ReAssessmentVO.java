package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReAssessmentVO {
    @ApiModelProperty(value = "考核主键")
    private String id;

    @ApiModelProperty(value = "复核说明")
    private String remark;
}
