package sshj.sshj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/create", method= RequestMethod.POST)
    public ResponseEntity<Void> createClubNotice(@ModelAttribute ClubNoticeDto clubNoticeDto) throws Exception{
        clubService.insertClubNotice(clubNoticeDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 읽기 Api"
            , notes = "동아리 공지 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/read", method= RequestMethod.POST)
    public ResponseEntity<ClubNoticeDto> readClubNotice(int noticeId) throws Exception{
        ClubNoticeDto clubNoticeDto=clubService.selectClubNotice(noticeId);
        return new ResponseEntity<ClubNoticeDto>(clubNoticeDto,HttpStatus.OK);
    }

    @ApiOperation(
            value = "전체 동아리 공지 읽기 Api"
            , notes = "전체 동아리 공지 읽기 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/readWhole", method= RequestMethod.POST)
    public ResponseEntity<List> readClubNoticeList(int clubId) throws Exception{
        List<ClubNoticeDto> list=clubService.selectClubNoticeList(clubId);
        return new ResponseEntity<List>(list,HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 업데이트 Api"
            , notes = "동아리 공지 업데이트 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/update", method= RequestMethod.POST)
    public ResponseEntity<Void> updateClubNotice(@ModelAttribute ClubNoticeDto dto) throws Exception{
        clubService.updateClubNotice(dto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "동아리 공지 삭제 Api"
            , notes = "동아리 공지 삭제 Api"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="")
    })
    @RequestMapping(value = "/delete", method= RequestMethod.POST)
    public ResponseEntity<Void> deleteClubNotice(int noticeId) throws Exception{
        clubService.deleteClubNotice(noticeId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
