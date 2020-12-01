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
	int createReview(long meetingId, long userId, String content);
	
	/**
	 * 후기 리스트
	 */
	List<ReviewDto> getReviewList(@Param("meetingId") long meetingId, @Param("offset") int offset, @Param("size") int size);

	/**
	 * 후기 수정
	 */
	int updateReview(long userId, long reviewId, String content);

	/**
	 * 후기 삭제
	 */
	int deleteReview(long userId, long reviewId);
	
}
