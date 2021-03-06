package com.tree.clouds.assessment.model.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;

import com.tree.clouds.assessment.model.vo.FileInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考核指标配置
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("assessment_indicators")
@ApiModel(value = "AssessmentIndicators对象", description = "考核指标配置")
public class AssessmentIndicators extends BaseEntity {

    public static final String INDICATORS_ID = "indicators_id";
    public static final String PARENT_ID = "parent_Id";
    public static final String INDICATORS_NAME = "indicators_name";
    public static final String EVALUATION_METHOD = "evaluation_method";
    public static final String UNIT_ID = "unit_id";
    public static final String UNIT_IDS = "unit_ids";
    public static final String ASSESSMENT_CRITERIA = "assessment_criteria";
    public static final String INSTRUCTIONS = "instructions";
    public static final String FRACTION = "fraction";
    public static final String INDICATORS_STATUS = "indicators_status";
    public static final String RELEASE_DATE = "release_date";
    public static final String EXPIRATION_DATE = "expiration_date";
    public static final String ASSESSMENT_YEAR = "assessment_year";
    public static final String INDICATORS_TYPE = "indicators_type";

    @ApiModelProperty(value = "指标主键")
    @TableId(value = "indicators_id", type = IdType.UUID)
    private String indicatorsId;

    @ApiModelProperty(value = "parentId")
    @TableField("parent_Id")
    private String parentId;

    @ApiModelProperty(value = "指标名称")
    @TableField("indicators_name")
    private String indicatorsName;

    @ApiModelProperty(value = "指标类型 0下属单位 1区县")
    @TableField("indicators_type")
    private Integer indicatorsType;

    @ApiModelProperty(value = "考评方式 1线上 2线下")
    @TableField("evaluation_method")
    private Integer evaluationMethod;

    @ApiModelProperty(value = "牵头单位id")
    @TableField("unit_id")
    private String unitId;

    @ApiModelProperty(value = "牵头单位id")
    @TableField("unit_ids")
    private String unitIds;

    @ApiModelProperty(value = "目录级别 0顶级目录1项目2指标任务3考评标准")
    @TableField("assessment_type")
    private Integer assessmentType;


    @ApiModelProperty(value = "状态")
    @TableField("indicators_status")
    private Integer indicatorsStatus;

    @ApiModelProperty(value = "发布日期")
    @TableField("release_date")
    private String releaseDate;
    @ApiModelProperty(value = "发布人")
    @TableField("release_user")
    private String releaseUser;

    @ApiModelProperty(value = "截止日期")
    @TableField("expiration_date")
    private String expirationDate;

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "考评年份")
    @TableField("assessment_year")
    private String assessmentYear;

    @ApiModelProperty(value = "附件")
    @TableField(exist = false)
    private List<FileInfoVO> fileInfoVOS;





}
