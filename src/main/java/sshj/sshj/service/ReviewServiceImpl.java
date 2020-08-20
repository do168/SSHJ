package sshj.sshj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import sshj.sshj.dto.ReviewDto;
import sshj.sshj.mapper.ReviewMapper;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Override
	public void createReview(ReviewDto reviewDto) {
		
		
		int cnt = reviewMapper.createReview(reviewDto);
		
		if(cnt != 1) {
			log.error("review create failed!!");
			throw new RuntimeException();
		}
	}
	
	@Override
	public List<ReviewDto> getReviewList(long meetingId, int page, int size){
		return reviewMapper.getReviewList(meetingId, page, size);
	}
	
	@Override
	public void updateReview(long userId, long reviewId, String content) {
		int cnt = reviewMapper.updateReview(userId, reviewId, content);
		
		if(cnt != 1) {
			log.error("review update failed!! check correct user or review");
			throw new RuntimeException();
		}
	}
	
	@Override
	public void deleteReview(long userId, long reviewId) {
		int cnt = reviewMapper.deleteReview(userId, reviewId);
		
		if(cnt != 1) {
			log.error("review delete failed!! check correct user or review");
			throw new RuntimeException();
		}
	}
}
