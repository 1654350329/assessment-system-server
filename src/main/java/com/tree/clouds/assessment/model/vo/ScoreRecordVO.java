package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScoreRecordVO {

    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;

    @ApiModelProperty(value = "发布日期")
    private String releaseDate;

    @ApiModelProperty(value = "截止日期")
    private String expirationDate;

    @ApiModelProperty(value = "分配指标数")
    private Integer unitNumber;

    @ApiModelProperty(value = "分配指标数")
    private Integer allocationNumber;
    @ApiModelProperty(value = "提交材料数")
    private Integer submitNumber;
    @ApiModelProperty(value = "已评")
    private Integer ReviewedNumber;
    @ApiModelProperty(value = "未评")
    private Integer unReviewedNumber;

}
