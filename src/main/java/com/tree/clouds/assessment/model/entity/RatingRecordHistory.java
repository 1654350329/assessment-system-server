package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 评分历史记录
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("rating_record_history")
@ApiModel(value = "RatingRecordHistory对象", description = "评分历史记录")
public class RatingRecordHistory extends BaseEntity {
    public static final String HISTORY_ID = "history_id";
    public static final String DETAIL_ID = "detail_id";
    public static final String EXPERT_SCORE = "expert_score";
    public static final String ILLUSTRATE = "illustrate";

    @ApiModelProperty(value = "记录历史id")
    @TableId(value = "history_id", type = IdType.UUID)
    private String historyId;

    @ApiModelProperty(value = "考核主键")
    @TableField("detail_id")
    private String detailId;

    @ApiModelProperty(value = "专家评分")
    @TableField("expert_score")
    private Double expertScore;

    @ApiModelProperty(value = "得分说明")
    @TableField("illustrate")
    private String illustrate;


}
