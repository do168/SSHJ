package sshj.sshj.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import sshj.dto.ApiResult;
import sshj.sshj.dto.ClubDto;
import sshj.sshj.dto.MeetingDto;
import sshj.sshj.dto.UserDto;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.dto.enums.FlagEnum;
import sshj.sshj.model.Meeting;
import sshj.sshj.service.MeetingService;

import static sshj.dto.ApiResult.succeed;

@Api(value="MeetingController")
@RequestMapping("/meetings")
@RestController
@Slf4j
public class MeetingController {
    @Autowired
    private MeetingService meetingService;


    @ApiOperation(
            value = "모임 생성 Api"
            , notes = "모임 생성 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @PostMapping(value = "/")
    public ApiResult<MeetingDto> createMeeting(
            @RequestBody Meeting meetingParam) throws Exception{
        
        Meeting createdMeeting = meetingService.create(meetingParam);
        return ApiResult.creationSucceed(
                new MeetingDto(createdMeeting)
        );
    }

    @ApiOperation(
            value = "모임 읽기 Api"
            , notes = "모임 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @GetMapping(value = "/{id}")
    public ApiResult<MeetingDto> readMeeting(
    		@ApiParam("meetingId") @PathVariable("id") long id ) throws Exception{
        Meeting meeting = meetingService.find(id);
        return succeed(
                new MeetingDto(meeting)
        );
    }

    @ApiOperation(
            value = "모임 업데이트 Api"
            , notes = "모임 업데이트 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/", method= RequestMethod.PUT)
    public ApiResult<MeetingDto> updateMeeting(@ModelAttribute MeetingDto meetingDto,
                                              @RequestParam(name = "id") long id) throws Exception{
        Meeting updatedMeeting = meetingService.update(id, meetingDto);
        return succeed(
                new MeetingDto(updatedMeeting)
        );
    }

    @ApiOperation(
            value = "모임 삭제 Api"
            , notes = "모임 삭제 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/", method= RequestMethod.DELETE)
    public ApiResult<Boolean> deleteMeeting(@ApiParam("meetingId") @RequestParam("id") long id) throws Exception{
        meetingService.delete(id);
        return succeed(true);
    }

    /**
     *
     * @param clubId 특정 동아리 모임 읽기
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "전체 모임 읽기 Api"
            , notes = "전체 모임 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @GetMapping(value = "/")
    public ApiResult<List<MeetingDto>> readMeetingList(
            @RequestParam (name = "clubId") long clubId,
            @RequestParam (name = "userId") long userId) throws Exception{
        List<MeetingDto> meetings = new ArrayList<>();

        meetingService.findAll(userId, clubId).forEach(meeting -> meetings.add(new MeetingDto(meeting)));

        return succeed(meetings);
    }

//    @ApiOperation(
//            value = "유저 모임 참여 Api"
//            , notes = "유저 모임 참여 Api"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/user/register", method= RequestMethod.POST)
//    public ResponseEntity<Void> registerUserMeeting(
//    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
//    		@ApiParam(value = "모임 번호") @RequestParam("meetingId")long meetingId) throws Exception{
//
//    	meetingService.registerUserApplied(userHeaderModel.getUserId(), meetingId);
//
//    	return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ApiOperation(
//            value = "유저 모임 참여 취소 Api"
//            , notes = "유저 모임 참여 취소 Api"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/user/cancel", method= RequestMethod.DELETE)
//    public ResponseEntity<Void> cancelUserMeeting(
//    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
//    		@ApiParam(value = "모임 번호") @RequestParam("meetingId")long meetingId) throws Exception{
//
//    	meetingService.deleteUserApplied(userHeaderModel.getUserId(), meetingId);
//
//    	return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ApiOperation(
//            value = "클럽 모임 참여 유저 리스트"
//            , notes = "클럽 모임 참여 유저 리스트"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/club/byClub/users", method = RequestMethod.GET)
//    public ResponseEntity<List<UserDto>> getMeetingUserList(
//    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
//    		@ApiParam(value = "모임 번호") @RequestParam("meetingId")long meetingId) throws Exception{
//
//    	List<UserDto> userList = meetingService.getMeetingUserList(userHeaderModel.getUserId(), meetingId);
//
//    	log.info("[{}]",userList);
//
//    	return new ResponseEntity<>(userList, HttpStatus.OK);
//    }
//
//    @ApiOperation(
//            value = "모임 오픈카톡 링크 조회"
//            , notes = "모임 오픈카톡 링크 조회"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/get/chaturl", method = RequestMethod.GET)
//    public ResponseEntity<String> getMeetingChatUrl(
//    		@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
//    		@ApiParam(value = "모임 번호") @RequestParam("meetingId")long meetingId) throws Exception{
//
//    	String chatUrl = meetingService.getMeetingChatUrl(userHeaderModel.getUserId(), meetingId);
//
//    	log.info("[{}]",chatUrl);
//
//    	return new ResponseEntity<>(chatUrl, HttpStatus.OK);
//    }
}
