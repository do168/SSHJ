package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import sshj.sshj.dto.enums.FlagEnum;
import sshj.sshj.dto.enums.MeetingStatusEnum;

@Data
public class MeetingSearchDto {
	
	@ApiModelProperty(value = "유저 번호", hidden = true)
	private long userId;
	
	@ApiModelProperty(value = "동아리 번호")
	private Long clubId;
	
	// TODO: 카테고리를 enum 처리 할지 말지
	@ApiModelProperty(value = "시작 날짜")
	private String startDate;
	
	@ApiModelProperty(value = "종료 날짜")
	private String endDate;
	
	@ApiModelProperty(value = "카테고리")
	private String category;
	
	@ApiModelProperty(value = "모집 상태")
	private MeetingStatusEnum status;
	
	@ApiModelProperty(value = "구독 여부")
	private FlagEnum isSubscribed;
	
	@ApiModelProperty(value = "참여 여부")
	private FlagEnum isApplied;
	
	@ApiModelProperty(value = "페이지 크기")
	private int pageScale;
	
	@ApiModelProperty(value = "페이지 offset")
	private int offset;
	
}
