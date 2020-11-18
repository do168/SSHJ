package sshj.sshj.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.NoteDto;
import sshj.sshj.dto.UserHeaderModel;
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

    @ApiOperation(
            value = "쪽지 보내기"
            , notes = "쪽지 보내기"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendNote(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
            @ApiParam(value = "receiver", required = true) @RequestParam(name = "receiver", required = true) String receiver,
            @ApiParam(value = "msg", required = true) @RequestParam(name = "msg", required = true) String msg) throws Exception {

        try{
            noteService.executeSendMessage(userHeaderModel.getLoginId(), receiver, msg);
            log.info("발신 성공");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch(Exception e) {
            log.error("발신 실패 : \n"+ e.toString());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

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

        List<NoteDto> relatedId;

        try{
            relatedId = noteService.selectPersonList(userHeaderModel.getLoginId());
            log.info("쪽지함 성공");
            return new ResponseEntity<>(relatedId, HttpStatus.OK);
        } catch (Exception e){
            log.error("쪽지함 불러오기 실패 \n"+ e.toString());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "특정인과의 쪽지 내용 보기"
            , notes = "특정인과의 쪽지 내용 보기"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/room/{other}", method = RequestMethod.GET)
    public ResponseEntity<List> listMessages(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
            @ApiParam(value = "other", required = true) @RequestParam(name = "other", required = true) String other) throws Exception {

        List<NoteDto> messageList = new ArrayList<>();

        try{
            messageList = noteService.selectMessages(userHeaderModel.getLoginId(), other);
            log.info("쪽지 내용 보기 성공");
            return new ResponseEntity<>(messageList, HttpStatus.OK);
        } catch (Exception e){
            log.error("쪽지함 불러오기 실패 \n"+ e.toString());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
