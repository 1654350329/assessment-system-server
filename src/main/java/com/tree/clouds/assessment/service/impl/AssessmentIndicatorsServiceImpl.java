package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.AssessmentIndicatorsMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private UnitManageService unitManageService;
    @Autowired
    private ScoreRecordService scoreRecordService;
    @Autowired
    private IndicatorReportService indicatorReportService;

    @Override
    public List<indicatorsTreeTreeVO> indicatorsTree(Integer year, Integer type) {
        if (year == null) {
            year = DateUtil.year(new Date());
        }
        List<indicatorsTreeTreeVO> list = this.baseMapper.getByYear(year, type);

        for (indicatorsTreeTreeVO indicatorsTreeTreeVO : list) {
            if (indicatorsTreeTreeVO.getAssessmentType() == 1 && type == 1) {
                indicatorsTreeTreeVO.setFile("文件");
            } else if (indicatorsTreeTreeVO.getAssessmentType() == 4 && type == 2) {
                indicatorsTreeTreeVO.setFile("文件");
                AssessmentIndicatorsDetail detail = this.detailService.getById(indicatorsTreeTreeVO.getId());
                if (detail != null) {
                    indicatorsTreeTreeVO.setInstructions(detail.getInstructions());
                    List<FileInfo> fileInfos = fileInfoService.getByBizIdsAndType(detail.getDetailId(), null);
                    List<FileInfoVO> collect = fileInfos.stream().map(fileInfo -> BeanUtil.toBean(fileInfo, FileInfoVO.class)).collect(Collectors.toList());
                    indicatorsTreeTreeVO.setFileInfoVOS(collect);
                }
            } else {
                indicatorsTreeTreeVO.setFile("文件夹");
            }
            int number = this.baseMapper.getDistributeNumber(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType());
            int sum = detailService.getCountByType(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType());
            int un = sum - number;
            indicatorsTreeTreeVO.setUnallocated(un);
            indicatorsTreeTreeVO.setAllocated(number);
            indicatorsTreeTreeVO.setTotal(sum);
        }
        return ConvertUtil.convertTree(list, treeVO -> "0".equals(treeVO.getParentId()));
    }

    /**
     * 填报树列表
     *
     * @param id
     * @param unitId
     * @param reportId
     * @param reportStatus
     * @return
     */
    public List<indicatorsTreeTreeVO> getByReportId(String id, String unitId, String reportId, Integer reportStatus) {
        return this.baseMapper.getByReportId(id, unitId, reportId, reportStatus);
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
                AssessmentIndicatorsDetail detail = this.detailService.getById(indicatorsTreeTreeVO.getId());
                if (detail != null) {
                    indicatorsTreeTreeVO.setInstructions(detail.getInstructions());
                    List<FileInfo> fileInfos = fileInfoService.getByBizIdsAndType(detail.getDetailId(), null);
                    List<FileInfoVO> collect = fileInfos.stream().map(fileInfo -> BeanUtil.toBean(fileInfo, FileInfoVO.class)).collect(Collectors.toList());
                    indicatorsTreeTreeVO.setFileInfoVOS(collect);
                }

            }
            indicatorsTreeTreeVO.setFraction(this.detailService.getScoreByType(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType(), String.valueOf(DateUtil.year(new Date()))));
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
            AssessmentIndicatorsDetail detail = this.detailService.getById(updateTaskVO.getIndicatorsId());
            detail.setAssessmentCriteria(updateTaskVO.getIndicatorsName());
            detail.setInstructions(updateTaskVO.getInstructions());
            detail.setFraction(updateTaskVO.getFraction());
            this.detailService.updateById(detail);

            fileInfoService.deleteByBizId(detail.getDetailId());
            //新增文件
            fileInfoService.saveFileInfo(updateTaskVO.getFileInfoVOS(), detail.getDetailId());
        }

    }

    @Override
    public void addTask(UpdateTaskVO updateTaskVO) {
        List<AssessmentIndicators> list = this.list(new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.ASSESSMENT_YEAR, DateUtil.year(new Date())));
        if (CollUtil.isNotEmpty(list) && list.get(0).getIndicatorsStatus() != null && list.get(0).getIndicatorsStatus() == 1) {
            throw new BaseBusinessException(400, "当前年已发布,不许添加新的考核指标");
        }
        if (updateTaskVO.getAssessmentType() != 4) {
//            AssessmentIndicators name = this.baseMapper.getByName(updateTaskVO.getIndicatorsName(), DateUtil.year(new Date()));
//            if (name!=null){
//                throw new BaseBusinessException(400,"指标已存在,请重新");
//            }
            AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateTaskVO, AssessmentIndicators.class);
            assessmentIndicators.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
            this.save(assessmentIndicators);
        } else {
            AssessmentIndicatorsDetail indicatorsDetail = this.getDetail(updateTaskVO.getParentId());
            indicatorsDetail.setFraction(updateTaskVO.getFraction());
            indicatorsDetail.setInstructions(updateTaskVO.getInstructions());
            indicatorsDetail.setAssessmentCriteria(updateTaskVO.getIndicatorsName());
            this.detailService.save(indicatorsDetail);
            //新增文件
            fileInfoService.saveFileInfo(updateTaskVO.getFileInfoVOS(), indicatorsDetail.getDetailId());
        }
    }

    private AssessmentIndicatorsDetail getDetail(String parentId) {
        AssessmentIndicatorsDetail indicatorsDetail = new AssessmentIndicatorsDetail();
        //考评标准3
        AssessmentIndicators indicators = this.getById(parentId);
        indicatorsDetail.setAssessmentId(indicators.getIndicatorsId());
        //指标任务或项目
        indicators = this.getById(indicators.getParentId());
        if (indicators.getAssessmentType() == 2) {
            indicatorsDetail.setTaskId(indicators.getIndicatorsId());
            //项目
            indicators = this.getById(indicators.getParentId());
        }
        indicatorsDetail.setProjectId(indicators.getIndicatorsId());

        //指标方案主键
        indicators = this.getById(indicators.getParentId());
        indicatorsDetail.setIndicatorsId(indicators.getIndicatorsId());
        return indicatorsDetail;
    }

    @Override
    public void releaseAssessment(ReleaseAssessmentVO releaseAssessmentVO) {
        List<AssessmentIndicators> list = this.list(new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.ASSESSMENT_YEAR, releaseAssessmentVO.getAssessmentYear()));
        if (CollUtil.isNotEmpty(list)) {
            AssessmentIndicators assessmentIndicators = list.get(0);
            if (assessmentIndicators.getIndicatorsStatus() != null && assessmentIndicators.getIndicatorsStatus() == 1) {
                throw new BaseBusinessException(400, "已发布不可再发布!");
            }
        }
        AssessmentIndicators assessmentIndicators = new AssessmentIndicators();
        assessmentIndicators.setIndicatorsStatus(1);
        assessmentIndicators.setExpirationDate(releaseAssessmentVO.getExpirationDate());
        assessmentIndicators.setReleaseDate(DateUtil.now());
        this.update(assessmentIndicators, new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.ASSESSMENT_YEAR, releaseAssessmentVO.getAssessmentYear()));


        List<UnitAssessment> unitAssessments = unitAssessmentService.getByYear(releaseAssessmentVO.getAssessmentYear());
        List<IndicatorReport> reports = new ArrayList<>();
        for (UnitAssessment unitAssessment : unitAssessments) {
            IndicatorReport indicatorReport = new IndicatorReport();
            indicatorReport.setReportProgress(0);
            indicatorReport.setDetailId(unitAssessment.getDetailId());
            indicatorReport.setIndicatorsId(unitAssessment.getIndicatorsId());
            indicatorReport.setUnitId(unitAssessment.getUnitId());
            reports.add(indicatorReport);
        }

        //添加到待上报表
        indicatorReportService.saveBatch(reports);
    }

    @Override
    public IPage<AssessmentVO> assessmentPage(AssessmentPageVO assessmentPageVO) {
        IPage<AssessmentVO> page = assessmentPageVO.getPage();
        page = this.baseMapper.assessmentPage(page, assessmentPageVO);
        List<String> year=new ArrayList<>();
        List<AssessmentVO> AssessmentVOs=new ArrayList<>();
        for (AssessmentVO record : page.getRecords()) {
            record.setUnitNumber(unitManageService.list().size());
            if (!year.contains( record.getAssessmentYear())){
                AssessmentVOs.add(record);
                year.add(record.getAssessmentYear());
            }
        }
        page.setRecords(AssessmentVOs);
        return page;
    }

    @Override
    public void updateExpirationDate(String assessmentYear, String expirationDate) {
        this.baseMapper.updateExpirationDate(assessmentYear, expirationDate);
    }

    @Override
    public List<indicatorsTreeTreeVO> getTreeById(String id) {
        AssessmentIndicatorsDetail detail = detailService.getById(id);
        List<indicatorsTreeTreeVO> tree = new ArrayList<>();
        //新增项目
        AssessmentIndicators indicators = this.getById(detail.getProjectId());
        indicatorsTreeTreeVO indicatorsTreeTreeVO = new indicatorsTreeTreeVO();
        indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
        indicatorsTreeTreeVO.setParentId("0");
        indicatorsTreeTreeVO.setAssessmentType(1);
        indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
        indicatorsTreeTreeVO.setFile("文件夹");
        tree.add(indicatorsTreeTreeVO);
        //新增任务
        if (detail.getTaskId() != null) {
            indicators = this.getById(detail.getTaskId());
            indicatorsTreeTreeVO = new indicatorsTreeTreeVO();
            indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setParentId(detail.getProjectId());
            indicatorsTreeTreeVO.setAssessmentType(2);
            indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
            indicatorsTreeTreeVO.setFile("文件夹");
            tree.add(indicatorsTreeTreeVO);
            indicators = this.getById(detail.getAssessmentId());
            indicatorsTreeTreeVO = new indicatorsTreeTreeVO();
            indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setParentId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setAssessmentType(3);
            indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
            indicatorsTreeTreeVO.setFile("文件夹");
            tree.add(indicatorsTreeTreeVO);
        } else {
            indicators = this.getById(detail.getAssessmentId());
            indicatorsTreeTreeVO = new indicatorsTreeTreeVO();
            indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setParentId(detail.getProjectId());
            indicatorsTreeTreeVO.setAssessmentType(3);
            indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
            indicatorsTreeTreeVO.setFile("文件夹");
            tree.add(indicatorsTreeTreeVO);
        }
        indicatorsTreeTreeVO = new indicatorsTreeTreeVO();
        indicatorsTreeTreeVO.setId(detail.getDetailId());
        indicatorsTreeTreeVO.setParentId(detail.getAssessmentId());
        indicatorsTreeTreeVO.setAssessmentType(4);
        indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
        indicatorsTreeTreeVO.setFile("文件");
        tree.add(indicatorsTreeTreeVO);

        indicatorsTreeTreeVO.setFraction(this.detailService.getScoreByType(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType(), String.valueOf(DateUtil.year(new Date()))));
        return ConvertUtil.convertTree(tree, treeVO -> "0".equals(treeVO.getParentId()));
    }

    @Override
    public List<AssessmentIndicators> getGroupByYear(String year) {
        return this.baseMapper.getGroupByYear(year);
    }

    @Override
    public List<indicatorsTreeTreeVO> auditTree(AuditTreeVO auditTreeVO) {
        return this.indicatorReportService.getTreeById(auditTreeVO.getId(), auditTreeVO.getUnitId(), auditTreeVO.getReportId(), null);
    }

}
