package com.tree.clouds.assessment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.aspose.words.SaveFormat;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.tree.clouds.assessment.common.Constants;
import com.tree.clouds.assessment.mapper.FileInfoMapper;
import com.tree.clouds.assessment.model.entity.FileInfo;
import com.tree.clouds.assessment.model.vo.FileInfoVO;
import com.tree.clouds.assessment.service.FileInfoService;
import com.tree.clouds.assessment.utils.BaseBusinessException;
import com.tree.clouds.assessment.utils.DownloadFile;
import com.tree.clouds.assessment.utils.MultipartFileUtil;
import com.tree.clouds.assessment.utils.Word2PdfUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文件信息 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {
    //访问运行白名单
//    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/png");
    //输出错误日志
    private static final Logger LOGGER = LoggerFactory.getLogger(FileInfoService.class);


    public FileInfoVO upload(MultipartFile multipartFile) {
        FileInfoVO fileInfoVO = new FileInfoVO();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件的类型
        String contentType = multipartFile.getContentType();

        try {
            File file = MultipartFileUtil.multipartFileToFile(multipartFile);
            String formatDate = DateUtil.formatDate(new Date()).replaceAll("-", "/");
            String filePath = Constants.TMP_HOME + formatDate;
            FileUtil.move(file,new File(filePath),true);
            //文件上传
            String type = StringUtils.substringAfterLast(originalFilename, ".");
            // 生成url地址，返回存储路径与预览路径
            fileInfoVO.setFilePath(filePath);
            fileInfoVO.setFileName(originalFilename);
            return fileInfoVO;
        } catch (IOException e) {
            LOGGER.info("服务器内部错误：{}", originalFilename);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean exist(String md5) {
        return this.baseMapper.exist(md5);
    }

    public boolean deleteByBizId(String id) {
        this.baseMapper.deleteByBizIds(id);
        return true;
    }

    @Override
    public List<FileInfo> getByBizIdsAndType(String id, String type) {
        return this.baseMapper.selectByBizIdsAndType(id, type);
    }
    @Override
    public List<FileInfo> selectByBizIds(List<String> ids) {
        return this.baseMapper.selectByBizIds(ids);
    }

//    public void preview(String id, HttpServletResponse response) {
//        FileInfo fileInfo = getByBizId(id);
//        String group = fileInfo.getFilePath().substring(0, fileInfo.getFilePath().indexOf("/"));
//        String fileId = fileInfo.getFilePath().substring(fileInfo.getFilePath().indexOf("/") + 1);
//        File file = fastDFSUtil.getFile(group, fileId);
//        String html = Constants.TMP_HOME + file.getName() + ".html";
//        Word2PdfUtil.doc2(file.getAbsolutePath(), html, SaveFormat.HTML);
//        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(html));
//
//            DownloadFile.downloadFile(fileInputStream, file.getName(), response);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public FileInfo getByBizId(String id) {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(FileInfo.BIZ_ID, id);
        return this.getOne(wrapper);
    }

    @Override
    public boolean saveFileInfo(List<FileInfoVO> bizFiles, String reportId) {
        if (CollUtil.isEmpty(bizFiles)){
            return true;
        }
        List<FileInfo> collect = bizFiles.stream().map(bizFile -> {
            FileInfo fileInfo = BeanUtil.toBean(bizFile, FileInfo.class);
            fileInfo.setBizId(reportId);
            return fileInfo;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
        return true;
    }

    @Override
    public void downFile(String bizId, HttpServletResponse response) {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(FileInfo.BIZ_ID, bizId);
        FileInfo fileInfo = this.getOne(wrapper);
        if (fileInfo == null) {
            throw new BaseBusinessException(400, "文件不存在");
        }
        File file = new File(fileInfo.getFilePath());

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DownloadFile.downloadFile(fileInputStream, file.getName(), response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
