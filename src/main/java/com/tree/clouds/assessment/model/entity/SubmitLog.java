package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 报送日志
 * </p>
 *
 * @author LZK
 * @since 2022-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("submit_log")
@ApiModel(value = "SubmitLog对象", description = "报送日志")
public class SubmitLog extends BaseEntity {

    public static final String SUBMIT_ID = "submit_id";
    public static final String INDICATORS_NAME = "indicators_Name";
    public static final String ASSESSMENT_CRITERIA = "assessment_Criteria";
    public static final String EVALUATION_METHOD = "evaluation_method";
    public static final String INDICATORS_STATUS = "indicators_Status";
    public static final String REMARK = "remark";
    public static final String EXPIRATION_DATE = "expiration_Date";

    @ApiModelProperty(value = "报送日志主键")
    @TableId(value = "submit_id", type = IdType.UUID)
    private String submitId;

    @ApiModelProperty(value = "单位主键")
    @TableField(value = "unit_Id")
    private String unitId;

    @ApiModelProperty(value = "上报主键")
    @TableField(value = "report_id")
    private String reportId;

    @ApiModelProperty(value = "考评年份")
    @TableField("assessment_year")
    private String assessmentYear;

    @ApiModelProperty(value = "考核项目")
    @TableField("indicators_Name")
    private String indicatorsName;

    @ApiModelProperty(value = "考核标准")
    @TableField("assessment_Criteria")
    private String assessmentCriteria;

    @ApiModelProperty(value = "报送时间")
    @TableField("evaluation_method")
    private String evaluationMethod;

    @ApiModelProperty(value = "状态")
    @TableField("indicators_Status")
    private Integer indicatorsStatus;

    @ApiModelProperty(value = "审核意见")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "修改截止日期")
    @TableField("expiration_Date")
    private String expirationDate;







}
