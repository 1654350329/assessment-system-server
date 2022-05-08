package com.tree.clouds.assessment.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.assessment.model.entity.FileInfo;

import java.util.List;


/**
 * <p>
 * 文件信息 Mapper 接口
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface FileInfoMapper extends BaseMapper<FileInfo> {

    /**
     * 根据md5值校验文件是否存在
     *
     * @param md5
     * @return
     */
    default Boolean exist(String md5) {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(FileInfo.MD5, md5);
        return selectCount(wrapper) != 0;
    }

    default List<FileInfo> selectByBizIdsAndType(String bizId, String type) {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(FileInfo.BIZ_ID, bizId);
        if (StrUtil.isNotBlank(type)) {
            wrapper.eq(FileInfo.TYPE, type);
        }
        return this.selectList(wrapper);
    }

    default void deleteByBizIds(String bizId) {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(FileInfo.BIZ_ID, bizId);
        this.delete(wrapper);
    }
}
