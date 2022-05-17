package com.tree.clouds.assessment.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 考核情况表
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("assessment_condition")
@ApiModel(value = "AssessmentCondition对象", description = "考核情况表")
public class AssessmentCondition extends BaseEntity {
    public static final String CONDITION_ID = "condition_id";
    public static final String ASSESSMENT_YEAR = "assessment_year";
    public static final String UNIT_NAME = "unit_name";
    public static final String INDICATORS_NAME = "indicators_name";
    public static final String ASSESSMENT_CRITERIA = "assessment_criteria";
    public static final String ILLUSTRATE = "illustrate";

    @ApiModelProperty(value = "情况主键")
    @TableId(value = "condition_id", type = IdType.UUID)
    @ExcelIgnore
    private String conditionId;

    @ApiModelProperty(value = "考评年份")
    @TableField("assessment_year")
    @ExcelIgnore
    private String assessmentYear;

    @ApiModelProperty(value = "责任单位")
    @TableField("unit_name")
    @ExcelIgnore
    private String unitName;

    @ApiModelProperty(value = "考核项目")
    @TableField("indicators_name")
    private String indicatorsName;

    @ApiModelProperty(value = "考核标准")
    @TableField("assessment_criteria")
    private String assessmentCriteria;

    @ApiModelProperty(value = "得分说明")
    @TableField("illustrate")
    private String illustrate;

}
