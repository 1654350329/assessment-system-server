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

    @ApiModelProperty("业务类型（1工伤决定书 2病例复印件 3鉴定文件 4审签意见 5劳动能力鉴定附件 6结论书,7通知书）")
    private String type;
}
