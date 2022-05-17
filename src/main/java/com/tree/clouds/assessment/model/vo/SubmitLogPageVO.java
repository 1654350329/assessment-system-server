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

    @ApiModelProperty("考评年份 必传")
    private String assessmentYear;

    @ApiModelProperty("单位主键 必传")
    private String unitId;

    @ApiModelProperty("上报id")
    private String reportId;

}
