package sshj.sshj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sshj.sshj.dto.ReviewDto;

/**
 * 후기 관련 mapper
 * @author krims
 *
 */
@Mapper
public interface ReviewMapper {

	/**
	 * 후기 등록
	 */
	public int createReview(ReviewDto reviewDto);
	
	/**
	 * 후기 리스트
	 */
	public List<ReviewDto> getReviewList(@Param("meetingId")long meetingId, @Param("page")int page, @Param("size")int size);
}
