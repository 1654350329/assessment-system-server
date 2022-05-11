package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetDataVO {
    @ApiModelProperty(value = "年份")
    private String year;
}
