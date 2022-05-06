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
@ApiModel(value="ComprehensiveAssessment对象", description="综合评定表")
public class ComprehensiveAssessment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "综合评定表主键")
      @TableId(value = "comprehensive_id", type = IdType.UUID)
    private String comprehensiveId;

    @ApiModelProperty(value = "考评年份")
    @TableField("comprehensive_year")
    private String comprehensiveYear;

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

    @ApiModelProperty(value = "专家评分")
    @TableField("expert_score")
    private String expertScore;

    @ApiModelProperty(value = "机制创新分")
    @TableField("innovation_score")
    private String innovationScore;

    @ApiModelProperty(value = "综合得分")
    @TableField("score_sum")
    private String scoreSum;

    @ApiModelProperty(value = "评价等级")
    @TableField("evel_name")
    private String evelName;

    @ApiModelProperty(value = "综合评分状态")
    @TableField("comprehensive_status")
    private String comprehensiveStatus;

    @ApiModelProperty(value = "专家复核说明")
    @TableField("remark")
    private String remark;




    public static final String COMPREHENSIVE_ID = "comprehensive_id";

    public static final String COMPREHENSIVE_YEAR = "comprehensive_year";

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



}
