package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditLogVO {
    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;

    @ApiModelProperty(value = "发布日期")
    private String releaseDate;

    @ApiModelProperty(value = "截止日期")
    private String expirationDate;

    @ApiModelProperty(value = "超期")
    private Boolean overdue;

    @ApiModelProperty(value = "责任单位")
    private String unitName;

    @ApiModelProperty(value = "分配指标数")
    private Integer allocationNumber;
    @ApiModelProperty(value = "提交材料数")
    private Integer submitNumber;
    @ApiModelProperty(value = "通过审核数")
    private Integer passNumber;
    @ApiModelProperty(value = "被驳回数")
    private Integer rejectionsNumber;
    @ApiModelProperty(value = "待审核数")
    private Integer pendingNumber;
}
