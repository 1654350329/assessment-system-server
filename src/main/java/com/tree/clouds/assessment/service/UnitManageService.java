package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.UnitManage;
import com.tree.clouds.assessment.model.vo.UnitManagePageVO;

import java.util.List;

/**
 * <p>
 * 分组管理 服务类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface UnitManageService extends IService<UnitManage> {

    IPage<UnitManage> groupManagePage(UnitManagePageVO unitManagePageVO);

    void deleteGroupRole(List<String> ids);

    List<UnitManage> getListByType(Integer type);
}
