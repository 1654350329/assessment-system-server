package com.tree.clouds.assessment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordVO {
    @NotBlank(message = "新密码不许为空")
    @ApiModelProperty("用户旧密码")
    private String password;

    @NotBlank(message = "密码不许为空")
    @ApiModelProperty("用户新密码")
    private String newPassword;

    @NotBlank(message = "二次验证新密码不许为空")
    @ApiModelProperty("二次验证新密码")
    private String towPassword;
}
