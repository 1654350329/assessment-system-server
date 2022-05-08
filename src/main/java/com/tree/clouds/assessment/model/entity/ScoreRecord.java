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
    public static final String RECORD_ID = "record_id";
    public static final String DETAIL_ID = "detail_Id";
    public static final String EXPERT_SCORE = "expert_score";
    public static final String ILLUSTRATE = "illustrate";
    public static final String UNIT_ID = "unit_id";
    public static final String SCORE_TYPE = "score_type";


    @ApiModelProperty(value = "记录主键")
    @TableField("record_id")
    private String recordId;

    @ApiModelProperty(value = "考核主键")
    @TableField("detail_Id")
    private String detailId;

    @ApiModelProperty(value = "评分类型 0初评 1复评")
    @TableField("score_type")
    private Integer scoreType;

    @ApiModelProperty(value = "专家评分")
    @TableField("expert_score")
    private Double expertScore;

    @ApiModelProperty(value = "得分说明")
    @TableField("illustrate")
    private String illustrate;

    @ApiModelProperty(value = "单位主键")
    @TableField("unit_Id")
    private String unitId;







}
