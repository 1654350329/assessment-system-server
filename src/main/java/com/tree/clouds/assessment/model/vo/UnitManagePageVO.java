package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnitManagePageVO extends PageParam {

    @ApiModelProperty(value = "单位名称")
    private String unitName;
}
