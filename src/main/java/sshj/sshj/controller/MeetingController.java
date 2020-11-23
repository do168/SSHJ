package sshj.sshj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.MeetingDto;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.service.ExpoPushService;
import sshj.sshj.service.MeetingService;

@Api(value="MeetingController", description="MeetingController")
@RequestMapping("/meeting")
@RestController
@Slf4j
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ExpoPushService expoPushService;

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "모임 생성 Api"
            , notes = "모임 생성 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/create", method= RequestMethod.POST)
    public ResponseEntity<MeetingDto> createMeeting(
            @ModelAttribute MeetingDto meetingDto) throws Exception{
        
    	log.info("meetingDto [{}]", meetingDto);
    	
    	MeetingDto resultDto = meetingService.insertMeeting(meetingDto);
        
        // 모임 생성 시 해당 동아리를 구독 중이던 유저들에게 푸시알림
//        expoPushService.sendingPushMeetingCreated(meetingDto.getClubId());
        return new ResponseEntity<MeetingDto>(resultDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = "모임 읽기 Api"
            , notes = "모임 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/read", method= RequestMethod.GET)
    public ResponseEntity<MeetingDto> readMeeting(
    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
    		@ApiParam("meetingId") @RequestParam("meetingId")long meetingId ) throws Exception{
        MeetingDto meetingDto=meetingService.selectMeeting(userHeaderModel.getUserId(), meetingId);
        return new ResponseEntity<>(meetingDto,HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "모임 업데이트 Api"
            , notes = "모임 업데이트 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/update", method= RequestMethod.PUT)
    public ResponseEntity<Void> updateMeeting(@ModelAttribute MeetingDto meetingDto) throws Exception{
        meetingService.updateMeeting(meetingDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "모임 삭제 Api"
            , notes = "모임 삭제 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/delete", method= RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMeeting(@ApiParam("meetingId") @RequestParam("meetingId")int meetingId) throws Exception{
        meetingService.deleteMeeting(meetingId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    /**
     *
     * @param userHeaderModel requestParam 전용 모델 -> 토큰에 담겨있는 정보
     * @return
     */

    @ApiOperation(
            value = "전체 모임 읽기 Api"
            , notes = "전체 모임 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readAll", method= RequestMethod.GET)
    public ResponseEntity<List> readAllMeeting(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel
    ) throws Exception{
    	log.info("실행 readAll");
        log.info("userId : "+userHeaderModel.getUserId());
        List<MeetingDto> list=meetingService.selectMeetingList();
        
        log.info("list [{}]", list);
        
        return new ResponseEntity<List>(list,HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리별 모임 읽기 Api"
            , notes = "동아리별 모임 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/club/list", method= RequestMethod.GET)
    public ResponseEntity<List> readClubByMeeting(int clubId) throws Exception{
        List<MeetingDto> list=meetingService.selectClubByMeetingList(clubId);
        return new ResponseEntity<List>(list,HttpStatus.OK);
    }

    // TODO: userId 바꿔야함
    @ApiOperation(
            value = "유저별 모임 읽기 Api"
            , notes = "유저별 모임 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/user/list", method= RequestMethod.GET)
    public ResponseEntity<List> readUserByMeeting(
    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        
    	List<MeetingDto> list=meetingService.selectUserByMeetingList(userHeaderModel.getUserId());

        return new ResponseEntity<List>(list, HttpStatus.OK);
    }
    
    
    // TODO: userId 바꿔야함
    @ApiOperation(
            value = "모임별 좋아요 삽입 Api"
            , notes = "모임별 좋아요 삽입 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/like/insert", method= RequestMethod.POST)
    public ResponseEntity<Void> insertMeetingLike(
    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,int meetingId) throws Exception{
        
    	meetingService.insertMeetingLike(userHeaderModel.getUserId(), meetingId);
        
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // 	TODO: userId 바꿔야함
    @ApiOperation(
            value = "모임별 좋아요 삭제 Api"
            , notes = "모임별 좋아요 삭제 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/like/delete", method= RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMeetingLike(
    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel, 
    		@ApiParam(value = "모임 번호") @RequestParam("meetingId") int meetingId) throws Exception{
        
    	meetingService.deleteMeetingLike(userHeaderModel.getUserId(), meetingId);
        
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    @ApiOperation(
            value = "유저 모임 참여 Api"
            , notes = "유저 모임 참여 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/user/register", method= RequestMethod.POST)
    public ResponseEntity<Void> registerUserMeeting(
    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
    		@ApiParam(value = "모임 번호") @RequestParam("meetingId")long meetingId) throws Exception{
    	
    	meetingService.registerUserApplied(userHeaderModel.getUserId(), meetingId);
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiOperation(
            value = "유저 모임 참여 취소 Api"
            , notes = "유저 모임 참여 취소 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/user/cancel", method= RequestMethod.DELETE)
    public ResponseEntity<Void> cancelUserMeeting(
    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
    		@ApiParam(value = "모임 번호") @RequestParam("meetingId")long meetingId) throws Exception{
    	
    	meetingService.deleteUserApplied(userHeaderModel.getUserId(), meetingId);
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
