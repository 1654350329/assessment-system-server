package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.IndicatorReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.vo.PerformancePageVO;
import com.tree.clouds.assessment.model.vo.PerformanceVO;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * 数据上报 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
public interface IndicatorReportMapper extends BaseMapper<IndicatorReport> {

    List<String> getSubmitNumber(String unitId);

    Integer getStatusNumber(String unitId, int status,Integer progress);

    IPage<IndicatorReport> getErrorList(IPage<IndicatorReport> page, String unitId);

    IPage<IndicatorReport> getListByType(IPage<PerformanceVO> page, PerformancePageVO performancePageVO);
}