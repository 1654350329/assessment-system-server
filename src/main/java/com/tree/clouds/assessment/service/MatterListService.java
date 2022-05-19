package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.MatterList;
import com.tree.clouds.assessment.model.vo.PageParam;

/**
 * <p>
 * 待办l列表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-05-18
 */
public interface MatterListService extends IService<MatterList> {

    IPage<MatterList> matterListPage(PageParam pageVO);

    void addMatter(String title, String unitId, String reportId, String userId, Integer type,String year,String indicatorsId);

    void updateMatter(String matterId);
}
