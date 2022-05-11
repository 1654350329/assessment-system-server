package com.tree.clouds.assessment.model.bo;

import com.tree.clouds.assessment.model.entity.UserManage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserManageBO extends UserManage {
    @ApiModelProperty("角色id")
    @NotNull(message = "至少绑定一种角色")
    private List<String> roleIds;
    @ApiModelProperty("单位Id")
    @NotNull(message = "至少绑定一个单位")
    private String unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
