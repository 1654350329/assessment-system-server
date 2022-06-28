package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserManagePageVO extends PageParam {
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "姓名")
    private String userName;
    @ApiModelProperty(value = "单位")
    private String unitName;

    @ApiModelProperty(value = "单位id")
    private String unitId;

    @ApiModelProperty(value = "账号状态")
    private String accountStatus;


    @ApiModelProperty(value = "角色名称")
    private List<String> roleIds;
}
