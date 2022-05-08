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
 * 指标单位中间表
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("unit_assessment")
@ApiModel(value = "UnitAssessment对象", description = "指标单位中间表")
public class UnitAssessment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "指标详细与单位中间表id")
    @TableId(value = "assessment_id", type = IdType.UUID)
    private String assessmentId;

    @ApiModelProperty(value = "指标id")
    @TableField("detail_id")
    private String detailId;

    @ApiModelProperty(value = "单位id")
    @TableField("unit_id")
    private String unitId;


    public static final String ASSESSMENT_ID = "assessment_id";

    public static final String INDICATORS_ID = "indicators_id";

    public static final String UNIT_ID = "unit_id";


}
