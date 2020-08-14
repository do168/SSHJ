package sshj.sshj.service;

import java.util.List;

import sshj.sshj.dto.ReviewDto;

/**
 * 후기 관련 서비스
 * @author krims
 *
 */
public interface ReviewService {
	
	/**
	 * 후기 작성
	 * @param reviewDto
	 */
	public void createReview(ReviewDto reviewDto);
	
	/**
	 * 후기 리스트 조회
	 * @param reviewDto
	 */
	public List<ReviewDto> getReviewList(long meetingId, int page, int size);
}
