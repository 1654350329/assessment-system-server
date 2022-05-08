package com.tree.clouds.assessment.controller;


import cn.hutool.core.util.ObjectUtil;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.vo.FileInfoVO;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件信息 前端控制器
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/file-info")
@Api(value = "FileInfo", tags = "文件管理模块")
public class FileInfoController {
    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload-image")
    @ApiOperation(value = "图片上传")
    @Log("图片上传")
    public RestResponse<FileInfoVO> uploadImage(@RequestParam("file") MultipartFile file) {
        FileInfoVO fileInfo = this.fileInfoService.upload(file);
        if (ObjectUtil.isEmpty(fileInfo)) {
            return RestResponse.fail(400, "只允许图片上传!");
        }
        return RestResponse.ok(fileInfo);
    }
//    @ApiOperation(value = "验证文件是否上传")
//    @PostMapping(value = "/exist")
//    public Result exist(@RequestParam("md5") String md5) {
//        return ResponseEntity.ok(fileInfoService.exist(md5));
//    }

//    @ApiOperation(value = "上传文件")
//    @PostMapping(value = "/upload", name = "上传文件")
//    public ResponseEntity<FileInfoVO> upload(MultipartFile file) {
//        return ResponseEntity.ok(fileInfoService.upload(file));
//    }


    @ApiOperation(value = "删除文件")
    @PostMapping(value = "/delete", name = "删除文件")
    @Log("删除文件")
    public RestResponse<Boolean> delete(@RequestBody @Validated PublicIdsReqVO publicIdsReqVO) {
        for (String id : publicIdsReqVO.getIds()) {
            fileInfoService.deleteByBizId(id);
        }
        return RestResponse.ok(true);
    }
//
//    @ApiOperation(value = "预览文件")
//    @PostMapping(value = "/preview", name = "预览文件")
//    public void preview(HttpServletResponse response, @RequestParam(value = "bizId") @ApiParam("业务id") String id) {
//        fileInfoService.preview(id, response);
//    }

    @ApiOperation(value = "下载文件")
    @PostMapping(value = "/downFile", name = "下载文件")
    @Log("下载文件")
    public void downFile(HttpServletResponse response, @RequestParam(value = "biz") @ApiParam("业务id") String id) {
        fileInfoService.downFile(id, response);
    }

}

