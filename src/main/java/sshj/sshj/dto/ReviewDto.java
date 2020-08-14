package sshj.sshj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 후기 DTO
 * @author krims
 *
 */
@Data
public class ReviewDto {

	@ApiModelProperty(value = "후기 번호")
	private long reviewId;

	@ApiModelProperty(value = "유저 번호")
	private long userId;

	@ApiModelProperty(value = "모임 번호")
	private long meeingId;

	@ApiModelProperty(value = "후기 내용")
	private long content;

	@ApiModelProperty(value = "작성 일자")
	private long created_time;

}
