package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReEvaluationVO {

    @ApiModelProperty(value = "综合评定表主键")
    private String comprehensiveId;

    @ApiModelProperty(value = "综合评分状态 0待评定 1通过 2驳回")
    private Integer comprehensiveStatus;
}
