package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("operation_log")
@ApiModel(value="OperationLog对象", description="操作日志")
public class OperationLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作id")
      @TableId(value = "operation_id", type = IdType.UUID)
    private String operationId;

    @ApiModelProperty(value = "ip地址")
    @TableField("ip")
    private String ip;

    @ApiModelProperty(value = "操作")
    @TableField("operation")
    private String operation;

    @TableField("CREATED_USER")
    private String createdUser;

      @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private String createdTime;

    @TableField("UPDATED_USER")
    private String updatedUser;

      @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private String updatedTime;

    @TableField("del")
    @TableLogic
    private Integer del;


    public static final String OPERATION_ID = "operation_id";

    public static final String IP = "ip";

    public static final String OPERATION = "operation";



}
