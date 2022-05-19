package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.common.Constants;
import com.tree.clouds.assessment.mapper.AssessmentConditionMapper;
import com.tree.clouds.assessment.model.entity.AssessmentCondition;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.model.vo.ConditionVO;
import com.tree.clouds.assessment.model.vo.GetConditionVO;
import com.tree.clouds.assessment.model.vo.ScorePageVO;
import com.tree.clouds.assessment.service.AssessmentConditionService;
import com.tree.clouds.assessment.service.RoleManageService;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.DownloadFile;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 考核情况表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-08
 */
@Service
public class AssessmentConditionServiceImpl extends ServiceImpl<AssessmentConditionMapper, AssessmentCondition> implements AssessmentConditionService {

    @Autowired
    private RoleManageService roleManageService;
    @Override
    public IPage<ConditionVO> conditionPage(ScorePageVO scorePageVO) {
        List<RoleManage> roleManageList = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> role_admin = roleManageList.stream().map(RoleManage::getRoleCode).filter(code -> code.equals("ROLE_admin")).collect(Collectors.toList());
        if (CollUtil.isEmpty(role_admin)){
            scorePageVO.setUnitName(LoginUserUtil.getUnitName());
        }
        return this.baseMapper.conditionPage(scorePageVO.getPage(), scorePageVO);
    }

    @Override
    public List<AssessmentCondition> getConditionList(GetConditionVO getConditionVO) {
        return this.list(new QueryWrapper<AssessmentCondition>()
                .eq(AssessmentCondition.ASSESSMENT_YEAR, getConditionVO.getAssessmentYear())
                .eq(AssessmentCondition.UNIT_NAME, getConditionVO.getUnitName()));
    }

    @Override
    public void exportDate(GetConditionVO getConditionVO, HttpServletResponse response) {
        List<AssessmentCondition> conditionList = getConditionList(getConditionVO);
        if (CollUtil.isEmpty(conditionList)){
            throw new BaseBusinessException(400,"该单位明细表为空");
        }
        String resource = this.getClass().getClassLoader().getResource("detail.xlsx").getFile();
        String fileName = getConditionVO.getAssessmentYear() + "年" + conditionList.get(0).getUnitName() + "绩效考评加减分一览表.xlsx";
        Map<String, Object> head = new HashMap<>();
        head.put("year", getConditionVO.getAssessmentYear());
        head.put("unitName",conditionList.get(0).getUnitName());
        List<Map<String, Object>> dates = new ArrayList<>();
        for (int i = 0; i < conditionList.size(); i++) {
            AssessmentCondition draw = conditionList.get(i);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("index", i + 1);
            data.put("indicatorsName", draw.getIndicatorsName());
            data.put("assessmentCriteria", draw.getAssessmentCriteria());
            data.put("illustrate", draw.getIllustrate());
            dates.add(data);
        }
        ExcelWriter excelWriter = EasyExcel.write(Constants.TMP_HOME + fileName)
                .withTemplate(resource)
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(dates, writeSheet);
        excelWriter.fill(head, writeSheet);
        excelWriter.finish();
        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);
    }
}
