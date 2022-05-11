package com.tree.clouds.assessment.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentConditionVO {

    @ApiModelProperty(value = "考评年份")
    private String assessmentYear;

    @ApiModelProperty(value = "责任单位")
    private String unitId;

    @ApiModelProperty(value = "考核项目")
    private String indicatorsName;

    @ApiModelProperty(value = "考核标准")
    private String assessmentCriteria;

    @ApiModelProperty(value = "得分说明")
    private String illustrate;
}
