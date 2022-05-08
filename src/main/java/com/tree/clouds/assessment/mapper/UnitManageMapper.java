package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.vo.UnitManagePageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 分组管理 Mapper 接口
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface UnitManageMapper extends BaseMapper<UnitManage> {

    IPage<UnitManage> unitManagePage(IPage<UnitManage> page, @Param("unitManagePageVO") UnitManagePageVO unitManagePageVO);
}
