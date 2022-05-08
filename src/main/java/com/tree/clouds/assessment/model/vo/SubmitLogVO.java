package com.tree.clouds.assessment.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubmitLogVO {

    @ApiModelProperty(value = "考核项目")
    private String indicatorsName;

    @ApiModelProperty(value = "考核标准")
    private String assessmentCriteria;

    @ApiModelProperty(value = "报送时间")
    private String evaluationMethod;

    @ApiModelProperty(value = "状态")
    private String indicatorsStatus;

    @ApiModelProperty(value = "审核意见")
    private String remark;

    @ApiModelProperty(value = "审核人")
    private String updatedUser;
    @ApiModelProperty(value = "审核时间")
    private String updatedTime;
    @ApiModelProperty(value = "修改截止日期")
    private String expirationDate;


}
