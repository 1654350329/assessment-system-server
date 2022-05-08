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
 * 数据上报
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("indicator_report")
@ApiModel(value = "IndicatorReport对象", description = "数据上报")
public class IndicatorReport extends BaseEntity {
    public static final String REPORT_ID = "report_id";
    public static final String INDICATORS_ID = "indicators_id";
    public static final String DETAIL_ID = "detail_id";
    public static final String UNIT_ID = "unit_id";
    public static final String USER_SCORE = "user_score";
    public static final String ILLUSTRATE = "illustrate";
    public static final String REPORT_PROGRESS = "report_Progress";

    @ApiModelProperty(value = "上报数据id")
    @TableId(value = "report_id", type = IdType.UUID)
    private String reportId;

    @ApiModelProperty(value = "指标主键")
    @TableField(value = "indicators_id")
    private String indicatorsId;

    @ApiModelProperty(value = "考核主键")
    @TableField("detail_id")
    private String detailId;

    @ApiModelProperty(value = "单位id")
    @TableField("unit_id")
    private String unitId;

    @ApiModelProperty(value = "自评分")
    @TableField("user_score")
    private String userScore;

    @ApiModelProperty(value = "得分说明")
    @TableField("illustrate")
    private String illustrate;


    @ApiModelProperty(value = "上报状态 0待审核 1通过 2驳回")
    @TableField("report_status")
    private Integer reportStatus;

    @ApiModelProperty(value = "上报状态进度 0 待上报 1已申报 2已评 3已复评 4已综合评定 5已结果复核 ")
    @TableField("report_progress")
    private Integer reportProgress;

    @ApiModelProperty(value = "报送材料 逗号分割")
    @TableField("file_id")
    private String fileId;


}
