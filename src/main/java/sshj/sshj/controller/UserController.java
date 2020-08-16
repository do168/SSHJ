package sshj.sshj.controller;


import io.swagger.annotations.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jdk.internal.jline.internal.Log;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sshj.sshj.dto.UserInfoModel;
import sshj.sshj.service.UserService;

@RestController
@Api(value = "Oauth-controller", description = "Oauth controller")
@AllArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "회원가입"
            , notes = "회원가입"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Void> signUp(
            @ModelAttribute final UserInfoModel userInfoModel) throws Exception {
        userService.insertUser(userInfoModel);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
//


    @ApiOperation(
            value = "아이디 중복 확인"
            , notes = "아이디 중복 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/idcheck", method = RequestMethod.POST)
    public ResponseEntity<Boolean> idCheck(
            @ApiParam(value = "입력 아이디", required = true) @RequestParam(name = "id", required = true) String id) throws Exception {

        if (userService.selectUserId(id) != 0) {
            Log.info("사용 가능한 아이디입니다.");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            Log.info("중복된 아이디입니다");
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "인증 이메일 발신"
            , notes = "인증 이메일 발신"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/sendEmail", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendEmail(
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "email", required = true) String email) throws Exception {
        try {
            userService.sendEmail(email);
            Log.info("이메일 발신 성공");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (Exception e) {
            Log.error(e);
            Log.info("이메일 발신 실패");
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }

    @ApiOperation(
            value = "인증 확인"
            , notes = "인증 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/certificate", method = RequestMethod.POST)
    public ResponseEntity<Boolean> certificate(
            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code", required = true) String insert_code,
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email", required = true) String insert_email) throws Exception {
        if (userService.selectCode(insert_code).equals(insert_email)) {
            Log.info("인증 성공");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            Log.info("인증 실패");
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }


}
