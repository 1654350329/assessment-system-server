package com.tree.clouds.assessment.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tree.clouds.assessment.common.ITree;
import com.tree.clouds.assessment.model.entity.AuditLog;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.tree.clouds.assessment.model.entity.RatingRecordHistory;
import com.tree.clouds.assessment.model.entity.ScoreRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class IndicatorsTreeTreeVO implements ITree<IndicatorsTreeTreeVO> {
    @ApiModelProperty(value = "指标主键")
    private String id;

    @ApiModelProperty(value = "parent_Id")
    private String parentId;

    @ApiModelProperty(value = "目录级别 0顶级目录1项目2指标任务3考评标准4考核标准")
    private Integer assessmentType;

    @ApiModelProperty(value = "指标名称")
    private String title;

    @ApiModelProperty(value = "分数")
    private Double fraction;

    @ApiModelProperty(value = "类型")
    private String file;

    @ApiModelProperty(value = "考核标准")
    @TableField("assessment_criteria")
    private String assessmentCriteria;

    @ApiModelProperty(value = "申报填报说明")
    @TableField("instructions")
    private String instructions;

    @ApiModelProperty(value = "附件")
    private List<FileInfoVO> fileInfoVOS;

    @ApiModelProperty(value = "子集")
    private  List<IndicatorsTreeTreeVO> children;

    @ApiModelProperty(value = "未分配")
    private Integer unallocated;
    @ApiModelProperty(value = "已分配")
    private Integer allocated;
    @ApiModelProperty(value = "总数")
    private Integer total;


    @ApiModelProperty(value = "报送信息")
    private IndicatorReport indicatorReport;

    @ApiModelProperty(value = "审核信息")
    private AuditLog auditLog;
    @ApiModelProperty(value = "专家初评分信息")
    private List<RatingRecordHistory> scoreRecords;

    @ApiModelProperty(value = "专家评分信息")
    private ScoreRecord scoreRecord;




}
