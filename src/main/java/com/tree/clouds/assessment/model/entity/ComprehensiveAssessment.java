package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 综合评定表
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comprehensive_assessment")
@ApiModel(value = "ComprehensiveAssessment对象", description = "综合评定表")
public class ComprehensiveAssessment extends BaseEntity {

    public static final String COMPREHENSIVE_ID = "comprehensive_id";
    public static final String ASSESSMENT_YEAR = "assessment_year";
    public static final String TASK_SCORE = "task_score";
    public static final String INNOVATION_SCORE_SUM = "innovation_score_sum";
    public static final String POSITIVE_SCORE = "positive_score";
    public static final String PERFORMANCE_SCORE = "performance_score";
    public static final String USER_SCORE = "user_score";
    public static final String EXPERT_SCORE = "expert_score";
    public static final String INNOVATION_SCORE = "innovation_score";
    public static final String SCORE_SUM = "score_sum";
    public static final String EVEL_NAME = "evel_name";
    public static final String COMPREHENSIVE_STATUS = "comprehensive_status";
    public static final String REMARK = "remark";
    public static final String UNIT_ID = "unit_Id";

    @ApiModelProperty(value = "综合评定表主键")
    @TableId(value = "comprehensive_id", type = IdType.UUID)
    private String comprehensiveId;

    @ApiModelProperty(value = "考评年份")
    @TableField("assessment_year")
    private String assessmentYear;

    @ApiModelProperty(value = "责任单位")
    @TableField("unit_Id")
    private String unitId;

    @ApiModelProperty(value = "指标主键")
    @TableField("indicators_id")
    private String indicatorsId;

    @ApiModelProperty(value = "绩效任务总分值")
    @TableField("task_score")
    private String taskScore;

    @ApiModelProperty(value = "机制创新总分值")
    @TableField("innovation_score_sum")
    private String innovationScoreSum;

    @ApiModelProperty(value = "正向激励总分值")
    @TableField("positive_score")
    private String positiveScore;

    @ApiModelProperty(value = "绩效减分总分值")
    @TableField("performance_score")
    private String performanceScore;

    @ApiModelProperty(value = "自评总分")
    @TableField("user_score")
    private String userScore;

    @ApiModelProperty(value = "专家总评分")
    @TableField("expert_Rating")
    private String expertRating;

    @ApiModelProperty(value = "机制创新分")
    @TableField("innovation_score")
    private String innovationScore;

    @ApiModelProperty(value = "综合得分")
    @TableField("score_sum")
    private String scoreSum;

    @ApiModelProperty(value = "评价等级")
    @TableField("evel_Id")
    private String evelId;

    @ApiModelProperty(value = "综合评分状态 0待评定 1通过 2驳回")
    @TableField("comprehensive_status")
    private Integer comprehensiveStatus;

    @ApiModelProperty(value = "综合评分状态 0待评定 1已评 2复核")
    @TableField("comprehensive_progress")
    private Integer comprehensiveProgress;

    @ApiModelProperty(value = "单位名称")
    @TableField(exist = false)
    private String unitName;

    @ApiModelProperty(value = "评价等次")
    @TableField(exist = false)
    private String evelName;





}
