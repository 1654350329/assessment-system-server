package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.AssessmentIndicators;
import com.tree.clouds.assessment.mapper.AssessmentIndicatorsMapper;
import com.tree.clouds.assessment.model.entity.AssessmentIndicatorsDetail;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.AssessmentIndicatorsDetailService;
import com.tree.clouds.assessment.service.AssessmentIndicatorsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.service.UnitAssessmentService;
import com.tree.clouds.assessment.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 考核指标配置 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class AssessmentIndicatorsServiceImpl extends ServiceImpl<AssessmentIndicatorsMapper, AssessmentIndicators> implements AssessmentIndicatorsService {

    @Autowired
    private UnitAssessmentService unitAssessmentService;
    @Autowired
    private AssessmentIndicatorsDetailService detailService;

    @Override
    public List<indicatorsTreeTreeVO> indicatorsTree(Integer year, Integer type) {
        if (year == null) {
            year = DateUtil.year(new Date());
        }
        List<indicatorsTreeTreeVO> list = this.baseMapper.getByYear(year, type);
        for (indicatorsTreeTreeVO indicatorsTreeTreeVO : list) {
            if (indicatorsTreeTreeVO.getAssessmentType() == 1) {
                indicatorsTreeTreeVO.setFile("文件");
            } else {
                indicatorsTreeTreeVO.setFile("文件夹");
            }
        }
        return ConvertUtil.convertTree(list, treeVO -> "0".equals(treeVO.getParentId()));
    }

    @Override
    public List<indicatorsTreeTreeVO> indicatorsChildrenTree(String id) {
        List<indicatorsTreeTreeVO> list = this.baseMapper.getByIndicatorId(id);
        for (indicatorsTreeTreeVO indicatorsTreeTreeVO : list) {
            if (indicatorsTreeTreeVO.getAssessmentType() == 1) {
                indicatorsTreeTreeVO.setParentId("0");
            }
            if (indicatorsTreeTreeVO.getAssessmentType() != 4) {
                indicatorsTreeTreeVO.setFile("文件夹");
            } else {
                indicatorsTreeTreeVO.setFile("文件");
            }
            indicatorsTreeTreeVO.setFraction(this.detailService.getScoreByType(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType()));
        }
        List<indicatorsTreeTreeVO> indicatorsTreeTreeVOS = ConvertUtil.convertTree(list, treeVO -> "0".equals(treeVO.getParentId()));
        return indicatorsTreeTreeVOS.stream().filter(indicatorsTreeTreeVO -> indicatorsTreeTreeVO.getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public AssessmentIndicators evaluationStandard(String id) {
        return this.getById(id);
    }

    @Override
    public void deleteIndicators(List<String> ids) {
        this.removeByIds(ids);
    }

    @Override
    public void updateIndicators(UpdateIndicatorsVO updateIndicatorsVO) {
        AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateIndicatorsVO, AssessmentIndicators.class);
        this.updateById(assessmentIndicators);
    }

    @Override
    public void addIndicators(UpdateIndicatorsVO updateIndicatorsVO) {
        AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateIndicatorsVO, AssessmentIndicators.class);
        assessmentIndicators.setAssessmentType(1);
        assessmentIndicators.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
        this.save(assessmentIndicators);
    }

    @Override
    public void updateTask(UpdateTaskVO updateTaskVO) {
        if (updateTaskVO.getAssessmentType() != 4) {
            AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateTaskVO, AssessmentIndicators.class);
            this.updateById(assessmentIndicators);
        } else {
            AssessmentIndicatorsDetail detail = BeanUtil.toBean(updateTaskVO, AssessmentIndicatorsDetail.class);
            detail.setDetailId(updateTaskVO.getIndicatorsId());
            this.detailService.updateById(detail);
        }

    }

    @Override
    public void addTask(UpdateTaskVO updateTaskVO) {
        if (updateTaskVO.getAssessmentType() != 4) {
            AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateTaskVO, AssessmentIndicators.class);
            assessmentIndicators.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
            this.save(assessmentIndicators);
        } else {
            AssessmentIndicatorsDetail indicatorsDetail = this.getDetail(updateTaskVO.getParentId());
            indicatorsDetail.setFraction(updateTaskVO.getFraction());
            indicatorsDetail.setFileId(updateTaskVO.getFileId());
            indicatorsDetail.setInstructions(updateTaskVO.getInstructions());
            indicatorsDetail.setAssessmentCriteria(updateTaskVO.getIndicatorsName());
            this.detailService.save(indicatorsDetail);
        }
    }

    private AssessmentIndicatorsDetail getDetail(String parentId) {
        AssessmentIndicatorsDetail indicatorsDetail = new AssessmentIndicatorsDetail();
        //考评标准3
        AssessmentIndicators indicators = this.getById(parentId);
        indicatorsDetail.setAssessmentId(indicators.getIndicatorsId());
        //指标任务或项目
        indicators = this.getById(indicators.getParentId());
        if (indicators.getAssessmentType()==2){
            indicatorsDetail.setTaskId(indicators.getIndicatorsId());
            indicators = this.getById(parentId);
            indicatorsDetail.setProjectId(indicators.getIndicatorsId());
        }else {
            indicatorsDetail.setProjectId(indicators.getIndicatorsId());
        }
        //指标方案主键
        indicators = this.getById(parentId);
        indicatorsDetail.setIndicatorsId(indicators.getIndicatorsId());
        return indicatorsDetail;
    }

    @Override
    public void releaseAssessment(ReleaseAssessmentVO releaseAssessmentVO) {
        AssessmentIndicators assessmentIndicators = new AssessmentIndicators();
        assessmentIndicators.setIndicatorsStatus(1);
        assessmentIndicators.setExpirationDate(releaseAssessmentVO.getExpirationDate());
        assessmentIndicators.setReleaseDate(DateUtil.now());
        this.update(new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.ASSESSMENT_YEAR, releaseAssessmentVO.getAssessmentYear()));
    }

    @Override
    public IPage<AssessmentVO> assessmentPage(AssessmentPageVO assessmentPageVO) {
        IPage<AssessmentVO> page = assessmentPageVO.getPage();
        page = this.baseMapper.assessmentPage(page, assessmentPageVO);
        for (AssessmentVO record : page.getRecords()) {
            int number = this.unitAssessmentService.getCount(record.getAssessmentYear());
            record.setUnitNumber(number);
        }
        return page;
    }

    @Override
    public void updateExpirationDate(String assessmentYear, String expirationDate) {
        this.baseMapper.updateExpirationDate(assessmentYear, expirationDate);
    }



}
