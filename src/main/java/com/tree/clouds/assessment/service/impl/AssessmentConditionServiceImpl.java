package com.tree.clouds.assessment.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.common.Constants;
import com.tree.clouds.assessment.mapper.AssessmentConditionMapper;
import com.tree.clouds.assessment.model.entity.AssessmentCondition;
import com.tree.clouds.assessment.model.entity.UserManage;
import com.tree.clouds.assessment.model.vo.ConditionVO;
import com.tree.clouds.assessment.model.vo.GetConditionVO;
import com.tree.clouds.assessment.model.vo.ScorePageVO;
import com.tree.clouds.assessment.service.AssessmentConditionService;
import com.tree.clouds.assessment.utils.DownloadFile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

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

    @Override
    public IPage<ConditionVO> conditionPage(ScorePageVO scorePageVO) {
        return this.baseMapper.conditionPage(scorePageVO.getPage(), scorePageVO);
    }

    @Override
    public List<AssessmentCondition> getConditionList(GetConditionVO getConditionVO) {
        return this.list(new QueryWrapper<AssessmentCondition>()
                .eq(AssessmentCondition.ASSESSMENT_YEAR, getConditionVO.getAssessmentYear())
                .eq(AssessmentCondition.UNIT_NAME, getConditionVO.getUnitName()));
    }

    @Override
    public void exportDate(GetConditionVO getConditionVO ,HttpServletResponse response) {
        List<AssessmentCondition> conditionList = getConditionList(getConditionVO);
        String fileName = "明细表.xlsx";
        EasyExcel.write(Constants.TMP_HOME + fileName, AssessmentCondition.class).sheet("明细表")
                .doWrite(conditionList);
        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);
    }
}
