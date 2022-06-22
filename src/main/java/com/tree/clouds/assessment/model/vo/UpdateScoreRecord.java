package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateScoreRecord {

    @ApiModelProperty(value = "考核主键")
    private String id;

    @ApiModelProperty(value = "专家评分")
    private Double expertScore;

    @ApiModelProperty(value = "得分说明")
    @Length(max = 200, message = "最大字符数200")
    private String illustrate;

    @ApiModelProperty(value = "单位主键")
    private String unitId;
}
