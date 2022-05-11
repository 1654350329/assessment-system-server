package com.tree.clouds.assessment.model.bo;

import com.tree.clouds.assessment.model.entity.AuditLog;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.tree.clouds.assessment.model.vo.indicatorsTreeTreeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndicatorReportBO extends indicatorsTreeTreeVO {


    @ApiModelProperty(value = "报送信息")
    private IndicatorReport indicatorReport;

    @ApiModelProperty(value = "初审信息")
    private AuditLog auditLog;

}
