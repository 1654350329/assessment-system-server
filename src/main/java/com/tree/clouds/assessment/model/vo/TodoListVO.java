package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TodoListVO {
    @ApiModelProperty("待办名称")
    private String doName;
    @ApiModelProperty("待办类型 0 材料驳回修改通知 1 市绩效考评 2重评通知")
    private Integer type;
    @ApiModelProperty("时间")
    private String time;
}
