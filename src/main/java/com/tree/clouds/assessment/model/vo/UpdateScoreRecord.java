package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateScoreRecord {

    @ApiModelProperty(value = "考核主键")
    private String id;

    @ApiModelProperty(value = "专家评分")
    private Double expertScore;

    @ApiModelProperty(value = "得分说明")
    private String illustrate;

    @ApiModelProperty(value = "单位主键")
    private String unitId;
}
