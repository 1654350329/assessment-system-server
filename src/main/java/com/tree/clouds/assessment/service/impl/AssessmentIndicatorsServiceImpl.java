package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.word.Word07Writer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
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

    @Override
    public List<IndicatorsTreeTreeVO> indicatorsTree(Integer year, Integer type, String unitId, String userId) {
        if (year == null) {
            year = DateUtil.year(new Date());
        }
        List<IndicatorsTreeTreeVO> list = this.baseMapper.getByYear(year, type);
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(userId);
        List<String> role_admin = roleManages.stream().map(RoleManage::getRoleCode).filter(code -> code.equals("ROLE_admin")||code.equals("ROLE_user_admin")).collect(Collectors.toList());
        if (userId != null && CollUtil.isEmpty(role_admin)) {
            list = list.stream().filter(vo -> {
                AssessmentIndicators id = this.getById(vo.getId());
                return vo.getAssessmentType() != 1 || Objects.equals(id.getUserId(), userId);
            }).collect(Collectors.toList());
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
                Set<IndicatorsTreeTreeVO> children = getChildrenById(treeTreeVO.getId());
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
     * @return
     */
    public Set<IndicatorsTreeTreeVO> getChildrenById(String id) {
        Set<IndicatorsTreeTreeVO> set = new LinkedHashSet<>();
        //获得子集
        List<IndicatorsTreeTreeVO> tree2 = this.baseMapper.getIndicatorsTreeTreeVOByPId(id);
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : tree2) {
            set.add(indicatorsTreeTreeVO);
            List<IndicatorsTreeTreeVO> VOByPId3 = this.baseMapper.getIndicatorsTreeTreeVOByPId(indicatorsTreeTreeVO.getId());
            if (CollUtil.isNotEmpty(VOByPId3)) {
                for (IndicatorsTreeTreeVO treeTreeVO : VOByPId3) {
                    set.add(treeTreeVO);
                    if (treeTreeVO.getAssessmentType() == 3) {
                        List<IndicatorsTreeTreeVO> VOByPId4 = this.detailService.getByParentId(treeTreeVO.getId(), LoginUserUtil.getUnitId());
                        set.addAll(VOByPId4);
                    } else {
                        List<IndicatorsTreeTreeVO> tree3 = this.baseMapper.getIndicatorsTreeTreeVOByPId(treeTreeVO.getId());
                        for (IndicatorsTreeTreeVO treeVO : tree3) {
                            set.add(treeVO);
                            List<IndicatorsTreeTreeVO> VOByPId4 = this.detailService.getByParentId(treeVO.getId(), LoginUserUtil.getUnitId());
                            set.addAll(VOByPId4);
                        }
                    }
                }
            }
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
    public AssessmentIndicators evaluationStandard(String id) {
        return this.getById(id);
    }

    @Override
    public void deleteIndicators(List<String> ids) {
        for (String id : ids) {
            getStatus(id);
        }
        this.removeByIds(ids);
    }

    @Override
    public void updateIndicators(UpdateIndicatorsVO updateIndicatorsVO) {
        getStatus(updateIndicatorsVO.getIndicatorsId());
        AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateIndicatorsVO, AssessmentIndicators.class);
        this.updateById(assessmentIndicators);
    }

    @Override
    public void addIndicators(UpdateIndicatorsVO updateIndicatorsVO) {
        getStatus(updateIndicatorsVO.getParentId());
        AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateIndicatorsVO, AssessmentIndicators.class);
        assessmentIndicators.setAssessmentType(1);
        assessmentIndicators.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
        this.save(assessmentIndicators);
    }

    @Override
    public void updateTask(UpdateTaskVO updateTaskVO) {

        if (updateTaskVO.getAssessmentType() != 4) {
            getStatus(updateTaskVO.getIndicatorsId());
            AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateTaskVO, AssessmentIndicators.class);
            this.updateById(assessmentIndicators);
        } else {
            AssessmentIndicatorsDetail detail = this.detailService.getById(updateTaskVO.getIndicatorsId());
            getStatus(detail.getIndicatorsId());
            detail.setAssessmentCriteria(updateTaskVO.getIndicatorsName());
            detail.setInstructions(updateTaskVO.getInstructions());
            detail.setFraction(updateTaskVO.getFraction());
            this.detailService.updateById(detail);

            fileInfoService.deleteByBizId(detail.getDetailId());
            //新增文件
            fileInfoService.saveFileInfo(updateTaskVO.getFileInfoVOS(), detail.getDetailId());
        }

    }

    private void getStatus(String indicatorsId) {
        AssessmentIndicators list = this.getById(indicatorsId);
        if (list.getIndicatorsStatus() != null && list.getIndicatorsStatus() == 1) {
            throw new BaseBusinessException(400, "当前年已发布,不许添加新的考核指标");
        }
    }

    @Override
    public void addTask(UpdateTaskVO updateTaskVO) {
        getStatus(updateTaskVO.getParentId());
        if (updateTaskVO.getAssessmentType() != 4) {
            AssessmentIndicators name = this.baseMapper.getByNameAndPid(updateTaskVO.getIndicatorsName(), updateTaskVO.getParentId());
            if (name != null) {
                throw new BaseBusinessException(400, "指标已存在,不许添加!");
            }
            AssessmentIndicators assessmentIndicators = BeanUtil.toBean(updateTaskVO, AssessmentIndicators.class);
            assessmentIndicators.setAssessmentYear(String.valueOf(DateUtil.year(new Date())));
            this.save(assessmentIndicators);
        } else {
            AssessmentIndicatorsDetail detail = this.detailService.getByNameAndPid(updateTaskVO.getIndicatorsName(), updateTaskVO.getParentId());
            if (detail != null) {
                throw new BaseBusinessException(400, "考核指标已存在,不许添加!");
            }
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
        if (new Date().getTime() > DateUtil.parseDateTime(releaseAssessmentVO.getExpirationDate()).getTime()) {
            throw new BaseBusinessException(400, "截止时间必须大于当前时间!");
        }
        AssessmentIndicators assessmentIndicators = new AssessmentIndicators();
        assessmentIndicators.setIndicatorsStatus(1);
        assessmentIndicators.setExpirationDate(releaseAssessmentVO.getExpirationDate());
        assessmentIndicators.setReleaseDate(DateUtil.now());
        assessmentIndicators.setReleaseUser(LoginUserUtil.getUserId());
        this.update(assessmentIndicators, new QueryWrapper<AssessmentIndicators>().eq(AssessmentIndicators.ASSESSMENT_YEAR, releaseAssessmentVO.getAssessmentYear()));


        List<UnitAssessment> unitAssessments = unitAssessmentService.getByYear(releaseAssessmentVO.getAssessmentYear());
        List<IndicatorReport> reports = new ArrayList<>();
        for (UnitAssessment unitAssessment : unitAssessments) {
            IndicatorReport indicatorReport = new IndicatorReport();
            indicatorReport.setReportProgress(0);
            indicatorReport.setDetailId(unitAssessment.getDetailId());
            indicatorReport.setIndicatorsId(unitAssessment.getIndicatorsId());
            indicatorReport.setUnitId(unitAssessment.getUnitId());
            indicatorReport.setExpirationDate(releaseAssessmentVO.getExpirationDate());
            reports.add(indicatorReport);
        }

        //添加到待上报表
        indicatorReportService.saveBatch(reports);
    }

    @Override
    public IPage<AssessmentVO> assessmentPage(AssessmentPageVO assessmentPageVO) {
        IPage<AssessmentVO> page = assessmentPageVO.getPage();
        page = this.baseMapper.assessmentPage(page, assessmentPageVO);
        List<String> year = new ArrayList<>();
        List<AssessmentVO> AssessmentVOs = new ArrayList<>();
        for (AssessmentVO record : page.getRecords()) {
            Integer number = this.baseMapper.getUnitNumberByYear(record.getAssessmentYear());
            record.setUnitNumber(number);
            if (!year.contains(record.getAssessmentYear())) {
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
        Integer status = null;
        if (StrUtil.isNotBlank(auditTreeVO.getReportStatus())) {
            status = Integer.parseInt(auditTreeVO.getReportStatus());
            if (status == 2) {
                status = 7;
            }
        }
        return this.indicatorReportService.getTreeById(auditTreeVO.getId(), auditTreeVO.getUnitId(), auditTreeVO.getReportId(), status, auditTreeVO.getContent(), 0);
    }


    @Override
    public void copyTask(Integer year) {
        int nowYear = DateUtil.year(new Date());
        if (nowYear == year) {
            throw new BaseBusinessException(400, "不许复制当前年指标");
        }
        List<IndicatorsTreeTreeVO> treeTreeVOList = this.baseMapper.getByYear(year, 0);
        List<AssessmentIndicators> assessmentIndicators = new ArrayList<>();
        List<AssessmentIndicatorsDetail> details = new ArrayList<>();
        //key旧id value新id
        Map<String, String> idMap = new LinkedHashMap<>();
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : treeTreeVOList) {
            String newId = UUID.randomUUID().toString();
            //存放
            idMap.put(indicatorsTreeTreeVO.getId(), newId);
            treeTreeVOList.stream().filter(vo -> vo.getParentId().equals(indicatorsTreeTreeVO.getId())).forEach(vo -> vo.setParentId(newId));
            indicatorsTreeTreeVO.setId(newId);
            if (indicatorsTreeTreeVO.getAssessmentType() == 4) {
                AssessmentIndicatorsDetail indicatorsDetail = this.getDetail(indicatorsTreeTreeVO.getParentId());
                AssessmentIndicatorsDetail detail = BeanUtil.toBean(indicatorsTreeTreeVO, AssessmentIndicatorsDetail.class);
                detail.setIndicatorsId(idMap.get(indicatorsDetail.getInstructions()));
                detail.setProjectId(idMap.get(indicatorsDetail.getProjectId()));
                detail.setTaskId(idMap.get(indicatorsDetail.getTaskId()));
                detail.setAssessmentId(idMap.get(indicatorsDetail.getAssessmentId()));
                details.add(detail);
            } else {
                AssessmentIndicators indicators = BeanUtil.toBean(indicatorsTreeTreeVO, AssessmentIndicators.class);
                indicators.setAssessmentYear(String.valueOf(nowYear));
                assessmentIndicators.add(indicators);
            }
        }
        this.saveBatch(assessmentIndicators);
        this.detailService.saveBatch(details);

    }

    @Override
    public int getScoreSum(Integer year) {
        Integer scoreSumByYear = this.baseMapper.getScoreSumByYear(year);
        return scoreSumByYear == null ? 0 : scoreSumByYear;
    }

    @Override
    public void export(Integer year, HttpServletResponse response) {
        List<IndicatorsTreeTreeVO> tree = this.indicatorsTree(year, 2, null, null);

        Word07Writer writer = new Word07Writer();
        writer.addText(new Font("方正小标宋简体", Font.PLAIN, 20), "               ", year + "年指标方案");
        int i = 0;
        for (IndicatorsTreeTreeVO indicatorsTreeTreeVO : tree) {
            i++;
            // 添加段落（分组）
            writer.addText(new Font("方正小标宋简体", Font.PLAIN, 10), i + "", indicatorsTreeTreeVO.getTitle(), "   分数:" + indicatorsTreeTreeVO.getFraction());
            int j = 0;
            for (IndicatorsTreeTreeVO child : indicatorsTreeTreeVO.getChildren()) {
                j++;
                // 添加段落（项目）
                writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j, child.getTitle(), "   分数:" + child.getFraction());
                if (child.getChildren() == null) {
                    continue;
                }
                int k = 0;
                for (IndicatorsTreeTreeVO childChild : child.getChildren()) {
                    k++;
                    // 添加段落（任务）
                    writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j + "-" + k, childChild.getTitle(), "   分数:" + childChild.getFraction());
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
                        writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j + "-" + k + "-" + l, childChildChild.getTitle(), "   分数:" + childChildChild.getFraction());
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
                            writer.addText(new Font("宋体", Font.PLAIN, 10), i + "-" + j + "-" + k + "-" + l + "-" + m, childChildChildChild.getTitle(), "   分数:" + childChildChildChild.getFraction());
                            if (childChildChild.getAssessmentType() == 4) {
                                writer.addText(new Font("宋体", Font.PLAIN, 10), "申报填报说明:" + childChildChildChild.getInstructions());
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

}
