package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EvaluationEvelVO extends PageParam{

    @ApiModelProperty(value = "等级名称")
    private String evelName;
}
