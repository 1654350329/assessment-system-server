package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.AssessmentIndicatorsDetailMapper;
import com.tree.clouds.assessment.model.entity.AssessmentIndicatorsDetail;
import com.tree.clouds.assessment.model.vo.IndicatorsTreeTreeVO;
import com.tree.clouds.assessment.service.AssessmentIndicatorsDetailService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 考核指标详细表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
@Service
public class AssessmentIndicatorsDetailServiceImpl extends ServiceImpl<AssessmentIndicatorsDetailMapper, AssessmentIndicatorsDetail> implements AssessmentIndicatorsDetailService {

    @Override
    public double getScoreByType(String id, Integer type, String year) {
        Double score = this.baseMapper.getScoreByType(id, type, year);
        return score == null ? 0 : score;
    }

    @Override
    public double getScoreByUnit(String unitId, String id) {
        Double score = this.baseMapper.getScoreByUnit(unitId, id);
        return score == null ? 0 : score;
    }

    @Override
    public int getCountByType(String id, Integer assessmentType) {
        return this.baseMapper.getCountByType(id, assessmentType);
    }

    @Override
    public AssessmentIndicatorsDetail getByNameAndPid(String name, String parentId) {
        return this.baseMapper.getByNameAndPid(name, parentId);
    }

    @Override
    public AssessmentIndicatorsDetail getByReportId(String reportId) {
        return this.baseMapper.getByReportId(reportId);
    }

    @Override
    public List<IndicatorsTreeTreeVO> getByParentId(String id, String unitId) {
        return this.baseMapper.getByParentId(id, unitId);
    }

    @Override
    public List<AssessmentIndicatorsDetail> getListByYearAndType(int type, String assessmentYear) {
        return this.baseMapper.getListByYearAndType(type,assessmentYear);
    }
}
