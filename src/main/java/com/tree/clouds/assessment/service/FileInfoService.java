package com.tree.clouds.assessment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.assessment.model.entity.FileInfo;
import com.tree.clouds.assessment.model.vo.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 文件信息 服务类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface FileInfoService extends IService<FileInfo> {

    Boolean exist(String md5);

    FileInfoVO upload(MultipartFile file);

    boolean deleteByBizId(String id);

    /**
     * 根据业务id和文件类型获取文件信息
     *
     * @param id
     * @param type
     * @return
     */
    List<FileInfo> getByBizIdsAndType(String id, String type);

//    void preview(String bizId, HttpServletResponse response);

    boolean saveFileInfo(List<FileInfoVO> bizFile, String bizId);

    void downFile(String id, HttpServletResponse response);

}
