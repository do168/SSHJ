package sshj.sshj.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.NoteDto;
import sshj.sshj.dto.ServiceResultModel;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.service.ExpoPushServiceImpl;
import sshj.sshj.service.NoteService;
import sshj.sshj.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Api(value = "Note-Contoller", description = "Note-Controller")
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private ExpoPushServiceImpl expoPushService;

    @ApiOperation(
            value = "쪽지 보내기"
            , notes = "쪽지 보내기"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<String> sendNote(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
            @ApiParam(value = "receiver", required = true) @RequestParam(name = "receiver", required = true) long receiver,
            @ApiParam(value = "msg", required = true) @RequestParam(name = "msg", required = true) String msg) throws Exception {

        // 쪽지 보내기
        noteService.insertSendMessage(userHeaderModel.getUserId(), receiver, msg);
        ServiceResultModel result = expoPushService.excuteSendingPushNoteReceived(userHeaderModel.getUserId(), receiver, msg);
        // 보내기 성공
            if (result.getFlag()) {
                return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 보내기 실패
            else {
                return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }
    }

    
    @ApiOperation(
            value = "쪽지함 보기"
            , notes = "쪽지함 보기"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/listMessages", method = RequestMethod.GET)
    public ResponseEntity<List<NoteDto>> listPerson(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception {

        List<NoteDto> relatedId = noteService.selectPersonList(userHeaderModel.getUserId());
        // 자기 자신에게 브로드캐스팅 -> 알림 표시 없애기
        ServiceResultModel result = expoPushService.excuteSendingPushNoteReceived(userHeaderModel.getUserId(), userHeaderModel.getUserId(), "쪽지알림 없애기");
        if (result.getFlag()) {
            return new ResponseEntity<>(relatedId, HttpStatus.OK);
        }
        // 알림 보내기 실패
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
//  TODO 읽은 것, 안 읽은 것 따로 처리해야 한다.
    @ApiOperation(
            value = "특정인과의 쪽지 내용 보기"
            , notes = "특정인과의 쪽지 내용 보기"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/note/{other}", method = RequestMethod.GET)
    public ResponseEntity<List> listMessages(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
            @ApiParam(value = "other", required = true) @RequestParam(name = "other", required = true) @PathVariable long other) throws Exception {

        List<NoteDto> messageList = noteService.selectMessages(userHeaderModel.getUserId(), other);
        if (messageList != null) {
            return new ResponseEntity<>(messageList, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }


}
