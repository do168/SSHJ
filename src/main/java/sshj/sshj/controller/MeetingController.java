package sshj.sshj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sshj.sshj.dto.MeetingDto;
import sshj.sshj.service.MeetingService;

@Api(value="MeetingController", description="MeetingController")
@RequestMapping("/meeting")
@RestController
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
    @RequestMapping(value = "/create", method= RequestMethod.POST)
    public ResponseEntity<Void> createMeeting(@ModelAttribute MeetingDto meetingDto) throws Exception{
        meetingService.insertMeeting(meetingDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "모임 읽기 Api"
            , notes = "모임 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/read", method= RequestMethod.POST)
    public ResponseEntity<MeetingDto> readMeeting(int meetingId) throws Exception{
        MeetingDto meetingDto=meetingService.selectMeeting(meetingId);
        return new ResponseEntity<MeetingDto>(meetingDto,HttpStatus.OK);
    }

    @ApiOperation(
            value = "모임 업데이트 Api"
            , notes = "모임 업데이트 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/update", method= RequestMethod.POST)
    public ResponseEntity<Void> updateMeeting(@ModelAttribute MeetingDto meetingDto) throws Exception{
        meetingService.updateMeeting(meetingDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "모임 삭제 Api"
            , notes = "모임 삭제 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/delete", method= RequestMethod.POST)
    public ResponseEntity<Void> deleteMeeting(int meetingId) throws Exception{
        meetingService.deleteMeeting(meetingId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "전체 모임 읽기 Api"
            , notes = "전체 모임 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readAll", method= RequestMethod.POST)
    public ResponseEntity<List> readAllMeeting() throws Exception{
        List<MeetingDto> list=meetingService.selectMeetingList();
        return new ResponseEntity<List>(list,HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리별 모임 읽기 Api"
            , notes = "동아리별 모임 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readClubByMeeting", method= RequestMethod.POST)
    public ResponseEntity<List> readClubByMeeting(int clubId) throws Exception{
        List<MeetingDto> list=meetingService.selectClubByMeetingList(clubId);
        return new ResponseEntity<List>(list,HttpStatus.OK);
    }

    @ApiOperation(
            value = "유저별 모임 읽기 Api"
            , notes = "유저별 모임 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readUserByMeeting", method= RequestMethod.POST)
    public ResponseEntity<List> readUserByMeeting(int userId) throws Exception{
        List<MeetingDto> list=meetingService.selectUserByMeetingList(userId);
        return new ResponseEntity<List>(list, HttpStatus.OK);
    }
}
