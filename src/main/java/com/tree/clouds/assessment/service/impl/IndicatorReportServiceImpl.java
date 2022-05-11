package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.IndicatorReportMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.ConvertUtil;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据上报 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-06
 */
@Service
public class IndicatorReportServiceImpl extends ServiceImpl<IndicatorReportMapper, IndicatorReport> implements IndicatorReportService {

    @Autowired
    private AssessmentIndicatorsService assessmentIndicatorsService;
    @Autowired
    private UnitAssessmentService unitAssessmentService;
    @Autowired
    private UnitManageService unitManageService;
    @Autowired
    private SubmitLogService submitLogService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private AssessmentIndicatorsDetailService detailService;
    @Autowired
    private ScoreRecordService scoreRecordService;
    @Autowired
    private ComprehensiveAssessmentService assessmentService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private RatingRecordHistoryService ratingRecordHistoryService;

    @Override
    public IPage<IndicatorReportVO> assessmentList(AssessmentPageVO assessmentPageVO, Integer type) {
        //获取年度项目
        IPage<AssessmentVO> assessmentVOIPage = assessmentIndicatorsService.assessmentPage(assessmentPageVO);
        List<IndicatorReportVO> collect = new ArrayList<>();
        if (type == 1) {
            List<UnitManage> list = unitManageService.list();
            for (UnitManage unitManage : list) {
                List<IndicatorReportVO> IndicatorReportVOs = assessmentVOIPage.getRecords().stream().map(record -> {
                    IndicatorReportVO indicatorReportVO = BeanUtil.toBean(record, IndicatorReportVO.class);
                    //获取分配指标数
                    indicatorReportVO.setDistributeNumber(unitAssessmentService.getDistributeNumber(unitManage.getUnitId()));
                    //获取提交材料数
                   Integer ids = this.baseMapper.getSubmitNumber(unitManage.getUnitId());
                    indicatorReportVO.setSubmitNumber(ids);
                    //获取通过审核数
                    indicatorReportVO.setPassNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 1, null));
                    //获取未通过审核数
                    indicatorReportVO.setRejectionsNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 2, null));
                    //获取待审核数
                    indicatorReportVO.setPendingNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 0, null));
                    //是否超期
                    if (StrUtil.isNotBlank(indicatorReportVO.getExpirationDate())) {
                        indicatorReportVO.setOverdue(new Date().getTime() > DateUtil.parseDate(indicatorReportVO.getExpirationDate()).getTime());
                    }
                    indicatorReportVO.setUnitName(unitManage.getUnitName());
                    indicatorReportVO.setUnitId(unitManage.getUnitId());
                    //已评数
                    indicatorReportVO.setReviewedNumber(this.baseMapper.getReviewedNumber(indicatorReportVO.getUnitId(), 1, DateUtil.year(new Date())));
                    indicatorReportVO.setUnReviewedNumber(this.baseMapper.getReviewedNumber(indicatorReportVO.getUnitId(), 0, DateUtil.year(new Date())));
                    //是否完成填报
                    return indicatorReportVO;
                }).collect(Collectors.toList());
                collect.addAll(IndicatorReportVOs);
            }
        } else {
            collect = assessmentVOIPage.getRecords().stream().map(record -> {
                IndicatorReportVO indicatorReportVO = BeanUtil.toBean(record, IndicatorReportVO.class);
                //获取分配指标数
                indicatorReportVO.setDistributeNumber(unitAssessmentService.getDistributeNumber(LoginUserUtil.getUnitId()));
                //获取提交材料数
                Integer submitNumber = this.baseMapper.getSubmitNumber(LoginUserUtil.getUnitId());
                indicatorReportVO.setSubmitNumber(submitNumber);
                //获取通过审核数
                indicatorReportVO.setPassNumber(this.baseMapper.getStatusNumber(LoginUserUtil.getUnitId(), 1, null));
                //获取未通过审核数
                indicatorReportVO.setRejectionsNumber(this.baseMapper.getStatusNumber(LoginUserUtil.getUnitId(), 2, null));
                //获取待审核数
                indicatorReportVO.setPendingNumber(this.baseMapper.getStatusNumber(LoginUserUtil.getUnitId(), 0, null));
                //是否超期
                if (StrUtil.isNotBlank(indicatorReportVO.getExpirationDate())) {
                    indicatorReportVO.setOverdue(new Date().getTime() > DateUtil.parseDate(indicatorReportVO.getExpirationDate()).getTime());
                }
                UnitManage unitManage = unitManageService.getById(LoginUserUtil.getUnitId());
                if (unitManage != null) {
                    indicatorReportVO.setUnitName(unitManage.getUnitName());
                }
                return indicatorReportVO;
            }).collect(Collectors.toList());
        }
        collect = collect.stream().filter(indicatorReportVO -> indicatorReportVO.getDistributeNumber() != 0).collect(Collectors.toList());

        IPage<IndicatorReportVO> iPage = new Page(assessmentVOIPage.getCurrent(), assessmentVOIPage.getSize());
        int cursor = Math.toIntExact(((assessmentVOIPage.getCurrent() - 1) * assessmentVOIPage.getSize()));
        int limit = Math.toIntExact(assessmentVOIPage.getSize());
        paging(cursor, limit, collect);
        iPage.setRecords(collect);
        iPage.setTotal(assessmentVOIPage.getTotal());
        return iPage;
    }

    @Override
    public List<AssessmentListVO> assessmentList() {
        return this.baseMapper.getAssessmentList(LoginUserUtil.getUnitId());
    }

    //手动实现分页
    public List<IndicatorReportVO> paging(int cursor, int limit, List<IndicatorReportVO> list) {
        //手动实现分页
        if (cursor < 0 || cursor >= list.size() || limit <= 0) {
            return null;
        }
        int lastIndex = cursor + limit;
        if (lastIndex > list.size()) {
            lastIndex = list.size();
        }
        //获得分页后的deviceIdList
        list = list.subList(cursor, lastIndex);
        return list;
    }

    @Override
    @Transactional
    public void updateReport(UpdateReportVO updateReportVO, int progress) {
        IndicatorReport one = this.getById(updateReportVO.getReportId());
        one.setUserScore(updateReportVO.getUserScore());
        one.setIllustrate(updateReportVO.getIllustrate());
        one.setCreatedUser(LoginUserUtil.getUserId());
        one.setReportProgress(1);
        one.setReportStatus(1);
        one.setReportTime(DateUtil.now());
        this.updateById(one);

        //新增文件
        fileInfoService.deleteByBizId(one.getReportId());
        fileInfoService.saveFileInfo(updateReportVO.getFileInfoVOS(), one.getReportId());
        //新增报送历史日志
        AssessmentIndicators assessmentIndicators = assessmentIndicatorsService.getById(one.getIndicatorsId());
        AssessmentIndicatorsDetail detail = detailService.getById(one.getDetailId());
        submitLogService.addLog(assessmentIndicators, detail.getAssessmentCriteria(), null, null, LoginUserUtil.getUnitId(), one.getReportTime());
        //新增到初审

        AuditLog auditLog = new AuditLog();
        auditLog.setDetailId(one.getDetailId());
        auditLog.setReportId(one.getReportId());
        auditLog.setIndicatorsStatus(0);
        auditLogService.saveOrUpdate(auditLog, new QueryWrapper<AuditLog>().eq(AuditLog.REPORT_ID, one.getReportId()));
    }

    @Override
    public IndicatorReport getByReportIdAndUnitId(String reportId, String unitId) {
        return this.getOne(new QueryWrapper<IndicatorReport>()
                .eq(IndicatorReport.REPORT_ID, reportId));
    }

    @Override
    public IPage<AssessmentErrorVO> assessmentErrorList(AssessmentPageVO assessmentPageVO) {
        IPage<IndicatorReport> page = this.baseMapper.getErrorList(assessmentPageVO.getPage(), LoginUserUtil.getUnitId());
        List<AssessmentErrorVO> list = new ArrayList<>();
        for (IndicatorReport record : page.getRecords()) {
            AssessmentIndicators assessmentIndicators = assessmentIndicatorsService.getById(record.getIndicatorsId());
            AssessmentIndicatorsDetail detail = detailService.getById(record.getDetailId());
            AssessmentErrorVO assessmentErrorVO = BeanUtil.toBean(assessmentIndicators, AssessmentErrorVO.class);
            assessmentErrorVO.setIndicatorsId(detail.getProjectId());
            assessmentErrorVO.setReportId(record.getReportId());
            assessmentErrorVO.setAssessmentDate(record.getReportTime());
            assessmentErrorVO.setExpirationDate(assessmentIndicators.getExpirationDate());
            assessmentErrorVO.setAssessmentCriteria(detail.getAssessmentCriteria());
            //责任人
            UserManage userManage = userManageService.getById(record.getCreatedUser());
            assessmentErrorVO.setCreatedUser(userManage.getUserName());
            //责任人
            assessmentErrorVO.setPhoneNumber(userManage.getPhoneNumber());
            //单位
            UnitManage unitManage = unitManageService.getById(userManage.getUnitId());
            assessmentErrorVO.setUnitName(unitManage.getUnitName());
            assessmentErrorVO.setStatus(0);
            list.add(assessmentErrorVO);
        }
        IPage<AssessmentErrorVO> assessmentErrorVOIPage = new Page<>();
        assessmentErrorVOIPage.setRecords(list);
        assessmentErrorVOIPage.setTotal(page.getTotal());
        assessmentErrorVOIPage.setSize(page.getSize());
        assessmentErrorVOIPage.setCurrent(page.getCurrent());
        return assessmentErrorVOIPage;
    }

    /**
     * @param reportId
     * @param progress
     */
    @Override
    public void updateProgress(String reportId, int progress) {
        IndicatorReport indicatorReport = new IndicatorReport();
        indicatorReport.setReportId(reportId);
        indicatorReport.setReportProgress(progress);
        this.updateById(indicatorReport);
    }

    @Override
    public double getUserScore(String unitId, String assessmentYear) {
        Double score = this.baseMapper.getUserScoreByUnit(unitId, assessmentYear);
        return score == null ? 0 : score;
    }

    /**
     * 填报树列表
     *
     * @param id
     * @param unitId
     * @param reportId
     * @return
     */
    @Override
    public List<indicatorsTreeTreeVO> getTreeById(String id, String unitId, String reportId, Integer indicatorsStatus) {
        if (unitId == null) {
            unitId = LoginUserUtil.getUnitId();
        }
        List<indicatorsTreeTreeVO> list = this.assessmentIndicatorsService.getByReportId(id, unitId, reportId, indicatorsStatus);
        for (indicatorsTreeTreeVO indicatorsTreeTreeVO : list) {
            if (indicatorsTreeTreeVO.getAssessmentType() == 1) {
                indicatorsTreeTreeVO.setParentId("0");
            }
            if (indicatorsTreeTreeVO.getAssessmentType() != 4) {
                indicatorsTreeTreeVO.setFile("文件夹");
            } else {
                indicatorsTreeTreeVO.setFile("文件");
                //获取上报信息
                IndicatorReport one = this.getById(indicatorsTreeTreeVO.getId());
                if (one == null) {
                    continue;
                }
                AssessmentIndicators indicators = assessmentIndicatorsService.getById(one.getIndicatorsId());
                one.setExpirationDate(indicators.getExpirationDate());
                UserManage userManage = userManageService.getById(one.getCreatedUser());
                one.setCreatedUser(userManage.getUserName());
                one.setPhoneNumber(userManage.getPhoneNumber());
                List<FileInfo> reportFileInfo = fileInfoService.getByBizIdsAndType(one.getDetailId(), null);
                one.setFileInfoVOS(reportFileInfo);

                //获取考核指标信息
                AssessmentIndicatorsDetail detail = this.detailService.getById(one.getDetailId());
                indicatorsTreeTreeVO.setInstructions(detail.getInstructions());
                List<FileInfo> fileInfos = fileInfoService.getByBizIdsAndType(detail.getDetailId(), null);
                List<FileInfoVO> collect = fileInfos.stream().map(fileInfo -> BeanUtil.toBean(fileInfo, FileInfoVO.class)).collect(Collectors.toList());
                indicatorsTreeTreeVO.setFileInfoVOS(collect);
                indicatorsTreeTreeVO.setIndicatorReport(one);
                //获取审核信息
                AuditLog auditLog = this.auditLogService.getByReportId(indicatorsTreeTreeVO.getId());
                if (auditLog != null) {
                    indicatorsTreeTreeVO.setAuditLog(auditLog);
                }
                //专家初评分信息
                List<RatingRecordHistory> historyList = ratingRecordHistoryService.getHistoryList(indicatorsTreeTreeVO.getId());
                if (historyList != null) {
                    indicatorsTreeTreeVO.setScoreRecords(historyList);
                }
                //复核说明
                ScoreRecord scoreRecord = scoreRecordService.getByReportId(indicatorsTreeTreeVO.getId());
                if (scoreRecord != null) {
                    indicatorsTreeTreeVO.setScoreRecord(scoreRecord);
                    indicatorsTreeTreeVO.setScoreRecords(historyList);
                }

            }
            indicatorsTreeTreeVO.setFraction(this.detailService.getScoreByType(indicatorsTreeTreeVO.getId(), indicatorsTreeTreeVO.getAssessmentType(), String.valueOf(DateUtil.year(new Date()))));
        }
        List<indicatorsTreeTreeVO> indicatorsTreeTreeVOS = ConvertUtil.convertTree(list, treeVO -> "0".equals(treeVO.getParentId()));
        return indicatorsTreeTreeVOS.stream().filter(indicatorsTreeTreeVO -> indicatorsTreeTreeVO.getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public int getMaterial(Integer type, String unitId) {
        return this.baseMapper.getMaterial(type, unitId);
    }

    @Override
    public Integer getReviewedNumber(String unitId, int type, Integer year) {
        return this.baseMapper.getReviewedNumber(unitId, 1, DateUtil.year(new Date()));
    }

    @Override
    public Map<String, Object> getData(GetDataVO getDataVO) {
        Map<String, Object> map = new LinkedHashMap();
        Integer sum = this.baseMapper.getDistributeSum(LoginUserUtil.getUnitId(), getDataVO.getYear());
        map.put("总分值", sum);
        Double UserScore = this.baseMapper.getUserScoreByUnit(LoginUserUtil.getUnitId(), getDataVO.getYear());
        map.put("自评总分", UserScore == null ? 0 : UserScore);
        return map;
    }

    @Override
    public Double getUserScoreByUnit(String unitId, int year) {
        Double UserScore = this.baseMapper.getUserScoreByUnit(unitId, String.valueOf(year));
        return UserScore == null ? 0 : UserScore;
    }


}
