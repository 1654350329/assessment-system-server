package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateTaskVO {

    @ApiModelProperty(value = "指标主键")
    private String indicatorsId;

    @ApiModelProperty(value = "parentId")
    private String parentId;

    @ApiModelProperty(value = "名称")
    @Length(max = 200, message = "最多输入200字符")
    private String indicatorsName;

    @ApiModelProperty(value = "目录级别 0顶级目录1项目2指标任务3考评标准4考核标准")
    @NotNull(message = "目录级别不许为空")
    private Integer assessmentType;

    @ApiModelProperty(value = "申报填报说明 考核标准新增才需填写")
    @Length(max = 300, message = "最多输入200字符")
    private String instructions;

    @ApiModelProperty(value = "分数 考核标准新增才需填写")
    private Double fraction;

    @ApiModelProperty(value = "附件 0考核指标配置文件 ")
    private List<FileInfoVO> fileInfoVOS;

    @ApiModelProperty(value = "考核牵头单位")
    private String unitId;
    @ApiModelProperty(value = "协助考核部门单位")
    private List<String> unitIds;

    @ApiModelProperty(value = "指标类型 0下属单位 1区县考核")
    private Integer indicatorsType;
}
