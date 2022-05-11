package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnitVO extends PageParam {
    @ApiModelProperty("单位主键")
    private String unitId;
    @ApiModelProperty("单位名称")
    private String unitName;
    @ApiModelProperty("分配数")
    private Integer number;
    @ApiModelProperty("年份")
    private Integer year;
}
