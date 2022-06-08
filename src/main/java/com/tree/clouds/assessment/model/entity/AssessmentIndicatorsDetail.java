package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.tree.clouds.assessment.model.vo.FileInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 考核指标详细表
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("assessment_indicators_detail")
@ApiModel(value = "AssessmentIndicatorsDetail对象", description = "考核指标详细表")
public class AssessmentIndicatorsDetail extends BaseEntity {

    public static final String DETAIL_ID = "detail_id";
    public static final String PROJECT_ID = "project_id";
    public static final String TASK_ID = "task_id";
    public static final String ASSESSMENT_ID = "assessment_id";
    public static final String ASSESSMENT_CRITERIA = "assessment_criteria";
    public static final String INSTRUCTIONS = "instructions";
    public static final String FRACTION = "fraction";

    @ApiModelProperty(value = "考核指标Id")
    @TableId(value = "detail_id", type = IdType.UUID)
    private String detailId;
    @ApiModelProperty(value = "指标方案主键")
    @TableField("indicators_id")
    private String indicatorsId;
    @ApiModelProperty(value = "项目主键")
    @TableField("project_id")
    private String projectId;

    @ApiModelProperty(value = "指标任务主键")
    @TableField("task_id")
    private String taskId;

    @ApiModelProperty(value = "考评标准主键")
    @TableField("assessment_id")
    private String assessmentId;

    @ApiModelProperty(value = "考核标准")
    @TableField("assessment_criteria")
    private String assessmentCriteria;

    @ApiModelProperty(value = "申报填报说明")
    @TableField("instructions")
    private String instructions;

    @ApiModelProperty(value = "分数")
    @TableField("fraction")
    private Double fraction;

    @ApiModelProperty(value = "指标类型 0下属单位 1区县")
    @TableField("indicators_type")
    private Integer indicatorsType;

    @ApiModelProperty(value = "附件")
    @TableField(exist = false)
    private List<FileInfo> fileInfoVOS;


}
