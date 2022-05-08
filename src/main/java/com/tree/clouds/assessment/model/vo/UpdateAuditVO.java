package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateAuditVO {

    @ApiModelProperty(value = "考核指标id")
    private String indicatorsId;

    @ApiModelProperty(value = "单位主键")
    private String unitId;

    @ApiModelProperty(value = "审核状态")
    private Integer indicatorsStatus;

    @ApiModelProperty(value = "审核意见")
    private String remark;

    @ApiModelProperty(value = "修改截止日期")
    private String expirationDate;
}
