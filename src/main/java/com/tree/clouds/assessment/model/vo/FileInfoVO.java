package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "文件管理", description = "文件管理")
public class FileInfoVO {

    @ApiModelProperty("文件存放路径")
    private String filePath;

    @ApiModelProperty("预览文件存放路径")
    private String previewPath;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("业务类型 不填")
    private String type;
}
