package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.assessment.model.entity.UnitAssessment;
import com.tree.clouds.assessment.mapper.UnitAssessmentMapper;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.vo.UnitVO;
import com.tree.clouds.assessment.model.vo.indicatorsTreeTreeVO;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import com.tree.clouds.assessment.service.UnitAssessmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.service.UnitManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        QueryWrapper<UnitManage> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(unitVO.getUnitName())) {
            queryWrapper.like(UnitManage.UNIT_NAME, unitVO.getUnitName());
        }
        List<UnitManage> unitManages = unitManageService.list(queryWrapper);
        return unitManages.stream().map(unitManage -> {
            UnitVO unit = BeanUtil.toBean(unitManage, UnitVO.class);
            unit.setNumber(this.count(new QueryWrapper<UnitAssessment>().eq(UnitAssessment.UNIT_ID, unitManage.getUnitId())));
            return unit;
        }).collect(Collectors.toList());
    }

    @Override
    public void addAssessment(List<String> ids, String unitId) {
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
        return  this.baseMapper.getCount(assessmentYear);
    }

    @Override
    public Integer getDistributeNumber(String unitId) {
        return this.baseMapper.getDistributeNumber(unitId);

    }

    @Override
    public List<Integer> getScoreByYearAndUnit(String assessmentYear, String unitId) {
        List<indicatorsTreeTreeVO> tree = assessmentIndicatorsService.indicatorsTree(Integer.valueOf(assessmentYear),1);
//        this.list()
        return null;
    }


}
