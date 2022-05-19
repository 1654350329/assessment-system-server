package com.tree.clouds.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.assessment.model.entity.MatterList;

import java.util.List;

/**
 * <p>
 * 待办l列表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-05-18
 */
public interface MatterListMapper extends BaseMapper<MatterList> {


    List<MatterList> getMatterList(Integer type, String userId, String unitId);
}
