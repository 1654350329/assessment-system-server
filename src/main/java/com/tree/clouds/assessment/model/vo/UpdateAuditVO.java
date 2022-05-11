package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAuditVO {

    @ApiModelProperty(value = "上报主键")
    private String id;

    @ApiModelProperty(value = "单位主键")
    private String unitId;

    @NotNull(message = "审核状态不许为空")
    @ApiModelProperty(value = "审核状态 0未审核 1 已审核 2驳回")
    private Integer indicatorsStatus;

    @ApiModelProperty(value = "审核意见")
    private String remark;

    @ApiModelProperty(value = "修改截止日期")
    private String expirationDate;
}
