package sshj.sshj.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sshj.sshj.mapper.S3FileMapper;
import sshj.sshj.model.Club;
import sshj.sshj.service.ClubService;
import sshj.sshj.service.ExpoPushService;
import sshj.sshj.service.UserService;

import java.util.List;

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
//            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Club> getClub(
            @ApiParam(value = "id", required = true) @PathVariable(name = "id") long id
    ){
        Club club = clubService.find(id);
        return new ResponseEntity<>(club, HttpStatus.OK);
    }


//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 생성 Api"
            , notes = "동아리 생성 Api"
//            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @PostMapping(value = "/")
    public ResponseEntity<Club> createClub
            (@ApiParam(value = "createClubParam", required = true) @RequestBody Club createClubParam) {
        Club createdClub = clubService.create(createClubParam);
        return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
    }


//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 수정 Api"
            , notes = "동아리 수정 Api"
//            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/", method= RequestMethod.PUT)
    public ResponseEntity<Club> updateClub(@ApiParam(value = "updateClubParam", required = true) @RequestBody Club updateClubParam,
                                           @ApiParam(value = "club_id", required = true) @RequestBody long id){
        Club updatedClub = clubService.update(updateClubParam, id);
        return new ResponseEntity<>(updatedClub, HttpStatus.OK);
    }

//    @Secured({"ROLE_CLUB", "ROLE_ADMIN"})
    @ApiOperation(
            value = "동아리 삭제 Api"
            , notes = "동아리 삭제 Api"
//            ,authorizations = {@Authorization(value = "JWT")}
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
        List<Club> list = clubService.findAll();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }




































}
