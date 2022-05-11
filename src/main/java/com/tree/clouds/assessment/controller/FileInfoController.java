package com.tree.clouds.assessment.controller;


import cn.hutool.core.util.ObjectUtil;
import com.tree.clouds.assessment.common.RestResponse;
import com.tree.clouds.assessment.common.aop.Log;
import com.tree.clouds.assessment.model.vo.FileInfoVO;
import com.tree.clouds.assessment.model.vo.PublicIdsReqVO;
import com.tree.clouds.assessment.service.FileInfoService;
import com.tree.clouds.assessment.utils.QiniuUtil;
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
    @Autowired
    private QiniuUtil qiniuUtil;

    /**
     * 文件上传
     *
     * @param
     * @return
     */
    @PostMapping("/getQNToken")
    @ApiOperation(value = "获取七牛token")
    @Log("获取七牛token")
    public RestResponse<String> getQNToken() {
        return RestResponse.ok(qiniuUtil.getUploadCredential());
    }


    @ApiOperation(value = "删除文件")
    @PostMapping(value = "/delete", name = "删除文件")
    @Log("删除文件")
    public RestResponse<Boolean> delete(@RequestBody @Validated PublicIdsReqVO publicIdsReqVO) {
        for (String id : publicIdsReqVO.getIds()) {
            fileInfoService.deleteByBizId(id);
        }
        return RestResponse.ok(true);
    }

    @ApiOperation(value = "下载文件")
    @PostMapping(value = "/downFile", name = "下载文件")
    @Log("下载文件")
    public void downFile(HttpServletResponse response, @RequestParam(value = "biz") @ApiParam("业务id") String id) {
        fileInfoService.downFile(id, response);
    }

}

