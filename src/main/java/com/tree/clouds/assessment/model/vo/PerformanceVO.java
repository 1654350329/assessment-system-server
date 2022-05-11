package com.tree.clouds.assessment.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private String taskScore;
    @ApiModelProperty(value = "机制创新总分值")
    private String innovationScoreSum;
    @ApiModelProperty(value = "正向激励加分总分值")
    private String positiveIncentiveSum;
    @ApiModelProperty(value = "绩效减分总分值")
    private String performanceScore;
    @ApiModelProperty(value = "自评总分")
    private String userScore;
    @ApiModelProperty(value = "专家评分（线上）")
    private String expertRating;
    @ApiModelProperty(value = "机制创新分")
    private String innovationScore;
    @ApiModelProperty(value = "综合得分")
    private String scoreSum;
    @ApiModelProperty(value = "评价等级")
    private String evelName;
    @ApiModelProperty(value = "综合评分状态 0待评定 1通过 2驳回")
    private Integer comprehensiveStatus;

    @ApiModelProperty(value = "综合评分进度 0待评定 1已评 2复核")
    private Integer comprehensiveProgress;

}
