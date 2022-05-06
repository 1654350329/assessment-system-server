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
@ApiModel(value="IndicatorReport对象", description="数据上报")
public class IndicatorReport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "上报数据id")
      @TableId(value = "report_id", type = IdType.UUID)
    private String reportId;

    @ApiModelProperty(value = "单位id")
    @TableField("unit_id")
    private String unitId;

    @ApiModelProperty(value = "自评分")
    @TableField("user_score")
    private String userScore;

    @ApiModelProperty(value = "得分说明")
    @TableField("illustrate")
    private String illustrate;




    public static final String REPORT_ID = "report_id";

    public static final String UNIT_ID = "unit_id";

    public static final String USER_SCORE = "user_score";

    public static final String ILLUSTRATE = "illustrate";



}
