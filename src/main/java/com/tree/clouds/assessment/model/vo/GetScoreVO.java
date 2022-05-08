package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetScoreVO {

    @ApiModelProperty(value = "考核主键")
    private String id;

    @ApiModelProperty(value = "单位主键")
    private String unitId;

    @ApiModelProperty(value = "分数类型 0初评 1复评")
    private Integer scoreType;
}
