package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.clouds.assessment.model.entity.*;
import com.tree.clouds.assessment.mapper.IndicatorReportMapper;
import com.tree.clouds.assessment.model.vo.*;
import com.tree.clouds.assessment.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
                    indicatorReportVO.setDistributeNumber(unitAssessmentService.getDistributeNumber(LoginUserUtil.getUnitId()));
                    //获取提交材料数
                    List<String> submitNumber = this.baseMapper.getSubmitNumber(unitManage.getUnitId());
                    List<String> fileIds = new ArrayList<>();
                    for (String fileId : submitNumber) {
                        fileIds.addAll(Arrays.asList(fileId.split(",")));
                    }
                    indicatorReportVO.setSubmitNumber(fileIds.size());
                    //获取通过审核数
                    indicatorReportVO.setPassNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 1, null));
                    //获取未通过审核数
                    indicatorReportVO.setRejectionsNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 2, null));
                    //获取待审核数
                    indicatorReportVO.setPendingNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 0, null));
                    //是否超期
                    indicatorReportVO.setOverdue(new Date().getTime() > DateUtil.parseDateTime(indicatorReportVO.getExpirationDate()).getTime());
                    indicatorReportVO.setUnitName(unitManage.getUnitName());
                    indicatorReportVO.setUnitId(unitManage.getUnitId());
                    //已评数
                    indicatorReportVO.setReviewedNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 1, 2));
                    indicatorReportVO.setUnReviewedNumber(this.baseMapper.getStatusNumber(unitManage.getUnitId(), 1, 1));
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
                List<String> submitNumber = this.baseMapper.getSubmitNumber(LoginUserUtil.getUnitId());
                List<String> fileIds = new ArrayList<>();
                for (String fileId : submitNumber) {
                    fileIds.addAll(Arrays.asList(fileId.split(",")));
                }
                indicatorReportVO.setSubmitNumber(fileIds.size());
                //获取通过审核数
                indicatorReportVO.setPassNumber(this.baseMapper.getStatusNumber(LoginUserUtil.getUnitId(), 1, null));
                //获取未通过审核数
                indicatorReportVO.setRejectionsNumber(this.baseMapper.getStatusNumber(LoginUserUtil.getUnitId(), 2, null));
                //获取待审核数
                indicatorReportVO.setPendingNumber(this.baseMapper.getStatusNumber(LoginUserUtil.getUnitId(), 0, null));
                //是否超期
                indicatorReportVO.setOverdue(new Date().getTime() > DateUtil.parseDateTime(indicatorReportVO.getExpirationDate()).getTime());
                UnitManage unitManage = unitManageService.getById(LoginUserUtil.getUnitId());
                if (unitManage != null) {
                    indicatorReportVO.setUnitName(unitManage.getUnitName());
                }
                return indicatorReportVO;
            }).collect(Collectors.toList());
        }


        IPage<IndicatorReportVO> iPage = new Page(assessmentVOIPage.getCurrent(), assessmentVOIPage.getSize());
        int cursor = Math.toIntExact(((assessmentVOIPage.getCurrent() - 1) * assessmentVOIPage.getSize()));
        int limit = Math.toIntExact(assessmentVOIPage.getSize());
        paging(cursor, limit, collect);
        iPage.setRecords(collect);
        iPage.setTotal(assessmentVOIPage.getTotal());
        return iPage;
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
    public void updateReport(UpdateReportVO updateReportVO, int progress) {
        IndicatorReport indicatorReport = BeanUtil.toBean(updateReportVO, IndicatorReport.class);
        StringBuilder fileId = new StringBuilder();
        updateReportVO.getFilePaths().forEach(id -> {
            fileId.append(id).append(",");
        });
        indicatorReport.setFileId(fileId.toString());
        indicatorReport.setReportProgress(progress);
        this.save(indicatorReport);
        //新增报送历史日志
        AssessmentIndicators assessmentIndicators = assessmentIndicatorsService.getById(updateReportVO.getIndicatorsId());
        submitLogService.addLog(assessmentIndicators, null, null);
    }

    @Override
    public IndicatorReport getByDetailIdAndUnitId(String detailId, String unitId) {
        return this.getOne(new QueryWrapper<IndicatorReport>()
                .eq(IndicatorReport.DETAIL_ID, detailId)
                .eq(IndicatorReport.UNIT_ID, unitId));
    }

    @Override
    public IPage<AssessmentErrorVO> assessmentErrorList(AssessmentPageVO assessmentPageVO) {
        IPage<IndicatorReport> page = this.baseMapper.getErrorList(assessmentPageVO.getPage(), LoginUserUtil.getUnitId());
        List<AssessmentErrorVO> list = new ArrayList<>();
        for (IndicatorReport record : page.getRecords()) {
            AssessmentIndicators assessmentIndicators = assessmentIndicatorsService.getById(record.getIndicatorsId());
            AssessmentErrorVO assessmentErrorVO = BeanUtil.toBean(assessmentIndicators, AssessmentErrorVO.class);
            //责任人
            UserManage userManage = userManageService.getById(record.getCreatedUser());
            assessmentErrorVO.setCreatedUser(userManage.getUserName());
            //责任人
            assessmentErrorVO.setPhoneNumber(userManage.getPhoneNumber());
            //单位
            UnitManage unitManage = unitManageService.getById(userManage.getUnitId());
            assessmentErrorVO.setUnitName(unitManage.getUnitName());

            list.add(assessmentErrorVO);
        }
        IPage<AssessmentErrorVO> assessmentErrorVOIPage = new Page<>();
        assessmentErrorVOIPage.setRecords(list);
        assessmentErrorVOIPage.setTotal(page.getTotal());
        assessmentErrorVOIPage.setSize(page.getSize());
        assessmentErrorVOIPage.setCurrent(page.getCurrent());
        return assessmentErrorVOIPage;
    }

    @Override
    public void updateProgress(String reportId, int progress) {
        IndicatorReport indicatorReport = new IndicatorReport();
        indicatorReport.setReportId(reportId);
        indicatorReport.setReportProgress(progress);
        this.updateById(indicatorReport);
    }

}
