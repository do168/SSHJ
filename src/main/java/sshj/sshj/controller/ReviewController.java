package sshj.sshj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sshj.sshj.dto.ReviewDto;
import sshj.sshj.service.ReviewService;

@Api(value="ReviewController", description="ReviewController")
@RequestMapping("/review")
@RestController
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;

    @ApiOperation(
            value = "후기 생성"
            , notes = "후기 생성"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/create", method= RequestMethod.POST)
    public ResponseEntity<Void> createReview(@ModelAttribute ReviewDto reviewDto) throws Exception{
        
    	reviewService.createReview(reviewDto);
    	
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    @ApiOperation(
            value = "후기 리스트 조회"
            , notes = "후기 리스트 조회"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/create", method= RequestMethod.POST)
    public ResponseEntity<List<ReviewDto>> createReview(
    		@ApiParam(value = "모임 번호") @RequestParam("meetingId")long meetingId
    		, @RequestParam("page")int page, @RequestParam("size")int size ) throws Exception{
        
    	List<ReviewDto> reviewList = reviewService.getReviewList(meetingId, page, size); 
    	
        return new ResponseEntity<List<ReviewDto>>(reviewList, HttpStatus.OK);
    }

}
