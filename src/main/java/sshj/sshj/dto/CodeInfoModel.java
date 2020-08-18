package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CodeInfoModel {

    @ApiModelProperty(value = "포탈이메일", required = true)
    private String email;

    @ApiModelProperty(value = "생성시간", required = true)
    private String created_time;
}
