package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClubInfoDto {
    @ApiModelProperty(value = "동아리 번호")
    private long clubId;

    @ApiModelProperty(value = "동아리 이름")
    private String clubName;
}
