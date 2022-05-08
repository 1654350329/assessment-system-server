package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PerformanceVO {
    @ApiModelProperty(value = "综合评定表主键")
    private String comprehensiveId;
    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;
    @ApiModelProperty(value = "责任单位")
    private String unitName;
    @ApiModelProperty(value = "责任单位主键")
    private String unitId;
    @ApiModelProperty(value = "绩效任务总分值")
    private Double taskScore;
    @ApiModelProperty(value = "机制创新总分值")
    private Double innovationScoreSum;
    @ApiModelProperty(value = "正向激励加分总分值")
    private Double positiveIncentiveSum;
    @ApiModelProperty(value = "绩效减分总分值")
    private Double performanceScore;
    @ApiModelProperty(value = "自评总分")
    private Double userScore;
    @ApiModelProperty(value = "专家评分（线上）")
    private Double expertRating;
    @ApiModelProperty(value = "机制创新分")
    private Double innovationScore;
    @ApiModelProperty(value = "评价等级")
    private String evelName;

}
