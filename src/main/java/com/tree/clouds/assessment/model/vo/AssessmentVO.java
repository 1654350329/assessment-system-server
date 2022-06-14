package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentVO {

    @ApiModelProperty(value = "状态")
    private Integer indicatorsStatus;
    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;
    @ApiModelProperty(value = "责任单位数量")
    private Integer unitNumber;
    @ApiModelProperty(value = "责任单位类型")
    private Integer indicatorsType;
    @ApiModelProperty(value = "发布日期")
    private String releaseDate;

    @ApiModelProperty(value = "截止日期")
    private String expirationDate;

    @ApiModelProperty(value = "责任人")
    private String createdUser;

}
