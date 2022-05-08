package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateIndicatorsVO {

    @ApiModelProperty(value = "指标主键 新增不填 更新填")
    private String indicatorsId;

    @ApiModelProperty(value = "父级pid")
    private String parentId;

    @ApiModelProperty(value = "指标名称")
    private String indicatorsName;

    @ApiModelProperty(value = "考评方式 1、线上自评 2、线下他评 33、线下选评")
    private String evaluationMethod;

    @ApiModelProperty(value = "专家id")
    private String userId;

}
