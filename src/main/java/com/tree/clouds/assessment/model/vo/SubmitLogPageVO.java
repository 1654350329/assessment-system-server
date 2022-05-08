package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubmitLogPageVO extends PageParam{
    @ApiModelProperty("报送开始时间")
    private String startTime;

    @ApiModelProperty("报送结束时间")
    private String endTime;
    @ApiModelProperty("状态 0待审核 1通过 2驳回")
    private Integer status;

}
