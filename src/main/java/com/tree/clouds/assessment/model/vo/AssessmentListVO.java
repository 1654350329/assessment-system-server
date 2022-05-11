package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentListVO {
    @ApiModelProperty(value = "上报主键")
    private String reportId;
    @ApiModelProperty(value = "考核主键")
    private String detailId;
    @ApiModelProperty(value = "报送时间")
    private String AssessmentDate;

    @ApiModelProperty(value = "考核年度")
    private String assessmentYear;

    @ApiModelProperty(value = "责任单位")
    private String unitName;
    @ApiModelProperty(value = "填报责任人")
    private String createdUser;
    @ApiModelProperty(value = "联系方式")
    private String phoneNumber;
    @ApiModelProperty(value = "考核项目")
    private String indicatorsName;
    @ApiModelProperty(value = "考核内容")
    private String assessmentCriteria;
    @ApiModelProperty(value = "审核状态 0未审核")
    private Integer indicatorsStatus;


}
