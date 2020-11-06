package sshj.sshj.dto;

import java.util.List;

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
	
	@ApiModelProperty(value = "이미지 urls")
    private List<String> imgUrlList;
	
	@ApiModelProperty(value = "생성 일자")
    private String createdTime;
	
	@ApiModelProperty(value = "최대 인원")
    private int maxParticipant;
	
	@ApiModelProperty(value = "현재 인원")
    private int curParticipant;
	
	@ApiModelProperty(value = "모임 장소")
    private String meetingPlace;
	
	@ApiModelProperty(value = "동아리 이름")
    private String clubName;
	
	@ApiModelProperty(value = "동아리 번호")
    private int clubId;

	@ApiModelProperty(value = "현재 사용자 참여여부")
    private boolean isParticipant;

}
