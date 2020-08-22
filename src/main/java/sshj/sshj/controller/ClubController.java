package sshj.sshj.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sshj.sshj.dto.ClubNoticeDto;
import sshj.sshj.service.ClubService;

import java.util.List;

@Api(value="ClubController")
@RequestMapping("/club")
@RestController
public class ClubController {
    @Autowired
    private ClubService clubService;

    @ApiOperation(
            value = "동아리 공지 생성 Api"
            , notes = "동아리 공지 생성 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/createNotice", method= RequestMethod.POST)
    public ResponseEntity<Void> createClubNotice(@ModelAttribute ClubNoticeDto clubNoticeDto) throws Exception{
        clubService.insertClubNotice(clubNoticeDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 읽기 Api"
            , notes = "동아리 공지 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readNotice", method= RequestMethod.POST)
    public ResponseEntity<ClubNoticeDto> readClubNotice(int noticeId) throws Exception{
        ClubNoticeDto clubNoticeDto=clubService.selectClubNotice(noticeId);
        return new ResponseEntity<ClubNoticeDto>(clubNoticeDto,HttpStatus.OK);
    }

    @ApiOperation(
            value = "전체 동아리 공지 읽기 Api"
            , notes = "전체 동아리 공지 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readWholeNotice", method= RequestMethod.POST)
    public ResponseEntity<List> readClubNoticeList(int clubId) throws Exception{
        List<ClubNoticeDto> list=clubService.selectClubNoticeList(clubId);
        return new ResponseEntity<List>(list,HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 업데이트 Api"
            , notes = "동아리 공지 업데이트 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/updateNotice", method= RequestMethod.POST)
    public ResponseEntity<Void> updateClubNotice(@ModelAttribute ClubNoticeDto dto) throws Exception{
        clubService.updateClubNotice(dto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 삭제 Api"
            , notes = "동아리 공지 삭제 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/deleteNotice", method= RequestMethod.POST)
    public ResponseEntity<Void> deleteClubNotice(int noticeId) throws Exception{
        clubService.deleteClubNotice(noticeId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 좋아요 생성 Api"
            , notes = "동아리 공지 좋아요 생성 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/createNoticeLike", method= RequestMethod.POST)
    public ResponseEntity<Void> createClubNoticeLike(int userId,int noticeId) throws Exception{
        clubService.insertClubNoticeLike(userId,noticeId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 좋아요 삭제 Api"
            , notes = "동아리 공지 좋아요 삭제 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/deleteNoticeLike", method= RequestMethod.POST)
    public ResponseEntity<Void> deleteClubNoticeLike(int userId,int noticeId) throws Exception{
        clubService.deleteClubNoticeLike(userId,noticeId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 좋아요 갯수 Api"
            , notes = "동아리 공지 좋아요 갯수 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/countNoticeLike", method= RequestMethod.POST)
    public ResponseEntity<Integer> countClubNoticeLike(int noticeId) throws Exception{
        int cnt=clubService.selectClubNoticeCnt(noticeId);
        return new ResponseEntity<Integer>(cnt,HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 구독 생성 Api"
            , notes = "동아리 구독 생성 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/createSubs", method= RequestMethod.POST)
    public ResponseEntity<Void> createClubSubs(int userId,int clubId) throws Exception{
        clubService.insertClubSubs(userId,clubId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 구독 삭제 Api"
            , notes = "동아리 구독 삭제 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/deleteSubs", method= RequestMethod.POST)
    public ResponseEntity<Void> deleteClubSubs(int userId,int clubId) throws Exception{
        clubService.deleteClubSubs(userId,clubId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
