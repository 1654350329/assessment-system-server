package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentErrorVO {
    @ApiModelProperty(value = "报送主键")
    private String reportId;


    @ApiModelProperty(value = "报送时间")
    private String AssessmentDate;

    @ApiModelProperty(value = "截止时间")
    private String expirationDate;

    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;

    @ApiModelProperty(value = "责任单位")
    private String unitName;

    @ApiModelProperty(value = "填报责任人")
    private String createdUser;

    @ApiModelProperty(value = "联系方式")
    private String phoneNumber;

    @ApiModelProperty(value = "考核项目")
    private String indicatorsName;
    @ApiModelProperty(value = "考核项目")
    private String indicatorsId;

    @ApiModelProperty(value = "考核标准")
    private String assessmentCriteria;
    @ApiModelProperty(value = "状态")
    private Integer status;
}
