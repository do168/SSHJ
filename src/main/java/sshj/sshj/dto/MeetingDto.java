package sshj.sshj.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import sshj.sshj.model.Club;
import sshj.sshj.model.Image;

@Data
public class MeetingDto {

	@ApiModelProperty(value = "모임 번호")
    private long id;

	@ApiModelProperty(value = "동아리")
	private Club club;
	
	@ApiModelProperty(value = "모임 이름")
    private String name;
	
	@ApiModelProperty(value = "모집 종료 일자")
    private LocalDateTime deadline;
	
	@ApiModelProperty(value = "모임 시작 일자")
    private LocalDateTime startDate;
	
	@ApiModelProperty(value = "모임 종료 일자")
    private LocalDateTime endDate;
	
	@ApiModelProperty(value = "카테고리")
    private String category;
	
	@ApiModelProperty(value = "상세 제목")
    private String explanationTitle;
	
	@ApiModelProperty(value = "상세 설명")
    private String explanationContent;
	
	@ApiModelProperty(value = "이미지 urls")
    private List<Image> imgs;
	
	@ApiModelProperty(value = "생성 일자")
    private LocalDateTime createdAt;

	@ApiModelProperty(value = "수정 일자")
    private LocalDateTime updatedAt;
	
	@ApiModelProperty(value = "최대 인원")
    private int maxParticipant;
	
	@ApiModelProperty(value = "모임 장소")
    private String meetingPlace;
	
	@ApiModelProperty(value = "오픈 채팅 url")
    private String chatUrl;

}
