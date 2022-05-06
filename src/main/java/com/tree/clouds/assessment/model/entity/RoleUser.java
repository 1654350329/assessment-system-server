package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色与用户管理中间表
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role_user")
@ApiModel(value="RoleUser对象", description="角色与用户管理中间表")
public class RoleUser extends BaseEntity {


    @ApiModelProperty(value = "角色id")
    @TableField("ROLE_ID")
    private String roleId;

    @ApiModelProperty(value = "用户id")
    @TableField("USER_ID")
    private String userId;

    public static final String ROLE_ID = "ROLE_ID";

    public static final String USER_ID = "USER_ID";



}
