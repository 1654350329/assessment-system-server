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
 * 评价等级表
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("evaluation_evel")
@ApiModel(value = "EvaluationEvel对象", description = "评价等级表")
public class EvaluationEvel extends BaseEntity {
    public static final String EVEL_ID = "evel_id";
    public static final String EVEL_NAME = "evel_name";
    @ApiModelProperty(value = "等级id")
    @TableId(value = "evel_id", type = IdType.UUID)
    private String evelId;

    @ApiModelProperty(value = "等级名称")
    @TableField("evel_name")
    private String evelName;


}
