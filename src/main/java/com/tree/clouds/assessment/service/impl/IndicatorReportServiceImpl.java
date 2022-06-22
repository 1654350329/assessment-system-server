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
import com.tree.clouds.assessment.utils.BaseBusinessException;
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
    @Autowired
    private RoleManageService roleManageService;
    @Autowired
    private MatterListService matterListService;

    @Override
    public IPage<IndicatorReportVO> assessmentList(AssessmentPageVO assessmentPageVO, Integer type) {
        //获取已发布年度项目
        assessmentPageVO.setIndicatorsStatus("1");
        IPage<AssessmentVO> assessmentVOIPage = assessmentIndicatorsService.assessmentPage(assessmentPageVO);
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> roleCodes = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        List<IndicatorReportVO> collect = new ArrayList<>();
        if (type == 1 || type == 2) {
            List<UnitManage> list;
            if (type == 1) {
                if (!roleCodes.contains("ROLE_admin")) {
                    assessmentPageVO.setUnitName(LoginUserUtil.getUnitName());
                }
                list = unitManageService.getListByType(UnitManage.UNIT_TYPE_ZERO, assessmentPageVO.getUnitName());
            } else {
                QueryWrapper<UnitManage> queryWrapper = new QueryWrapper<>();
                if (StrUtil.isNotBlank(assessmentPageVO.getUnitName())) {
                    queryWrapper.like(UnitManage.UNIT_NAME, assessmentPageVO.getUnitName());
                }
                list = unitManageService.list(queryWrapper);
            }
            for (UnitManage unitManage : list) {
                if (assessmentPageVO.getIndicatorsType() != null && !Objects.equals(unitManage.getUnitType(), assessmentPageVO.getIndicatorsType())) {
                    continue;
                }
                for (AssessmentVO record : assessmentVOIPage.getRecords()) {
                    if (!Objects.equals(unitManage.getUnitType(), record.getIndicatorsType())) {
                        continue;
                    }

                    IndicatorReportVO indicatorReportVO = BeanUtil.toBean(record, IndicatorReportVO.class);
                    //获取分配指标数
                    Integer expertDistributeNumber;
                    if (type == 2) {
                        expertDistributeNumber = unitAssessmentService.getExpertDistributeNumber(unitManage.getUnitId(), LoginUserUtil.getUnitId());
                    } else {
                        expertDistributeNumber = unitAssessmentService.getDistributeNumber(unitManage.getUnitId(), 0);
                    }
                    indicatorReportVO.setDistributeNumber(expertDistributeNumber);
                    if (indicatorReportVO.getDistributeNumber() == 0) {
                        continue;
                    }
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
                    indicatorReportVO.setReviewedNumber(this.baseMapper.getReviewedNumber(indicatorReportVO.getUnitId(), 1, record.getAssessmentYear(), LoginUserUtil.getUnitId()));
                    indicatorReportVO.setUnReviewedNumber(this.baseMapper.getReviewedNumber(indicatorReportVO.getUnitId(), 0, record.getAssessmentYear(), LoginUserUtil.getUnitId()));
                    //是否完成填报
                    indicatorReportVO.setUnitType(unitManage.getUnitType());
                    if (assessmentPageVO.getOverdue() != null && indicatorReportVO.getOverdue() != assessmentPageVO.getOverdue()) {
                        continue;
                    }
                    collect.add(indicatorReportVO);
                }
            }
        } else {
            collect = assessmentVOIPage.getRecords().stream().map(record -> {
                IndicatorReportVO indicatorReportVO = BeanUtil.toBean(record, IndicatorReportVO.class);
                //获取分配指标数
                indicatorReportVO.setDistributeNumber(unitAssessmentService.getDistributeNumber(LoginUserUtil.getUnitId(), 0));
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
        if (!roleCodes.contains("ROLE_admin")) {
            collect = collect.stream().filter(indicatorReportVO -> indicatorReportVO.getDistributeNumber() != 0).collect(Collectors.toList());
        }

        IPage<IndicatorReportVO> iPage = new Page(assessmentVOIPage.getCurrent(), assessmentVOIPage.getSize());
        int cursor = Math.toIntExact(((assessmentVOIPage.getCurrent() - 1) * assessmentVOIPage.getSize()));
        int limit = Math.toIntExact(assessmentVOIPage.getSize());
        paging(cursor, limit, collect);
        iPage.setRecords(collect);
        iPage.setTotal(collect.size());
        return iPage;
    }

    @Override
    public IPage<AssessmentListVO> assessmentList(PageParam pageParam) {
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> roleCodes = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        String unitId = null;
        if (!roleCodes.contains("ROLE_admin")) {
            unitId = LoginUserUtil.getUnitId();
        }
        IPage<AssessmentListVO> page = pageParam.getPage();
        return this.baseMapper.getAssessmentList(page, unitId);
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
        //判断填报时间是否截止
        IndicatorReport one = this.getById(updateReportVO.getReportId());
        AssessmentIndicators assessmentIndicators = assessmentIndicatorsService.getById(one.getIndicatorsId());
        if (new Date().getTime() > DateUtil.parseDateTime(assessmentIndicators.getExpirationDate()).getTime()) {
            throw new BaseBusinessException(400, "填报时间已截止!");
        }

        AssessmentIndicatorsDetail detail = detailService.getById(one.getDetailId());
//        AssessmentIndicators indicators = this.assessmentIndicatorsService.getById(detail.getIndicatorsId());
//        if (!indicators.getIndicatorsName().equals("绩效减分")) {
//            if (StrUtil.isNotBlank(updateReportVO.getUserScore()) && Double.parseDouble(updateReportVO.getUserScore()) > detail.getFraction()) {
//                throw new BaseBusinessException(400, "自评分数不许大于考核指标分数");
//            }
//        } else {
//            if (StrUtil.isNotBlank(updateReportVO.getUserScore()) && Double.parseDouble(updateReportVO.getUserScore()) < detail.getFraction()) {
//                throw new BaseBusinessException(400, "扣分分数不许超过考核指标分数");
//            }
//        }

        if (StrUtil.isNotBlank(updateReportVO.getUserScore())) {
            one.setUserScore(Double.valueOf(updateReportVO.getUserScore()));
        }

        one.setIllustrate(updateReportVO.getIllustrate());
        one.setCreatedUser(LoginUserUtil.getUserId());
        one.setReportProgress(progress);
        one.setReportStatus(1);
        one.setReportTime(DateUtil.now());
        this.updateById(one);

        //新增文件
        fileInfoService.deleteByBizId(one.getReportId());
        fileInfoService.saveFileInfo(updateReportVO.getFileInfoVOS(), one.getReportId());
        //新增报送历史日志
        submitLogService.addLog(assessmentIndicators, detail.getAssessmentCriteria(), 0, null, LoginUserUtil.getUnitId(),
                one.getReportTime(), one.getReportId(), assessmentIndicators.getExpirationDate());
        //新增到初审
        AuditLog auditLog = new AuditLog();
        auditLog.setDetailId(one.getDetailId());
        auditLog.setReportId(one.getReportId());
        auditLog.setIndicatorsStatus(0);
        auditLogService.saveOrUpdate(auditLog, new QueryWrapper<AuditLog>().eq(AuditLog.REPORT_ID, one.getReportId()));
        //添加到待办列表
        UnitManage unitManage = unitManageService.getById(one.getUnitId());
        matterListService.addMatter(unitManage.getUnitName() + "-" + assessmentIndicators.getIndicatorsName() + "-" + detail.getAssessmentCriteria() + "材料上报待审核", one.getUnitId(), one.getReportId(), null, 1, assessmentIndicators.getAssessmentYear(), assessmentIndicators.getIndicatorsId());
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
            assessmentErrorVO.setExpirationDate(record.getExpirationDate());
            assessmentErrorVO.setAssessmentCriteria(detail.getAssessmentCriteria());
            //责任人
            UserManage userManage = userManageService.getInfo(record.getCreatedUser());
            assessmentErrorVO.setCreatedUser(userManage.getUserName());
            //责任人
            assessmentErrorVO.setPhoneNumber(userManage.getPhoneNumber());
            //单位
            assessmentErrorVO.setUnitName(userManage.getUnitName());
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
     * @param content
     * @param type
     * @return
     */
    @Override
    public List<IndicatorsTreeTreeVO> getTreeById(String id, String unitId, String reportId, Integer indicatorsStatus, String content, int type) {
        if (StrUtil.isBlank(unitId)) {
            unitId = LoginUserUtil.getUnitId();
        }
        List<IndicatorsTreeTreeVO> list = this.assessmentIndicatorsService.getByReportId(id, unitId, reportId, indicatorsStatus, content);
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> roleCodes = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : list) {

            if (!(roleCodes.contains("ROLE_admin") || roleCodes.contains("ROLE_user_admin")) && type == 3 && indicatorsTreeTreeVO.getAssessmentType() == 2) {
                AssessmentIndicators assessmentIndicators = this.assessmentIndicatorsService.getById(indicatorsTreeTreeVO.getId());
                if (!assessmentIndicators.getUnitId().equals(LoginUserUtil.getUnitId())) {
                    continue;
                }
            }
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

                UserManage userManage = userManageService.getById(one.getCreatedUser());
                one.setCreatedUser(userManage.getUserName());
                one.setPhoneNumber(userManage.getPhoneNumber());
                //获取考核指标信息
                AssessmentIndicatorsDetail detail = this.detailService.getById(one.getDetailId());
                if (detail == null) {
                    continue;
                }
                List<FileInfo> detailFileInfo = fileInfoService.getByBizIdsAndType(detail.getDetailId(), null);
                detail.setFileInfoVOS(detailFileInfo);
                List<FileInfo> reportFileInfo = fileInfoService.getByBizIdsAndType(one.getReportId(), null);
                one.setFileInfoVOS(reportFileInfo);

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
        List<IndicatorsTreeTreeVO> IndicatorsTreeTreeVOS = ConvertUtil.convertTree(list, treeVO -> "0".equals(treeVO.getParentId()));
        List<IndicatorsTreeTreeVO> collect = IndicatorsTreeTreeVOS.stream().filter(indicatorsTreeTreeVO -> indicatorsTreeTreeVO.getId().equals(id)).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            throw new BaseBusinessException(400, "该项目下,未搜到相应数据!");
        }
        return collect;
    }

    @Override
    public int getMaterial(Integer type, String unitId) {
        return this.baseMapper.getMaterial(type, unitId);
    }

    @Override
    public int getMaterial(String unitId, String expertUnit, Integer type) {
        return this.baseMapper.getMaterialReport(unitId, expertUnit, type);
    }

    @Override
    public int getReportSum(String unitId, String expertUnit, Integer type) {
        return this.baseMapper.getReport2Sum(unitId, expertUnit, type);
    }

    @Override
    public int getReportSum(Integer type, String unitId) {
        return this.baseMapper.getReportSum(type, unitId);
    }

    @Override
    public Integer getReviewedNumber(String unitId, int type, String year) {
        return this.baseMapper.getReviewedNumber(unitId, type, year, null);
    }

    @Override
    public Map<String, Object> getData(GetDataVO getDataVO) {
        Map<String, Object> map = new LinkedHashMap<>();
        Double sum = this.baseMapper.getDistributeSum(LoginUserUtil.getUnitId(), getDataVO.getYear());
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

    @Override
    public Integer getAuditNumber(String unitId, int year) {
        Integer number = this.baseMapper.getAuditNumber(unitId, year);
        return number == null ? 0 : number;
    }

    @Override
    public List<IndicatorsTreeTreeVO> scoreLeftTree(Integer year, String unitId, int progress) {
        UnitManage unitManage = unitManageService.getById(unitId);
        List<IndicatorsTreeTreeVO> scoreLeftTree = assessmentIndicatorsService.getScoreLeftTree(year, unitId, unitManage.getUnitType(), progress);
        for (IndicatorsTreeTreeVO treeVO : scoreLeftTree) {
            if (treeVO == null || treeVO.getChildren() == null) {
                continue;
            }
            if (progress == 2) {
                treeVO.getChildren().removeIf(chTree -> {
                    AssessmentIndicators indicators = this.assessmentIndicatorsService.getById(chTree.getId());
                    return 2 == indicators.getEvaluationMethod();
                });
            }

        }
        return scoreLeftTree;
    }


}
