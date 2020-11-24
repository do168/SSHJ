package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoModel {

    @ApiModelProperty(value = "포탈이메일", required = true)
    private String email;
	
    @ApiModelProperty(value = "패스워드", required = true)
    private String password;

    @ApiModelProperty(value = "닉네임", required = true)
    private String nickname;

    @ApiModelProperty(value = "인증코드", required = true)
    private String code;
}
