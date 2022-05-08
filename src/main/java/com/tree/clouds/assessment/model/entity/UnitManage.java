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
    @ApiModelProperty(value = "分组id")
    @TableId(value = UNIT_ID, type = IdType.UUID)
    private String unitId;

    @ApiModelProperty(value = "分组名称")
    @TableField(UNIT_NAME)
    private String unitName;


}
