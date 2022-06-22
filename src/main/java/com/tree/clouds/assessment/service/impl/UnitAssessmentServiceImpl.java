package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.assessment.model.entity.UnitAssessment;
import com.tree.clouds.assessment.mapper.UnitAssessmentMapper;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.vo.UnitVO;
import com.tree.clouds.assessment.model.vo.IndicatorsTreeTreeVO;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import com.tree.clouds.assessment.service.UnitAssessmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.service.UnitManageService;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 指标单位中间表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class UnitAssessmentServiceImpl extends ServiceImpl<UnitAssessmentMapper, UnitAssessment> implements UnitAssessmentService {

    @Autowired
    private UnitManageService unitManageService;
    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;

    @Override
    public List<UnitVO> assessmentList(UnitVO unitVO) {
        if (unitVO.getYear() == null) {
            unitVO.setYear(DateUtil.year(new Date()));
        }
        QueryWrapper<UnitManage> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(unitVO.getUnitName())) {
            queryWrapper.like(UnitManage.UNIT_NAME, unitVO.getUnitName());
        }
        queryWrapper.eq(UnitManage.UNIT_TYPE, UnitManage.UNIT_TYPE_ZERO);
        List<UnitManage> unitManages = unitManageService.list(queryWrapper);
        List<UnitVO> unitVOS = new ArrayList<>();
        for (UnitManage unitManage : unitManages) {
            UnitVO unit = BeanUtil.toBean(unitManage, UnitVO.class);
            unit.setNumber(this.baseMapper.getCountUnit(unitManage.getUnitId(), unitVO.getYear()));
            unitVOS.add(unit);
        }
        return unitVOS;
    }

    @Override
    @Transactional
    public void addAssessment(List<String> ids, String unitId) {
//        if (CollUtil.isEmpty(ids)) {
//            return;
//        }
        Integer release = this.baseMapper.isRelease(DateUtil.year(new Date()));
        if (release != null && release == 1) {
            throw new BaseBusinessException(400, "当前年已发布,不许修改分配考核指标");
        }
        //删除已分配项目
        this.remove(new QueryWrapper<UnitAssessment>().eq(UnitAssessment.UNIT_ID, unitId));
        //重新添加项目
        List<UnitAssessment> collect = ids.stream().map(id -> {
            UnitAssessment unitAssessment = new UnitAssessment();
            unitAssessment.setDetailId(id);
            unitAssessment.setUnitId(unitId);
            return unitAssessment;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

    @Override
    public List<String> getAssessment(String unitId) {
        return this.list(new QueryWrapper<UnitAssessment>().eq(UnitAssessment.UNIT_ID, unitId)).stream().map(UnitAssessment::getDetailId).collect(Collectors.toList());
    }

    @Override
    public int getCount(String assessmentYear) {
        return this.baseMapper.getCount(assessmentYear);
    }

    @Override
    public Integer getDistributeNumber(String unitId, Integer type) {
        return this.baseMapper.getDistributeNumber(unitId, type);

    }


    @Override
    public List<UnitAssessment> getByYear(String assessmentYear) {
        return this.baseMapper.getByYear(assessmentYear);
    }

    @Override
    public Integer getExpertDistributeNumber(String unitId, String expertUnit) {
        return this.baseMapper.getExpertDistributeNumber(unitId, expertUnit);
    }

    @Override
    public Double getScoreByUnitIdAndYear(String unitId, String year) {
        Double scoreByUnitIdAndYear = this.baseMapper.getScoreByUnitIdAndYear(unitId, year);
        return scoreByUnitIdAndYear == null ? 0 : scoreByUnitIdAndYear;
    }



}
