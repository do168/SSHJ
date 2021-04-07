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
import sshj.sshj.model.Club;
import sshj.sshj.service.ClubService;
import sshj.sshj.service.ExpoPushService;
import sshj.sshj.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value="ClubController")
@RequestMapping("/clubs")
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
            value = "동아리 정보 읽기 Api"
            , notes = "동아리 정보 읽기 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @GetMapping(value = "/{id}", produces="text/plain;charset=UTF-8")
    public ResponseEntity<Club> getClub(
            @ApiParam(value = "id", required = true) @PathVariable(name = "id", required = true) long id
    ){
        return new ResponseEntity<Club>(clubService.find(id), HttpStatus.OK);
    }


    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 생성 Api"
            , notes = "동아리 생성 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @PostMapping(value = "/")
    public ResponseEntity<Club> createClub
            (@ApiParam(value = "createClubParam", required = true) @RequestBody Club createClubParam,
             @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) {
        Club createdClub = clubService.create(createClubParam);
        return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
    }


    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 수정 Api"
            , notes = "동아리 수정 Api"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/", method= RequestMethod.PUT)
    public ResponseEntity<Club> updateClub(@ApiParam(value = "updateClubParam", required = true) @RequestBody Club updateClubParam){
        Club updatedClub = clubService.update(updateClubParam);
        return new ResponseEntity<>(updatedClub, HttpStatus.OK);
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
    @RequestMapping(value = "/", method= RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteClub(@ApiParam(value = "id", required = true) @RequestParam(name = "id", required = true) long id) {
        clubService.delete(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    @ApiOperation(
            value = "전체 동아리 읽기 Api"
            , notes = "전체 동아리 읽기 Api"
            ,authorizations = {@Authorization (value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @GetMapping(value = "/")
    public ResponseEntity<List> getClubList(long ids){
        List<Club> list = clubService.getList(ids);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    // TODO: 유지할 지 없앨지 선택해야함
//    @ApiOperation(
//            value = "동아리 구독 생성 Api"
//            , notes = "동아리 구독 생성 Api"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/{id}/subsribe", method= RequestMethod.POST)
//    public ResponseEntity<Void> createClubSubs(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "clubId", required = true) long clubId,
//     @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
//        clubService.insertClubSubs(userHeaderModel.getUserId(),clubId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ApiOperation(
//            value = "동아리 구독 삭제 Api"
//            , notes = "동아리 구독 삭제 Api"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/{id}/deleteSubs", method= RequestMethod.DELETE)
//    public ResponseEntity<Void> deleteClubSubs(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) long clubId,
//                                               @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
//        clubService.deleteClubSubs(userHeaderModel.getUserId(),clubId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    // TODO: 유저 - 동아리 테이블 관계를 재정립하자
//    @ApiOperation(
//            value = "유저의 특정 동아리 구독 여부 Api"
//            , notes = "유저의 특정 동아리 구독 여부 Api"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/isSubClub", method= RequestMethod.GET)
//    public ResponseEntity<Boolean> isSubClub(@ApiParam(value = "클럽 id", required = true) @RequestParam(name = "club_id", required = true) long clubId,
//                                               @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
//        boolean isSubClub = clubService.selectIsSubClub(userHeaderModel.getUserId(), clubId);
//        return new ResponseEntity<>(isSubClub, HttpStatus.OK);
//    }
//
//    @ApiOperation(
//            value = "유저가 구독 중인 동아리 리스트"
//            , notes = "유저가 구독 중인 동아리 리스트"
//            ,authorizations = {@Authorization (value = "JWT")}
//    )
//    @ApiResponses(value={
//            @ApiResponse(code=200, message="")
//    })
//    @RequestMapping(value = "/SubClubList", method= RequestMethod.GET)
//    public ResponseEntity<List<Long>> SubClubList(@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception{
//        List<Long> clubList = clubService.selectSubClubList(userHeaderModel.getUserId());
//        return new ResponseEntity<>(clubList, HttpStatus.OK);
//    }




































}
