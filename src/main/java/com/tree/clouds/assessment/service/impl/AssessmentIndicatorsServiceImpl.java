package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.word.Word07Writer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.common.Constants;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.AssessmentIndicatorsMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.ConvertUtil;
import com.tree.clouds.assessment.utils.DownloadFile;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.util.*;
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
    @Autowired
    private RoleManageService roleManageService;
    @Autowired
    private MatterListService matterListService;

    @Override
    public List<IndicatorsTreeTreeVO> indicatorsTree(Integer year, Integer type, String unitId, String userId, Integer indicatorsType) {
        if (year == null) {
            year = DateUtil.year(new Date());
        }
        List<IndicatorsTreeTreeVO> list = this.baseMapper.getByYear(year, type, indicatorsType);
        if (CollUtil.isEmpty(list)) {
            String[] names = {"绩效任务", "机制创新", "正向激励加分", "绩效减分"};
            List<AssessmentIndicators> assessmentIndicatorsList = new ArrayList<>();
            for (String name : names) {
                AssessmentIndicators assessmentIndicators = new AssessmentIndicators();
                if (name.equals("绩效任务")) {
                    assessmentIndicators.setSort(1);
                }
                if (name.equals("机制创新")) {
                    assessmentIndicators.setSort(2);
                }
                if (name.equals("正向激励加分")) {
                    assessmentIndicators.setSort(3);
                }
                if (name.equals("绩效减分")) {
                    assessmentIndicators.setSort(4);
                }
                assessmentIndicators.setAssessmentYear(String.valueOf(year));
                assessmentIndicators.setParentId("0");
                assessmentIndicators.setAssessmentType(0);
                assessmentIndicators.setIndicatorsStatus(0);
                assessmentIndicators.setIndicatorsType(indicatorsType);
                assessmentIndicators.setIndicatorsName(name);
                assessmentIndicatorsList.add(assessmentIndicators);
            }
            this.saveBatch(assessmentIndicatorsList);
            list = this.baseMapper.getByYear(year, type, indicatorsType);
        }
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : list) {
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
            int number = this.baseMapper.getDistributeNumber(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType(), unitId);
            int sum = detailService.getCountByType(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType());
            int un = sum - number;
            indicatorsTreeTreeVO.setUnallocated(un);
            indicatorsTreeTreeVO.setAllocated(number);
            indicatorsTreeTreeVO.setTotal(sum);
            indicatorsTreeTreeVO.setFraction(this.detailService.getScoreByType(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType(), String.valueOf(year)));
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
     * @param content
     * @return
     */
    public List<IndicatorsTreeTreeVO> getByReportId(String id, String unitId, String reportId, Integer reportStatus, String content) {
        List<IndicatorsTreeTreeVO> treeTreeVOS = this.baseMapper.getByReportId(id, unitId, reportId, reportStatus, content);
        Set<IndicatorsTreeTreeVO> all = new LinkedHashSet<>();
        if (StrUtil.isNotBlank(content)) {
            for (IndicatorsTreeTreeVO treeTreeVO : treeTreeVOS) {
                all.add(treeTreeVO);
                Set<IndicatorsTreeTreeVO> prentById = getPrentById(treeTreeVO.getParentId());
                if (prentById != null) {
                    all.addAll(prentById);
                }
                Set<IndicatorsTreeTreeVO> children = getChildrenById(treeTreeVO.getId(), unitId);
                all.addAll(children);
            }
        } else {
            return treeTreeVOS;
        }
        return new ArrayList<>(all);
    }

    /**
     * 获取所有父级
     *
     * @param id
     * @return
     */
    public Set<IndicatorsTreeTreeVO> getPrentById(String id) {
        Set<IndicatorsTreeTreeVO> set = new LinkedHashSet<>();
        IndicatorsTreeTreeVO tree = this.baseMapper.getIndicatorsTreeTreeVOById(id);
        if (tree.getAssessmentType() == 0) {
            return null;
        }
        set.add(tree);
        while (tree.getAssessmentType() != 1) {
            tree = this.baseMapper.getIndicatorsTreeTreeVOById(tree.getParentId());
            set.add(tree);
        }
        return set;
    }

    /**
     * 获取所有子级
     *
     * @param id
     * @param unitId
     * @return
     */
    public Set<IndicatorsTreeTreeVO> getChildrenById(String id, String unitId) {
        Set<IndicatorsTreeTreeVO> set = new LinkedHashSet<>();
        //获得子集
        List<IndicatorsTreeTreeVO> tree2 = this.baseMapper.getIndicatorsTreeTreeVOByPId(id);


        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : tree2) {
            set.add(indicatorsTreeTreeVO);
            List<IndicatorsTreeTreeVO> VOByPId3 = this.baseMapper.getIndicatorsTreeTreeVOByPId(indicatorsTreeTreeVO.getId());
            if (CollUtil.isEmpty(VOByPId3)) {
                List<IndicatorsTreeTreeVO> VOByPId4 = this.detailService.getByParentId(indicatorsTreeTreeVO.getId(), unitId);
                set.addAll(VOByPId4);
            }
            if (CollUtil.isNotEmpty(VOByPId3)) {
                for (IndicatorsTreeTreeVO treeTreeVO : VOByPId3) {
                    set.add(treeTreeVO);
                    if (treeTreeVO.getAssessmentType() == 3) {
                        List<IndicatorsTreeTreeVO> VOByPId4 = this.detailService.getByParentId(treeTreeVO.getId(), unitId);
                        set.addAll(VOByPId4);
                    } else {
                        List<IndicatorsTreeTreeVO> tree3 = this.baseMapper.getIndicatorsTreeTreeVOByPId(treeTreeVO.getId());
                        for (IndicatorsTreeTreeVO treeVO : tree3) {
                            set.add(treeVO);
                            if (treeTreeVO.getAssessmentType() == 3) {
                                List<IndicatorsTreeTreeVO> VOByPId4 = this.detailService.getByParentId(treeTreeVO.getId(), unitId);
                                set.addAll(VOByPId4);
                            }
                            List<IndicatorsTreeTreeVO> VOByPId4 = this.detailService.getByParentId(treeVO.getId(), LoginUserUtil.getUnitId());
                            set.addAll(VOByPId4);
                        }
                    }
                }
            }
        }
        if (CollUtil.isEmpty(tree2)) {
            List<IndicatorsTreeTreeVO> VOByPId4 = this.detailService.getByParentId(id, unitId);
            set.addAll(VOByPId4);
        }
        return set;
    }

    @Override
    public List<IndicatorsTreeTreeVO> indicatorsChildrenTree(String id) {
        List<IndicatorsTreeTreeVO> list = this.baseMapper.getByIndicatorId(id);
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : list) {
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
        List<IndicatorsTreeTreeVO> IndicatorsTreeTreeVOS = ConvertUtil.convertTree(list, treeVO -> "0".equals(treeVO.getParentId()));
        return IndicatorsTreeTreeVOS.stream().filter(indicatorsTreeTreeVO -> indicatorsTreeTreeVO.getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public AssessmentIndicatorsVO evaluationStandard(String id) {
        AssessmentIndicators indicators = this.getById(id);
        AssessmentIndicatorsVO assessmentIndicatorsVO = BeanUtil.toBean(indicators, AssessmentIndicatorsVO.class);
        if (StrUtil.isNotBlank(indicators.getUnitIds()) && !indicators.getUnitIds().equals("[]")) {
            assessmentIndicatorsVO.setUnitIds(Arrays.asList(indicators.getUnitIds().split(",")));
        }
        return assessmentIndicatorsVO;
    }

    @Override
    @Transactional
    public void deleteIndicators(List<String> ids) {
        Set<String> detailId = new HashSet<>();
        Set<String> indicatorsIds = new HashSet<>();
        for (String id : ids) {
            getStatus(id);
            indicatorsIds.add(id);
            List<AssessmentIndicators> list = this.list(new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.PARENT_ID, id));
            if (CollUtil.isEmpty(list)) {
                List<AssessmentIndicatorsDetail> detailList = this.detailService.list(new QueryWrapper<AssessmentIndicatorsDetail>().eq(AssessmentIndicatorsDetail.ASSESSMENT_ID, id));
                if (detailList != null) {
                    Set<String> collect = detailList.stream().map(AssessmentIndicatorsDetail::getDetailId).collect(Collectors.toSet());
                    detailId.addAll(collect);
                }
            }


            getChild(detailId, indicatorsIds, list);


        }
        if (detailId.size() != 0) {
            this.detailService.removeByIds(detailId);
        }
        this.removeByIds(indicatorsIds);
    }

    private void getChild(Set<String> detailId, Set<String> indicatorsIds, List<AssessmentIndicators> list) {
        for (AssessmentIndicators indicators : list) {
            indicatorsIds.add(indicators.getIndicatorsId());
            if (indicators.getAssessmentType() == 3) {
                List<AssessmentIndicatorsDetail> detailList = this.detailService.list(new QueryWrapper<AssessmentIndicatorsDetail>().eq(AssessmentIndicatorsDetail.ASSESSMENT_ID, indicators.getIndicatorsId()));
                if (detailList != null) {
                    Set<String> collect = detailList.stream().map(AssessmentIndicatorsDetail::getDetailId).collect(Collectors.toSet());
                    detailId.addAll(collect);
                }
            } else {
                list = this.list(new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.PARENT_ID, indicators.getIndicatorsId()));
                if (CollUtil.isNotEmpty(list)) {
                    Set<String> indicatorsId = list.stream().map(AssessmentIndicators::getIndicatorsId).collect(Collectors.toSet());
                    indicatorsIds.addAll(indicatorsId);
                }
                getChild(detailId, indicatorsIds, list);
            }

        }
    }

    @Override
    public void updateIndicators(UpdateIndicatorsVO updateIndicatorsVO) {
        getStatus(updateIndicatorsVO.getIndicatorsId());
        AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateIndicatorsVO, AssessmentIndicators.class);
        this.updateById(assessmentIndicators);
    }

    @Override
    public void addIndicators(UpdateIndicatorsVO updateIndicatorsVO) {
        AssessmentIndicators indicators = this.getById(updateIndicatorsVO.getParentId());
        getStatus(updateIndicatorsVO.getParentId());
        AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateIndicatorsVO, AssessmentIndicators.class);
        assessmentIndicators.setAssessmentType(1);
        assessmentIndicators.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
        assessmentIndicators.setIndicatorsType(indicators.getIndicatorsType());
        this.save(assessmentIndicators);
    }

    @Override
    public void updateTask(UpdateTaskVO updateTaskVO) {

        if (updateTaskVO.getAssessmentType() != 4) {
            getStatus(updateTaskVO.getIndicatorsId());
            AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateTaskVO, AssessmentIndicators.class);
            if (CollUtil.isNotEmpty(updateTaskVO.getUnitIds())) {
                assessmentIndicators.setUnitIds(String.join(",", updateTaskVO.getUnitIds()));
            }
            this.updateById(assessmentIndicators);
        } else {
            AssessmentIndicatorsDetail detail = this.detailService.getById(updateTaskVO.getIndicatorsId());
            AssessmentIndicators assessmentIndicators = this.getById(detail.getIndicatorsId());
            if (!assessmentIndicators.getIndicatorsName().equals("绩效减分") && updateTaskVO.getFraction() < 0) {
                throw new BaseBusinessException(400, "分数为正数!");
            }
            if (assessmentIndicators.getIndicatorsName().equals("绩效减分") && updateTaskVO.getFraction() > 0) {
                throw new BaseBusinessException(400, "绩效减分项目,分数为负数!");
            }
            getStatus(detail.getIndicatorsId());
            detail.setAssessmentCriteria(updateTaskVO.getIndicatorsName());
            detail.setInstructions(updateTaskVO.getInstructions());
            detail.setFraction(updateTaskVO.getFraction());
            this.detailService.updateById(detail);
            //新增文件
            if (CollUtil.isNotEmpty(updateTaskVO.getFileInfoVOS())) {
                fileInfoService.deleteByBizId(detail.getDetailId());
                fileInfoService.saveFileInfo(updateTaskVO.getFileInfoVOS(), detail.getDetailId());
            }
        }

    }

    private void getStatus(String indicatorsId) {
        AssessmentIndicators list = this.getById(indicatorsId);
        if (list.getIndicatorsStatus() != null && list.getIndicatorsStatus() == 1) {
            throw new BaseBusinessException(400, "当前年已发布,不许添加或修改考核指标");
        }
    }

    @Override
    @Transactional
    public void addTask(UpdateTaskVO updateTaskVO) {
        getStatus(updateTaskVO.getParentId());
        AssessmentIndicators indicators = this.getById(updateTaskVO.getParentId());
        if (updateTaskVO.getAssessmentType() != 4) {
            AssessmentIndicators name = this.baseMapper.getByNameAndPid(updateTaskVO.getIndicatorsName(), updateTaskVO.getParentId());
            if (name != null) {
                throw new BaseBusinessException(400, "指标已存在,不许添加!");
            }
            AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateTaskVO, AssessmentIndicators.class);
            assessmentIndicators.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
            assessmentIndicators.setIndicatorsType(indicators.getIndicatorsType());
            assessmentIndicators.setUnitId(updateTaskVO.getUnitId());
            if (CollUtil.isNotEmpty(updateTaskVO.getUnitIds())) {
                assessmentIndicators.setUnitIds(String.join(",", updateTaskVO.getUnitIds()));
            }
            //下属单位 默认考评单位id 10
            if (indicators.getIndicatorsType() == 0 && updateTaskVO.getAssessmentType() == 2) {
                assessmentIndicators.setUnitId("10");
            }
            this.save(assessmentIndicators);
        } else {
            AssessmentIndicatorsDetail detail = this.detailService.getByNameAndPid(updateTaskVO.getIndicatorsName(), updateTaskVO.getParentId());
            if (detail != null) {
                throw new BaseBusinessException(400, "考核指标已存在,不许添加!");
            }
            AssessmentIndicatorsDetail indicatorsDetail = this.getDetail(updateTaskVO.getParentId());
            AssessmentIndicators assessmentIndicators = this.getById(indicatorsDetail.getIndicatorsId());
            if (!assessmentIndicators.getIndicatorsName().equals("绩效减分") && updateTaskVO.getFraction() < 0) {
                throw new BaseBusinessException(400, "分数为正数!");
            }
            if (assessmentIndicators.getIndicatorsName().equals("绩效减分") && updateTaskVO.getFraction() > 0) {
                throw new BaseBusinessException(400, "绩效减分项目,分数为负数!");
            }
            indicatorsDetail.setFraction(updateTaskVO.getFraction());
            indicatorsDetail.setInstructions(updateTaskVO.getInstructions());
            indicatorsDetail.setAssessmentCriteria(updateTaskVO.getIndicatorsName());
            indicatorsDetail.setIndicatorsType(indicators.getIndicatorsType());
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
    @Transactional
    public void releaseAssessment(ReleaseAssessmentVO releaseAssessmentVO) {

        List<AssessmentIndicators> list = this.list(new QueryWrapper<AssessmentIndicators>()
                .eq(AssessmentIndicators.ASSESSMENT_YEAR, releaseAssessmentVO.getAssessmentYear())
                .eq(AssessmentIndicators.INDICATORS_TYPE, releaseAssessmentVO.getIndicatorsType()));
        if (CollUtil.isNotEmpty(list)) {
            AssessmentIndicators assessmentIndicators = list.get(0);
            if (assessmentIndicators.getIndicatorsStatus() != null && assessmentIndicators.getIndicatorsStatus() == 1) {
                throw new BaseBusinessException(400, "已发布不可再发布!");
            }
        }
        if (new Date().getTime() > DateUtil.parseDateTime(releaseAssessmentVO.getExpirationDate()).getTime()) {
            throw new BaseBusinessException(400, "截止时间必须大于当前时间!");
        }
        AssessmentIndicators assessmentIndicators = new AssessmentIndicators();
        assessmentIndicators.setIndicatorsStatus(1);
        assessmentIndicators.setExpirationDate(releaseAssessmentVO.getExpirationDate());
        assessmentIndicators.setReleaseDate(DateUtil.now());
        assessmentIndicators.setReleaseUser(LoginUserUtil.getUserId());
        this.update(assessmentIndicators, new QueryWrapper<AssessmentIndicators>()
                .eq(AssessmentIndicators.ASSESSMENT_YEAR, releaseAssessmentVO.getAssessmentYear())
                .eq(AssessmentIndicators.INDICATORS_TYPE, releaseAssessmentVO.getIndicatorsType()));


        List<IndicatorReport> reports = new ArrayList<>();
        //下属单位
        if (releaseAssessmentVO.getIndicatorsType() == 0) {
            Set<String> unitIds = new HashSet<>();
            List<UnitAssessment> unitAssessments = unitAssessmentService.getByYear(releaseAssessmentVO.getAssessmentYear());
            if (CollUtil.isEmpty(unitAssessments)) {
                throw new BaseBusinessException(400, "责任单位数量为0,不许发布!");
            }
            for (UnitAssessment unitAssessment : unitAssessments) {
                IndicatorReport indicatorReport = new IndicatorReport();
                indicatorReport.setReportProgress(0);
                indicatorReport.setDetailId(unitAssessment.getDetailId());
                indicatorReport.setIndicatorsId(unitAssessment.getIndicatorsId());
                indicatorReport.setUnitId(unitAssessment.getUnitId());
                indicatorReport.setExpirationDate(releaseAssessmentVO.getExpirationDate());
                reports.add(indicatorReport);
                unitIds.add(unitAssessment.getUnitId());
            }
            //添加到待办列表
            for (String unitId : unitIds) {
                matterListService.addMatter(releaseAssessmentVO.getAssessmentYear() + "年考核指标已发布", unitId, null, null, 0, releaseAssessmentVO.getAssessmentYear(), null);
            }
        } else {
            //区县
            List<UnitAssessment> unitAssessments = new ArrayList<>();
            List<UnitManage> areaList = unitManageService.getListByType(UnitManage.UNIT_TYPE_ONE, null);
            List<AssessmentIndicatorsDetail> details = this.detailService.getListByYearAndType(1, releaseAssessmentVO.getAssessmentYear());
            if (CollUtil.isEmpty(details)) {
                throw new BaseBusinessException(400, "发布失败,请先配置指标方案!");
            }
            for (UnitManage unitManage : areaList) {
                for (AssessmentIndicatorsDetail detail : details) {
                    IndicatorReport indicatorReport = new IndicatorReport();
                    AssessmentIndicators indicators = this.getById(detail.getProjectId());
                    if (indicators.getEvaluationMethod() == 1) {
                        indicatorReport.setReportProgress(2);
                    } else {
                        indicatorReport.setReportProgress(3);
                    }
                    indicatorReport.setDetailId(detail.getDetailId());
                    indicatorReport.setIndicatorsId(detail.getIndicatorsId());
                    indicatorReport.setUnitId(unitManage.getUnitId());
                    indicatorReport.setExpirationDate(releaseAssessmentVO.getExpirationDate());
                    reports.add(indicatorReport);
                    UnitAssessment unitAssessment = new UnitAssessment();
                    unitAssessment.setDetailId(detail.getDetailId());
                    unitAssessment.setUnitId(unitManage.getUnitId());
                    unitAssessments.add(unitAssessment);
                    AssessmentIndicators task = this.getById(detail.getTaskId());
                    matterListService.addMatter(unitManage.getUnitName() + "-" + indicators.getIndicatorsName() + "-" + detail.getAssessmentCriteria() + "材料待审",
                            indicatorReport.getUnitId(), indicatorReport.getReportId(), task.getUnitId(), 3,
                            indicators.getAssessmentYear(), indicatorReport.getIndicatorsId());
                }
            }
            unitAssessmentService.saveBatch(unitAssessments);
        }
        //添加到待上报表
        indicatorReportService.saveBatch(reports);
        //添加到评分列表
        List<ScoreRecord> collect = reports.stream().filter(report -> report.getReportProgress() == 2).map(report -> {
            ScoreRecord scoreRecord = new ScoreRecord();
            scoreRecord.setReportId(report.getReportId());
            scoreRecord.setExpertStatus(0);
            return scoreRecord;
        }).collect(Collectors.toList());
        scoreRecordService.saveBatch(collect);


    }

    @Override
    public IPage<AssessmentVO> assessmentPage(AssessmentPageVO assessmentPageVO) {
        IPage<AssessmentVO> page = assessmentPageVO.getPage();
        page = this.baseMapper.assessmentPage(page, assessmentPageVO);
        for (AssessmentVO record : page.getRecords()) {
            record.setUnitNumber(10);
            if (assessmentPageVO.getIndicatorsType() != null && assessmentPageVO.getIndicatorsType() == 0) {
                Integer number = this.baseMapper.getUnitNumberByYear(record.getAssessmentYear());
                record.setUnitNumber(number);
            }
        }
        List<AssessmentVO> collect = page.getRecords().stream().filter(record -> record.getUnitNumber() != 0).collect(Collectors.toList());
        page.setRecords(collect);
        page.setTotal(collect.size());
        return page;
    }

    @Override
    public void updateExpirationDate(String assessmentYear, String expirationDate) {
        this.baseMapper.updateExpirationDate(assessmentYear, expirationDate);
    }

    @Override
    public List<IndicatorsTreeTreeVO> getTreeById(String id) {
        AssessmentIndicatorsDetail detail = detailService.getById(id);
        List<IndicatorsTreeTreeVO> tree = new ArrayList<>();
        //新增项目
        AssessmentIndicators indicators = this.getById(detail.getProjectId());
        IndicatorsTreeTreeVO indicatorsTreeTreeVO = new IndicatorsTreeTreeVO();
        indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
        indicatorsTreeTreeVO.setParentId("0");
        indicatorsTreeTreeVO.setAssessmentType(1);
        indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
        indicatorsTreeTreeVO.setFile("文件夹");
        tree.add(indicatorsTreeTreeVO);
        //新增任务
        if (detail.getTaskId() != null) {
            indicators = this.getById(detail.getTaskId());
            indicatorsTreeTreeVO = new IndicatorsTreeTreeVO();
            indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setParentId(detail.getProjectId());
            indicatorsTreeTreeVO.setAssessmentType(2);
            indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
            indicatorsTreeTreeVO.setFile("文件夹");
            tree.add(indicatorsTreeTreeVO);
            indicators = this.getById(detail.getAssessmentId());
            indicatorsTreeTreeVO = new IndicatorsTreeTreeVO();
            indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setParentId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setAssessmentType(3);
            indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
            indicatorsTreeTreeVO.setFile("文件夹");
            tree.add(indicatorsTreeTreeVO);
        } else {
            indicators = this.getById(detail.getAssessmentId());
            indicatorsTreeTreeVO = new IndicatorsTreeTreeVO();
            indicatorsTreeTreeVO.setId(indicators.getIndicatorsId());
            indicatorsTreeTreeVO.setParentId(detail.getProjectId());
            indicatorsTreeTreeVO.setAssessmentType(3);
            indicatorsTreeTreeVO.setTitle(indicators.getIndicatorsName());
            indicatorsTreeTreeVO.setFile("文件夹");
            tree.add(indicatorsTreeTreeVO);
        }
        indicatorsTreeTreeVO = new IndicatorsTreeTreeVO();
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
    public List<IndicatorsTreeTreeVO> auditTree(AuditTreeVO auditTreeVO) {
        int status = 8;
        if (StrUtil.isNotBlank(auditTreeVO.getReportStatus())) {
            status = Integer.parseInt(auditTreeVO.getReportStatus());
            if (status == 2) {
                status = 7;
            } else {
                //0对应 未审核状态1 1对应已审核
                status = status + 1;
            }
        }
        return this.indicatorReportService.getTreeById(auditTreeVO.getId(), auditTreeVO.getUnitId(), auditTreeVO.getReportId(), status, auditTreeVO.getContent(), 0);
    }


    @Override
    @Transactional
    public void copyTask(Integer year, Integer indicatorsType) {
        int nowYear = DateUtil.year(new Date());
        if (nowYear == year) {
            throw new BaseBusinessException(400, "不许复制当前年指标");
        }
        //判断当前年是否发布
        List<IndicatorsTreeTreeVO> byYear = this.baseMapper.getByYear(nowYear, 0, indicatorsType);
        if (CollUtil.isNotEmpty(byYear)) {
            getStatus(byYear.get(0).getId());
        }
        List<IndicatorsTreeTreeVO> treeTreeVOList = this.baseMapper.getByYear(year, 0, indicatorsType);
        List<AssessmentIndicators> assessmentIndicators = new ArrayList<>();
        List<AssessmentIndicatorsDetail> details = new ArrayList<>();
        //key旧id value新id
        Map<String, String> idMap = new LinkedHashMap<>();
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : treeTreeVOList) {
            String newId = UUID.fastUUID().toString(true);
            //存放
            idMap.put(indicatorsTreeTreeVO.getId(), newId);
        }
        //替换
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : treeTreeVOList) {
            if (indicatorsTreeTreeVO.getAssessmentType() == 4) {
                AssessmentIndicatorsDetail detail = this.getDetail(indicatorsTreeTreeVO.getParentId());
                AssessmentIndicatorsDetail indicatorsDetail = detailService.getById(indicatorsTreeTreeVO.getId());
                indicatorsDetail.setIndicatorsId(idMap.get(detail.getIndicatorsId()));
                indicatorsDetail.setProjectId(idMap.get(detail.getProjectId()));
                indicatorsDetail.setTaskId(idMap.get(detail.getTaskId()));
                indicatorsDetail.setAssessmentId(idMap.get(detail.getAssessmentId()));
                indicatorsDetail.setIndicatorsType(indicatorsType);
                indicatorsDetail.setDetailId(idMap.get(indicatorsTreeTreeVO.getId()));
                details.add(indicatorsDetail);
                List<FileInfo> fileInfos = fileInfoService.getByBizIdsAndType(indicatorsTreeTreeVO.getId(), null);
                List<FileInfoVO> collect = fileInfos.stream().map(fileInfo -> BeanUtil.toBean(fileInfo, FileInfoVO.class)).collect(Collectors.toList());
                fileInfoService.saveFileInfo(collect, idMap.get(indicatorsTreeTreeVO.getId()));
            } else {
                AssessmentIndicators indicators = new AssessmentIndicators();
                indicators.setIndicatorsName(indicatorsTreeTreeVO.getTitle());
                if (indicatorsTreeTreeVO.getTitle().equals("绩效任务")) {
                    indicators.setSort(1);
                }
                if (indicatorsTreeTreeVO.getTitle().equals("机制创新")) {
                    indicators.setSort(2);
                }
                if (indicatorsTreeTreeVO.getTitle().equals("正向激励加分")) {
                    indicators.setSort(3);
                }
                if (indicatorsTreeTreeVO.getTitle().equals("绩效减分")) {
                    indicators.setSort(4);
                }
                if (indicatorsTreeTreeVO.getAssessmentType() == 1) {
                    AssessmentIndicators id = this.getById(indicatorsTreeTreeVO.getId());
                    indicators.setEvaluationMethod(id.getEvaluationMethod());
                }
                if (indicatorsType == 1 && indicatorsTreeTreeVO.getAssessmentType() == 2) {
                    AssessmentIndicators id = this.getById(indicatorsTreeTreeVO.getId());
                    indicators.setUnitId(id.getUnitId());
                    indicators.setUnitIds(id.getUnitIds());
                }
                indicators.setAssessmentYear(String.valueOf(nowYear));

                indicators.setAssessmentType(indicatorsTreeTreeVO.getAssessmentType());
                indicators.setIndicatorsType(indicatorsType);
                indicators.setIndicatorsId(idMap.get(indicatorsTreeTreeVO.getId()));
                String s = idMap.get(indicatorsTreeTreeVO.getParentId());
                indicators.setParentId(s == null ? "0" : s);
                assessmentIndicators.add(indicators);
            }

        }
//        treeTreeVOList.stream().filter(vo -> vo.getParentId().equals(indicatorsTreeTreeVO.getId())).forEach(vo -> vo.setParentId(newId));
//        indicatorsTreeTreeVO.setId(newId);
        removeByYearAndType(nowYear, indicatorsType);
        this.saveBatch(assessmentIndicators);
        this.detailService.saveBatch(details);

    }

    private void removeByYearAndType(Integer year, Integer indicatorsType) {
        List<AssessmentIndicatorsDetail> detailList = this.detailService.getListByYearAndType(indicatorsType, String.valueOf(year));
        if (CollUtil.isNotEmpty(detailList)) {
            List<String> collect = detailList.stream().map(AssessmentIndicatorsDetail::getDetailId).collect(Collectors.toList());
            detailService.removeByIds(collect);
        }
        this.baseMapper.removeByYearAndType(year, indicatorsType);
    }

    @Override
    public double getScoreSum(Integer year, Integer indicatorsType) {
        Double scoreSumByYear = this.baseMapper.getScoreSumByYear(year, indicatorsType);
        return scoreSumByYear == null ? 0 : scoreSumByYear;
    }

    @Override
    public void export(Integer year, Integer indicatorsType, HttpServletResponse response) {
        List<IndicatorsTreeTreeVO> tree = this.indicatorsTree(year, 2, null, null, indicatorsType);

        Word07Writer writer = new Word07Writer();
        writer.addText(new Font("方正小标宋简体", Font.PLAIN, 20), "               ", year + "年指标方案");
        int i = 0;
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : tree) {
            i++;
            // 添加段落（分组）
            writer.addText(new Font("方正小标宋简体", Font.PLAIN, 10), i + " ", indicatorsTreeTreeVO.getTitle(), "   分数:" + indicatorsTreeTreeVO.getFraction());
            int j = 0;
            if (indicatorsTreeTreeVO.getChildren() == null) {
                continue;
            }
            for (IndicatorsTreeTreeVO child : indicatorsTreeTreeVO.getChildren()) {
                j++;
                // 添加段落（项目）
                writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j + " ", child.getTitle(), "   分数:" + child.getFraction());
                if (child.getChildren() == null) {
                    continue;
                }
                int k = 0;
                for (IndicatorsTreeTreeVO childChild : child.getChildren()) {
                    k++;
                    // 添加段落（任务）
                    writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j + "-" + k + " ", childChild.getTitle(), "   分数:" + childChild.getFraction());
                    if (childChild.getChildren() == null) {
                        continue;
                    }
                    int l = 0;
                    for (IndicatorsTreeTreeVO childChildChild : childChild.getChildren()) {
                        l++;
                        // 添加段落（考核指标）
                        if (childChildChild.getAssessmentType() == 4) {
                            childChildChild.setTitle("考核标准:" + childChildChild.getTitle());
                        }
                        writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j + "-" + k + "-" + l + " ", childChildChild.getTitle(), "   分数:" + childChildChild.getFraction());
                        if (childChildChild.getAssessmentType() == 4) {
                            writer.addText(new Font("宋体", Font.PLAIN, 10), "申报填报说明:" + childChildChild.getInstructions());
                        }
                        if (childChildChild.getChildren() == null) {
                            continue;
                        }
                        int m = 0;
                        for (IndicatorsTreeTreeVO childChildChildChild : childChildChild.getChildren()) {
                            m++;
                            // 添加段落（考核指标）
                            if (childChildChild.getAssessmentType() == 4) {
                                childChildChild.setTitle("考核标准:" + childChildChild.getTitle());
                            }
                            // 添加段落（考核标准）
                            writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j + "-" + k + "-" + l + "-" + m + " ", childChildChildChild.getTitle(), "   分数:" + childChildChildChild.getFraction());
                            if (childChildChild.getAssessmentType() == 4) {
                                writer.addText(new Font("宋体", Font.PLAIN, 10), "申报填报说明: " + childChildChildChild.getInstructions());
                            }
                        }
                    }
                }
            }

        }
        String fileName = year + "年指标方案.docx";
        File file = FileUtil.file(Constants.TMP_HOME + fileName);
        // 写出到文件
        writer.flush(file);
        // 关闭
        writer.close();
        byte[] bytes = DownloadFile.File2byte(file);
        DownloadFile.downloadFile(bytes, fileName, response, false);
    }

    @Override
    public List<IndicatorsTreeTreeVO> getScoreLeftTree(Integer year, String unitId, Integer unitType, Integer progress) {
        List<IndicatorsTreeTreeVO> scoreLeftTree = this.baseMapper.getScoreLeftTree(year, unitId, unitType, progress);
        scoreLeftTree.forEach(tree -> {
            if (tree.getAssessmentType() == 0) {
                tree.setFile("文件夹");
            }
            if (tree.getAssessmentType() == 1) {
                tree.setFile("文件");
            }
        });
        scoreLeftTree = ConvertUtil.convertTree(scoreLeftTree, treeVO -> "0".equals(treeVO.getParentId()));
        return scoreLeftTree;
    }

    @Override
    public boolean isExpertUnit(String unitId) {
        return CollUtil.isNotEmpty(this.list(new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.UNIT_ID, unitId)
                .or().like(AssessmentIndicators.UNIT_IDS, unitId)));
    }

    /**
     * 当前单位是否是任务主评单位或者协助单位
     *
     * @param indicatorsId
     * @param unitId
     * @return
     */
    @Override
    public boolean getTaskAndExpertUnitId(String indicatorsId, String unitId) {
        int count = this.baseMapper.getTaskAndExpertUnitId(indicatorsId, unitId);
        return count != 0;
    }

}
