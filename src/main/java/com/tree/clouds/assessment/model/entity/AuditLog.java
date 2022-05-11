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
 * 审核日志
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("audit_log")
@ApiModel(value = "AuditLog对象", description = "审核日志")
public class AuditLog extends BaseEntity {
    public static final String AUDIT_ID = "audit_id";

    public static final String DETAIL_ID = "detail_Id";
    public static final String REPORT_ID = "report_Id";

    public static final String INDICATORS_STATUS = "indicators_status";

    public static final String REMARK = "remark";

    public static final String EXPIRATION_DATE = "expiration_date";

    @ApiModelProperty(value = "审核记录id")
    @TableId(value = "audit_id", type = IdType.UUID)
    private String auditId;

    @ApiModelProperty(value = "考核指标id")
    @TableField("detail_Id")
    private String detailId;

    @ApiModelProperty(value = "上报id")
    @TableField("report_Id")
    private String reportId;

    @ApiModelProperty(value = "审核状态 0未审核 1通过 2驳回")
    @TableField("indicators_status")
    private Integer indicatorsStatus;

    @ApiModelProperty(value = "审核意见")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "修改截止日期")
    @TableField("expiration_date")
    private String expirationDate;


}
