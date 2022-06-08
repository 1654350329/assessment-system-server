package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 待办l列表
 * </p>
 *
 * @author LZK
 * @since 2022-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("matter_list")
@ApiModel(value = "MatterList对象", description = "待办l列表")
public class MatterList extends BaseEntity {

    public static final String MATTER_ID = "matter_id";
    public static final String TITLE = "title";
    public static final String MATTER_TYPE = "matter_type";
    public static final String UNIT_ID = "unit_id";
    public static final String REPORT_ID = "report_id";
    public static final String MATTER_STATUS = "matter_status";

    @ApiModelProperty(value = "事项id")
    @TableId(value = "matter_id", type = IdType.UUID)
    private String matterId;

    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "事项类型(0发布考核,1材料审核,2上报驳回,3专家待评,4专家重评)")
    @TableField("matter_type")
    private Integer matterType;

    @ApiModelProperty(value = "单位id")
    @TableField("unit_id")
    private String unitId;

    @ApiModelProperty(value = "上报id")
    @TableField("report_id")
    private String reportId;

    @ApiModelProperty(value = "主评单位id")
    @TableField("expert_Unit_Id")
    private String expertUnitId;

    @ApiModelProperty(value = "0未读 1已读")
    @TableField("matter_status")
    private Integer matterStatus;


    @ApiModelProperty(value = "年份")
    @TableField("year")
    private String year;

    @ApiModelProperty(value = "指标主键")
    @TableField("indicators_Id")
    private String indicatorsId;


}
