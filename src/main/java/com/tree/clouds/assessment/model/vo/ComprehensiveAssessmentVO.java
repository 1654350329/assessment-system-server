package com.tree.clouds.assessment.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComprehensiveAssessmentVO {
    @ApiModelProperty(value = "综合评定表主键")
    private String comprehensiveId;

    @ApiModelProperty(value = "机制创新分")
    private Double innovationScore;

    @ApiModelProperty(value = "综合得分")
    private Double scoreSum;
    @ApiModelProperty(value = "线下得分")
    private Double offlineScore;


    @ApiModelProperty(value = "评价等级")
    @TableField("evel_Id")
    private String evelId;

}
