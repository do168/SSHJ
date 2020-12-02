package sshj.sshj.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.ClubDescriptionDto;
import sshj.sshj.dto.ClubInfoDto;
import sshj.sshj.dto.ClubNoticeDto;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.mapper.S3FileMapper;
import sshj.sshj.service.ClubService;
import sshj.sshj.service.ExpoPushService;
import sshj.sshj.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            @ApiParam(value = "club_id", required = true) @RequestParam(name = "club_id", required = true) long clubId
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

    @ApiOperation(
            value = "동아리 컨텐츠 url 읽기 Api"
            , notes = "동아리 컨텐츠 url 읽기 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/selectClubContentsUrl", method= RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> selectClubContentsUrl(
            @ApiParam(value = "club_id", required = true) @RequestParam(name = "club_id", required = true) long clubId
    ) throws Exception{
        return new ResponseEntity<>(fileMapper.selectContentsUrl(clubId), HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 설명 생성 Api"
            , notes = "동아리 설명 생성 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/createDescription", method= RequestMethod.POST)
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
            @ApiParam(value = "club_id", required = true) @RequestParam(name = "club_id", required = true) long clubId
    ) throws Exception{
        return new ResponseEntity<>(clubService.selectClubDescription(clubId), HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 설명 수정 Api"
            , notes = "동아리 설명 수정 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/updateDescription", method= RequestMethod.PUT)
    public ResponseEntity<Void> updateClubDescription(@ApiParam(value = "클럽 description", required = true) @RequestParam(name = "description", required = true) String description,
                             @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        ClubDescriptionDto clubDescriptionDto = new ClubDescriptionDto(userHeaderModel.getUserId(), description);
        clubService.updateClubDescription(clubDescriptionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 설명 삭제 Api"
            , notes = "동아리 설명 삭제 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/deleteDescription", method= RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClubDescription(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel
    ) throws Exception{
        clubService.deleteClubDescription(userHeaderModel.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 공지 생성 Api"
            , notes = "동아리 공지 생성 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/createNotice", method= RequestMethod.POST)
    public ResponseEntity<Void> createClubNotice(@ModelAttribute ClubNoticeDto clubNoticeDto,
                                                 @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
    	clubNoticeDto.setClubId(userHeaderModel.getUserId());
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
    public ResponseEntity<ClubNoticeDto> readClubNotice(long noticeId) throws Exception{
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
    public ResponseEntity<List> readClubNoticeList(long clubId) throws Exception{
        List<ClubNoticeDto> list=clubService.selectClubNoticeList(clubId);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 공지 업데이트 Api"
            , notes = "동아리 공지 업데이트 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/updateNotice", method= RequestMethod.PUT)
    public ResponseEntity<Void> updateClubNotice(@ModelAttribute ClubNoticeDto dto) throws Exception{
        
    	log.info("[{}]",dto);
    	
    	clubService.updateClubNotice(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 공지 삭제 Api"
            , notes = "동아리 공지 삭제 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/byClub/deleteNotice", method= RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClubNotice(long noticeId) throws Exception{
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
    public ResponseEntity<Void> createClubNoticeLike(@ApiParam(value = "공지사항 id", required = true) @RequestParam(name = "notice_id", required = true) long noticeId,
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
    @RequestMapping(value = "/deleteNoticeLike", method= RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClubNoticeLike(@ApiParam(value = "공지사항 id", required = true) @RequestParam(name = "notice_id", required = true) long noticeId,
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
    @RequestMapping(value = "/countNoticeLike", method= RequestMethod.GET)
    public ResponseEntity<Long> countClubNoticeLike(long noticeId) throws Exception{
        long cnt=clubService.selectClubNoticeCnt(noticeId);
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
    public ResponseEntity<Void> createClubSubs(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) long clubId,
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
    @RequestMapping(value = "/deleteSubs", method= RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClubSubs(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) long clubId,
                                               @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        clubService.deleteClubSubs(userHeaderModel.getUserId(),clubId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "유저의 특정 동아리 구독 여부 Api"
            , notes = "유저의 특정 동아리 구독 여부 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/isSubClub", method= RequestMethod.GET)
    public ResponseEntity<Boolean> isSubClub(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) long clubId,
                                               @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        boolean isSubClub = clubService.selectIsSubClub(userHeaderModel.getUserId(), clubId);
        return new ResponseEntity<>(isSubClub, HttpStatus.OK);
    }

    @ApiOperation(
            value = "유저가 구독 중인 동아리 리스트"
            , notes = "유저가 구독 중인 동아리 리스트"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/SubClubList", method= RequestMethod.GET)
    public ResponseEntity<List<Long>> SubClubList(@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
        List<Long> clubList = clubService.selectSubClubList(userHeaderModel.getUserId());
        return new ResponseEntity<>(clubList, HttpStatus.OK);
    }

    @ApiOperation(
            value = "클럽 ID, 클럽 NAME 리스트"
            , notes = "클럽 ID, 클럽 NAME 리스트"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/ShowClubNameAndId", method= RequestMethod.GET, produces = "application/json; charset=utf8")
    public ResponseEntity<List<Map<String, String>>> ShowClubNameAndId() throws Exception{

        List<Map<String, String>> clubNameList = new ArrayList<>();
        for(ClubInfoDto cid : clubService.selectClubIdAndClubName()) {
            Map<String, String> mapInfo = new HashMap<>();
            mapInfo.put("club_name", cid.getClubName());
            mapInfo.put("club_id", Long.toString(cid.getClubId()));
            clubNameList.add(mapInfo);
        }
        return new ResponseEntity<>(clubNameList, HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 통합 api"
            , notes = "동아리 통합 api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/ClubPackage", method= RequestMethod.GET, produces = "application/json; charset=utf8")
    public ResponseEntity<Map<String, Object>> ClubPackage(@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
                                                           @ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) long clubId) throws Exception{

        Map<String, Object> clubPackage = new HashMap();

        String bucket = "profile";

        clubPackage.put("club_name", userService.selectUserNickname(clubId));
        clubPackage.put("club_description", clubService.selectClubDescription(clubId));
        clubPackage.put("club_profile_image", fileMapper.selectProfileImage(clubId, bucket));
        clubPackage.put("Is_user_subscribing_club", clubService.selectIsSubClub(userHeaderModel.getUserId(), clubId));
        clubPackage.put("NumSubscribe", clubService.selectClubSubscribeCnt(clubId));
        return new ResponseEntity<>(clubPackage, HttpStatus.OK);
    }



































}
