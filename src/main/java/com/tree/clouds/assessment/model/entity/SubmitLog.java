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
@ApiModel(value="SubmitLog对象", description="报送日志")
public class SubmitLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报送日志主键")
      @TableId(value = "submit_id", type = IdType.UUID)
    private String submitId;

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

    @ApiModelProperty(value = "创建人")
    @TableField("CREATED_USER")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
      @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private String createdTime;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATED_USER")
    private String updatedUser;

    @ApiModelProperty(value = "更新时间")
      @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private String updatedTime;

    @ApiModelProperty(value = "删除")
    @TableField("DEL")
    private Integer del;


    public static final String SUBMIT_ID = "submit_id";

    public static final String INDICATORS_NAME = "indicators_Name";

    public static final String ASSESSMENT_CRITERIA = "assessment_Criteria";

    public static final String EVALUATION_METHOD = "evaluation_method";

    public static final String INDICATORS_STATUS = "indicators_Status";

    public static final String REMARK = "remark";

    public static final String EXPIRATION_DATE = "expiration_Date";

    public static final String CREATED_USER = "CREATED_USER";

    public static final String CREATED_TIME = "CREATED_TIME";

    public static final String UPDATED_USER = "UPDATED_USER";

    public static final String UPDATED_TIME = "UPDATED_TIME";

    public static final String DEL = "DEL";

}
