package com.tree.clouds.assessment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 文件信息
 * </p>
 *
 * @author LZK
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("file_info")
@ApiModel(value = "FileInfo对象", description = "文件信息")
public class FileInfo extends BaseEntity implements Serializable {

    public static final String FILE_ID = "FILE_ID";
    public static final String BIZ_ID = "BIZ_ID";
    public static final String FILE_NAME = "FILE_NAME";
    public static final String PREVIEW_PATH = "PREVIEW_PATH";
    public static final String FILE_PATH = "FILE_PATH";
    public static final String TYPE = "TYPE";
    public static final String MD5 = "MD5";
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @TableId(value = FILE_ID, type = IdType.UUID)
    private String fileId;

    @ApiModelProperty(value = "业务主键id")
    @TableField(BIZ_ID)
    private String bizId;

    @ApiModelProperty(value = "资料上报")
    @TableField(FILE_NAME)
    private String fileName;

    @ApiModelProperty(value = "预览文件地址")
    @TableField(PREVIEW_PATH)
    private String previewPath;

    @ApiModelProperty(value = "文件地址")
    @TableField(FILE_PATH)
    private String filePath;

    @ApiModelProperty(value = "类型（1工伤决定书 2病例复印件 3鉴定文件 4审签意见 5劳动能力鉴定附件）")
    @TableField(TYPE)
    private String type;

    @ApiModelProperty(value = "md5值")
    @TableField(MD5)
    private String md5;
}
