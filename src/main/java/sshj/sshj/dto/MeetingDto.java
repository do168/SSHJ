package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MeetingDto {

	@ApiModelProperty(value = "모임 번호")
    private long meetingId;
	
	@ApiModelProperty(value = "모임 이름")
    private String meetingName;
	
	@ApiModelProperty(value = "모집 종료 일자")
    private String deadline;
	
	@ApiModelProperty(value = "모임 시작 일자")
    private String startDate;
	
	@ApiModelProperty(value = "모임 종료 일자")
    private String endDate;
	
	@ApiModelProperty(value = "카테고리")
    private String category;
	
	@ApiModelProperty(value = "상세 제목")
    private String explanationTitle;
	
	@ApiModelProperty(value = "상세 설명")
    private String explanationContent;
	
	@ApiModelProperty(value = "생성 일자")
    private String createdTime;
	
	@ApiModelProperty(value = "최대 인원")
    private int maxParticipant;
	
	@ApiModelProperty(value = "동아리 번호")
    private int clubId;
}
