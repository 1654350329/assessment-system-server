package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditTreeVO {
    @ApiModelProperty(value = "内容指标")
    private String indicatorsName;
    @ApiModelProperty(value = "审核状态")
    private String reportStatus;
    @ApiModelProperty(value = "单位主键")
    private String unitId;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "reportId 复评 查看具体复评")
    private String reportId;
    @ApiModelProperty(value = "内容")
    private String content;
}
