package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.SubmitLog;
import com.tree.clouds.assessment.model.vo.SubmitLogPageVO;
import com.tree.clouds.assessment.model.vo.SubmitLogVO;

/**
 * <p>
 * 报送日志 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-07
 */
public interface SubmitLogMapper extends BaseMapper<SubmitLog> {

    IPage<SubmitLogVO> submitLogPage(IPage<SubmitLogVO> page, SubmitLogPageVO submitLogPageVO);

}
