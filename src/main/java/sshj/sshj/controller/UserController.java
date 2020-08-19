package sshj.sshj.controller;


import io.swagger.annotations.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sshj.sshj.configuration.JwtTokenProvider;
import sshj.sshj.dto.CodeInfoModel;
import sshj.sshj.dto.UserDto;
import sshj.sshj.dto.UserInfoModel;
import sshj.sshj.service.UserService;

@RestController
@Api(value = "Oauth-controller", description = "Oauth controller")
@AllArgsConstructor
@Slf4j
@RequestMapping("/member")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @ApiOperation(
            value = "아이디 중복 확인"
            , notes = "아이디 중복 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/idcheck", method = RequestMethod.GET)
    public ResponseEntity<Boolean> idCheck(
            @ApiParam(value = "입력 아이디", required = true) @RequestParam(name = "id", required = true) String id) throws Exception {

        if (userService.selectUserId(id) == 0) {
            log.info("사용 가능한 아이디입니다.");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            log.info("중복된 아이디입니다");
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "닉네임 중복 확인"
            , notes = "닉네임 중복 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/nicknamecheck", method = RequestMethod.GET)
    public ResponseEntity<Boolean> nicknameCheck(
            @ApiParam(value = "입력 닉네임", required = true) @RequestParam(name = "nickname", required = true) String nickname) throws Exception {

        if (userService.selectUserNickname(nickname) == 0) {
            log.info("사용 가능한 닉네임입니다.");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            log.info("중복된 닉네임입니다");
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
    @RequestMapping(value = "/signup/sendEmail", method = RequestMethod.GET)
    public ResponseEntity<Boolean> sendEmail(
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "email", required = true) String email) throws Exception {
        if(userService.selectUserEmail(email)==1) {
            log.info("이미 인증된 이메일입니다. 다른 이메일을 입력해주세요");
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }

        try {
            userService.sendEmail(email);
            log.info("이메일 발신 성공");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (Exception e) {
            log.info("이메일 발신 실패");
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
    @RequestMapping(value = "/signup/certificate", method = RequestMethod.GET)
    public ResponseEntity<Boolean> certificate(
            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code", required = true) String insert_code,
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email", required = true) String insert_email) throws Exception {
        long now_time = Long.parseLong(userService.time_now());
        CodeInfoModel codeInfoModel = userService.selectCode(insert_code);
        long created_time = Long.parseLong(codeInfoModel.getCreated_time());

        if (codeInfoModel.getEmail().equals(insert_email) && now_time-created_time<=3000) {
            log.info("인증 성공");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            log.info("인증 실패");
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }

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

    @ApiOperation(
            value = "로그인"
            , notes = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ResponseEntity<String> singIn(
        @ApiParam(value = "아이디", required = true) @RequestParam(name = "id", required = true) String id,
        @ApiParam(value = "패스워드", required = true) @RequestParam(name="password", required = true) String password) throws Exception {


        UserDto userDto = userService.selectUser(id);
        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return new ResponseEntity<>(jwtTokenProvider.createToken(userDto.getUsername(), userDto.getRole()), HttpStatus.OK);
    }

}
