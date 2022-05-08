package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScorePageVO extends PageParam {
    @ApiModelProperty("责任单位名称")
    private String unitName;

    @ApiModelProperty("考评年份")
    private String assessmentYear;

    @ApiModelProperty("超期")
    private Boolean overdue;
}
