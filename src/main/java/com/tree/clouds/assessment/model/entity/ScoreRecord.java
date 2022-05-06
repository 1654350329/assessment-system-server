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
 * 评分记录表
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("score_record")
@ApiModel(value="ScoreRecord对象", description="评分记录表")
public class ScoreRecord extends BaseEntity {


    @ApiModelProperty(value = "记录主键")
    @TableField("record_id")
    private String recordId;

    @ApiModelProperty(value = "指标主键")
    @TableField("indicators_id")
    private String indicatorsId;

    @ApiModelProperty(value = "专家评分")
    @TableField("expert_score")
    private String expertScore;

    @ApiModelProperty(value = "得分说明")
    @TableField("illustrate")
    private String illustrate;

    @ApiModelProperty(value = "单位名称")
    @TableField("unit_name")
    private String unitName;




    public static final String RECORD_ID = "record_id";

    public static final String INDICATORS_ID = "indicators_id";

    public static final String EXPERT_SCORE = "expert_score";

    public static final String ILLUSTRATE = "illustrate";

    public static final String UNIT_NAME = "unit_name";



}
