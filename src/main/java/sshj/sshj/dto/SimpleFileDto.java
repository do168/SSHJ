package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SimpleFileDto {

	@ApiModelProperty(value = "파일 번호")
	private long fileId;

	@ApiModelProperty(value = "소유 유저 번호")
	private long userId;

	@ApiModelProperty(value = "파일 url")
	private String fileUrl;
}
