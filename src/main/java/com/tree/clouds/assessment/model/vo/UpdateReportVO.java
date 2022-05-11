package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateReportVO {

    @ApiModelProperty(value = "上报数据id")
    private String reportId;

    @ApiModelProperty(value = "指标id")
    private String indicatorsId;

    @ApiModelProperty(value = "自评分")
    private String userScore;

    @ApiModelProperty(value = "得分说明")
    private String illustrate;

    @ApiModelProperty(value = "报送责任人")
    private String createdUser;

    @ApiModelProperty(value = "联系电话")
    private String phoneNumber;
    @ApiModelProperty(value = "报送材料")
    private List<FileInfoVO> fileInfoVOS;
}
