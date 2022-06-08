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

    @ApiModelProperty(value = "考评方式 0、线上 1、线下")
    private Integer evaluationMethod;

    @ApiModelProperty(value = "指标类型 0下属单位 1区县")
    private Integer indicatorsType;


}
