package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "PublicIdReqVO", description = "通用请求VO")
public class PublicIdReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", required = true)
    private String id;

    @ApiModelProperty(value = "单位主键")
    private String unitId;

    @ApiModelProperty(value = "内容指标关键字")
    private String content;

    @ApiModelProperty(value = "审核状态")
    private String reportStatus;

    @ApiModelProperty(value = "上报主键")
    private String reportId;
}