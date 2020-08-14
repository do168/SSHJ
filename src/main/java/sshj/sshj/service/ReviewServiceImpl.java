package sshj.sshj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sshj.sshj.dto.ReviewDto;
import sshj.sshj.mapper.ReviewMapper;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewMapper reviewMapper;
	
	@Override
	public void createReview(ReviewDto reviewDto) {
		int cnt = reviewMapper.createReview(reviewDto);
		
		if(cnt != 1)
			throw new RuntimeException();
	}
	
	@Override
	public List<ReviewDto> getReviewList(long meetingId, int page, int size){
		return reviewMapper.getReviewList(meetingId, page, size);
	}
}
