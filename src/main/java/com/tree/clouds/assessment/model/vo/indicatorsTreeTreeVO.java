package com.tree.clouds.assessment.model.vo;

import com.tree.clouds.assessment.common.ITree;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class indicatorsTreeTreeVO implements ITree<indicatorsTreeTreeVO> {
    @ApiModelProperty(value = "指标主键")
    private String id;

    @ApiModelProperty(value = "parent_Id")
    private String parentId;

    @ApiModelProperty(value = "目录级别 0顶级目录1项目2指标任务3考评标准")
    private Integer assessmentType;

    @ApiModelProperty(value = "指标名称")
    private String title;

    @ApiModelProperty(value = "分数")
    private Double fraction;

    @ApiModelProperty(value = "类型")
    private String file;


    @ApiModelProperty(value = "子集")
    private  List<indicatorsTreeTreeVO> children;

}
