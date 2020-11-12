package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClubNoticeDto {
	
	@ApiModelProperty(value = "공지 번호")
    private long noticeId;

	@ApiModelProperty(value = "동아리 번호")
    private long clubId;
	
	@ApiModelProperty(value = "제목")
    private String title;
	
	@ApiModelProperty(value = "내용")
    private String content;
	
	@ApiModelProperty(value = "생성 날짜")
    private String createdTime;
}
