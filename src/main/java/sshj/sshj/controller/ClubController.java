package sshj.sshj.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.ClubDescriptionDto;
import sshj.sshj.dto.ClubNoticeDto;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.mapper.S3FileMapper;
import sshj.sshj.service.ClubService;
import sshj.sshj.service.ExpoPushService;
import sshj.sshj.service.UserService;

import java.util.List;

@Api(value="ClubController")
@RequestMapping("/club")
@RestController
public class ClubController {
    @Autowired
    private ClubService clubService;

    @Autowired
    private ExpoPushService expoPushService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3FileMapper fileMapper;

    @ApiOperation(
            value = "동아리 이름 읽기 Api"
            , notes = "동아리 이름 읽기 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/selectClubName", method= RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> selectClubName(
            @ApiParam(value = "club_id", required = true) @RequestParam(name = "club_id", required = true) int clubId
    ) throws Exception{
        return new ResponseEntity<>(userService.selectUserNickname(clubId), HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 프로필 사진 읽기 Api"
            , notes = "동아리 프로필 사진 읽기 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/selectClubProfileImage", method= RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> selectClubProfileImage(
            @ApiParam(value = "club_id", required = true) @RequestParam(name = "club_id", required = true) long clubId
    ) throws Exception{
        String bucket = "profile";
        return new ResponseEntity<>(fileMapper.selectProfileImage(clubId, bucket), HttpStatus.OK);
    }

    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 설명 생성 Api"
            , notes = "동아리 설명 생성 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/createDescription", method= RequestMethod.POST)
    public ResponseEntity<Void> createClubDescription
            (@ApiParam(value = "생성할 클럽 description", required = true) @RequestParam(name = "description", required = true) String description,
             @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        ClubDescriptionDto clubDescriptionDto = new ClubDescriptionDto(userHeaderModel.getUserId(), description);
        clubService.insertClubDescription(clubDescriptionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 설명 읽기 Api"
            , notes = "동아리 설명 읽기 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readDescription", method= RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> selectClubDescription(
            @ApiParam(value = "club_id", required = true) @RequestParam(name = "club_id", required = true) int clubId
    ) throws Exception{
        return new ResponseEntity<>(clubService.selectClubDescription(clubId), HttpStatus.OK);
    }

    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 설명 수정 Api"
            , notes = "동아리 설명 수정 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/updateDescription", method= RequestMethod.POST)
    public ResponseEntity<Void> updateClubDescription(@ApiParam(value = "클럽 description", required = true) @RequestParam(name = "description", required = true) String description,
                             @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        ClubDescriptionDto clubDescriptionDto = new ClubDescriptionDto(userHeaderModel.getUserId(), description);
        clubService.updateClubDescription(clubDescriptionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 설명 삭제 Api"
            , notes = "동아리 설명 삭제 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/deleteDescription", method= RequestMethod.POST)
    public ResponseEntity<Void> deleteClubDescription(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel
    ) throws Exception{
        clubService.deleteClubDescription(userHeaderModel.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 공지 생성 Api"
            , notes = "동아리 공지 생성 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/createNotice", method= RequestMethod.POST)
    public ResponseEntity<Void> createClubNotice(@ModelAttribute ClubNoticeDto clubNoticeDto,
                                                 @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        clubService.insertClubNotice(clubNoticeDto);
        // 동아리 공지 생성 시 구독한 유저들에게 푸시알림
        expoPushService.sendingPushClubNoticeCreated(userHeaderModel.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 읽기 Api"
            , notes = "동아리 공지 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readNotice", method= RequestMethod.GET, produces="text/plain;charset=UTF-8")
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
    @RequestMapping(value = "/readWholeNotice", method= RequestMethod.GET)
    public ResponseEntity<List> readClubNoticeList(int clubId) throws Exception{
        List<ClubNoticeDto> list=clubService.selectClubNoticeList(clubId);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
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
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<Void> createClubNoticeLike(@ApiParam(value = "공지사항 id", required = true) @RequestParam(name = "notice_id", required = true) int noticeId,
                                                     @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        clubService.insertClubNoticeLike(userHeaderModel.getUserId(),noticeId);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<Void> deleteClubNoticeLike(@ApiParam(value = "공지사항 id", required = true) @RequestParam(name = "notice_id", required = true) int noticeId,
                                                     @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        clubService.deleteClubNoticeLike(userHeaderModel.getUserId(),noticeId);
        return new ResponseEntity<>(HttpStatus.OK);
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
        return new ResponseEntity<>(cnt,HttpStatus.OK);
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
    public ResponseEntity<Void> createClubSubs(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) int clubId,
     @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        clubService.insertClubSubs(userHeaderModel.getUserId(),clubId);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<Void> deleteClubSubs(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) int clubId,
                                               @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        clubService.deleteClubSubs(userHeaderModel.getUserId(),clubId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
