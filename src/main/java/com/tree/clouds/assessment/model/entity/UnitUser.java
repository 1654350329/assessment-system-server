package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分组与角色管理中间表
 * </p>
 *
 * @author LZK
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("unit_user")
@ApiModel(value = "UnitUser对象", description = "单位与用户管理中间表")
public class UnitUser extends BaseEntity {

    public static final String Unit_ID = "unit_id";
    public static final String USER_ID = "user_id";
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "分组id")
    @TableField(Unit_ID)
    private String unitId;

    @ApiModelProperty(value = "用户id")
    @TableField(USER_ID)
    private String userId;


}
