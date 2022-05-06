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
 * 用户管理
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_manage")
@ApiModel(value = "UserManage对象", description = "用户管理")
public class UserManage extends BaseEntity {
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String UNIT = "UNIT";
    public static final String CATEGORY = "category";
    public static final String SEX = "SEX";
    public static final String JOB = "JOB";
    public static final String TITLE_GRADE = "TITLE_GRADE";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String PASSWORD = "PASSWORD";
    public static final String SALT = "salt";

    public static final String ACCOUNT_STATUS = "ACCOUNT_STATUS";

    public static final String REMARK = "REMARK";

    @ApiModelProperty(value = "主键")
    @TableId(value = "USER_ID", type = IdType.UUID)
    private String userId;

    @ApiModelProperty(value = "姓名")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "联系方式")
    @TableField("PHONE_NUMBER")
    private String phoneNumber;

    @ApiModelProperty(value = "工作单位")
    @TableField("UNIT")
    private String unit;

    @ApiModelProperty(value = "科别")
    @TableField("category")
    private String category;

    @ApiModelProperty(value = "性别")
    @TableField("SEX")
    private String sex;

    @ApiModelProperty(value = "职务")
    @TableField("JOB")
    private String job;

    @ApiModelProperty(value = "职称")
    @TableField("TITLE_GRADE")
    private String titleGrade;

    @ApiModelProperty(value = "账号")
    @TableField("ACCOUNT")
    private String account;

    @ApiModelProperty(value = "密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "盐")
    @TableField("salt")
    private String salt;

    @ApiModelProperty(value = "账号状态")
    @TableField("ACCOUNT_STATUS")
    private Integer accountStatus;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;


}
