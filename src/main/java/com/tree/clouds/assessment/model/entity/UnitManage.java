package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分组管理
 * </p>
 *
 * @author LZK
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("unit_manage")
@ApiModel(value = "UnitManage对象", description = "单位管理")
public class UnitManage extends BaseEntity {

    public static final String UNIT_ID = "UNIT_ID";
    public static final String UNIT_NAME = "UNIT_NAME";
    public static final String UNIT_TYPE = "unit_type";
    //单位类型 0下属单位 1区县
    public static final Integer UNIT_TYPE_ZERO = 0;
    public static final Integer UNIT_TYPE_ONE = 1;
    @ApiModelProperty(value = "单位id")
    @TableId(value = UNIT_ID, type = IdType.UUID)
    private String unitId;

    @ApiModelProperty(value = "单位名称")
    @TableField(UNIT_NAME)
    private String unitName;

    @ApiModelProperty(value = "所属区县")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty(value = "单位类型")
    @TableField(UNIT_TYPE)
    private Integer unitType;


}
