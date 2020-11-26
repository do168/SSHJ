package sshj.sshj.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.component.SSHZUtil;
import sshj.sshj.configuration.JwtTokenProvider;
import sshj.sshj.dto.*;
import sshj.sshj.service.UserService;

@RestController
@Api(value = "Oauth-controller")
@Slf4j
@RequestMapping("/member")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 생성할 아이디 정규식 체크 & 중복 체크
     * @param loginId
     * @return HttpStatus.Ok if 사용 가능
     *         else HttpStatus.BAD_REQUEST
     * @throws Exception
     */
//    @ApiOperation(
//            value = "아이디 중복 확인"
//            , notes = "아이디 중복 확인"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "complete")
//    })
//    @RequestMapping(value = "/signup/idcheck", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
//    public ResponseEntity<String> idCheck(
//            @ApiParam(value = "입력 아이디", required = true) @RequestParam(name = "loginId", required = true) String loginId) throws Exception {
//
//        if(!sshzUtil.isLoginReg(loginId)) {
//            String msg = "아이디는 영문 혹은 숫자로만 가능합니다.";
//            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
//        }
//
//        if (userService.selectUserLoginId(loginId) == 0) {
//            log.info("사용 가능한 아이디입니다.");
//            String msg = "사용 가능한 아이디입니다.";
//            return new ResponseEntity<>(msg, HttpStatus.OK);
//        }
//
//
//        else {
//            log.info("중복된 아이디입니다");
//            String msg = "중복된 아이디입니다";
//            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
//        }
//    }

    /**
     * 생성할 닉네임 중복 체크
     *
     * @param nickname 닉네임
     * @return HttpStatus.Ok if 사용 가능
     * else HttpStatus.BAD_REQUEST
     */
    @ApiOperation(
            value = "닉네임 중복 확인"
            , notes = "닉네임 중복 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/nicknamecheck", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> nicknameCheck(
            @ApiParam(value = "입력 닉네임", required = true) @RequestParam(name = "nickname") String nickname) {

        ServiceResultModel result = userService.selectUserNicknameIsOk(nickname);

        // 생성 가능한 경우
        if (result.getFlag()) {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 생성 불가능한 경우
        else {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 입력한 이메일 중복 검사 후 인증코드 발신
     *
     * @param email 이메일
     * @return true if 이메일 발신 성공
     * else false
     */
    @ApiOperation(
            value = "인증 이메일 발신"
            , notes = "인증 이메일 발신"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/sendEmail", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> sendEmail(
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "email") String email) {

        ServiceResultModel result = userService.excuteSendEmail(email);

        // 발신 성공
        if (result.getFlag()) {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 발신 실패
        else {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 인증코드와 이메일을 비교하여 DB와 일치하는지 확인
     *
     * @param code  인증코드
     * @param email 이메일
     * @return true if 인증코드 일치
     * else false
     */
    @ApiOperation(
            value = "인증 확인"
            , notes = "인증 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/certificate", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> certificate(
            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code") String code,
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email") String email) {

        ServiceResultModel result = userService.executeValidateCode(code, email);

        // 인증 성공
        if (result.getFlag()) {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 인증 실패
        else {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 입력받은 정보들 DB에 저장, 이미 아아디가 존재하는지 한번 더 체크
     *
     * @param userInfoModel 토큰으로 받아온 유저정보
     * @return OK if 회원가입 성공, else BAD_REQUEST
     */
    @ApiOperation(
            value = "회원가입"
            , notes = "회원가입"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> signUp(
            @ModelAttribute final UserInfoModel userInfoModel) {

        ServiceResultModel result = userService.executeSignUp(userInfoModel);

        // 정상적으로 회원가입 된 경우
        if (result.getFlag()) {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 회원가입 로직에서 오류가 생긴 부분
        else {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 입력받은 아이디와 비밀번호로 로그인, 성공 시 Map형태로 Access Token과 Refresh Token 리턴
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return Map {
     * "accessToken" : access_token ,
     * "refreshToken" : refresh_token
     * }
     */
    @ApiOperation(
            value = "로그인"
            , notes = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> signIn(
            @ApiParam(value = "이메일", required = true) @RequestParam(name = "email") String email,
            @ApiParam(value = "패스워드", required = true) @RequestParam(name = "password") String password) {

        ServiceResultModel result = userService.excuteLogIn(email, password);
        Map<String, String> token = new HashMap<>();

        // 로그인 성공 시 Access Token 리턴
        if (result.getFlag()) {
            token.put("accessToken", result.getMsg());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        // 로그인 실패 시 에러 메세지 리턴
        else {
            token.put("error", result.getMsg());
            return new ResponseEntity<>(token, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 동아리사이트 전용 로그인 API
     * 입력받은 아이디와 비밀번호로 로그인, 성공 시 Map형태로 Access Token과 Refresh Token 리턴
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return Map {
     * "accessToken" : access_token
     * }
     */
    @ApiOperation(
            value = "로그인 for 동아리 사이트"
            , notes = "로그인 for 동아리 사이트"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signInforClub", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> singInForClub(
            @ApiParam(value = "이메일", required = true) @RequestParam(name = "email") String email,
            @ApiParam(value = "패스워드", required = true) @RequestParam(name = "password") String password) {

        ServiceResultModel result = userService.excuteLogInForClub(email, password);
        Map<String, String> tokenForClub = new HashMap<>();

        // 동어리 로그인 성공 시 Access Token 리턴
        if (result.getFlag()) {
            tokenForClub.put("accessToken", result.getMsg());
            return new ResponseEntity<>(tokenForClub, HttpStatus.OK);
        }
        // 동아리 로그인 실패 시 에러 메세지 리턴
        else {
            tokenForClub.put("error", result.getMsg());
            return new ResponseEntity<>(tokenForClub, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 입력받은 refresh_token이 레디스에서 access_token에서 추출한 id를 키값으로 하는 refresh_token 존재 시 accessToken 재발급
     *
     * @param accessToken 만료된 accessToken
     *                    //     * @param refreshToken 안전한 저장소에 저장된 refreshToken
     * @return if (refreshToken이 우효할 시)
     * Map {
     * "success" : true,
     * "accessToken" : newtok
     * }
     * <p>
     * else if( refreshToken expired date를 지났을 시)
     * Map {
     * "success" : false,
     * "msg" : "refresh token is expired."
     * }
     * <p>
     * else (refreshToken이 존재하지 않을 시)
     * Map {
     * "success" : false,
     * "msg" : "your refresh token does not exist."
     * }
     */
    @ApiOperation(
            value = "토큰 재발급 요청"
            , notes = "토큰 재발급 요청"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/retoken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> reToken(
            @ApiParam(value = "accessToken", required = true) @RequestParam(name = "accessToken") String accessToken
//            ,@ApiParam(value = "refreshToken", required = true) @RequestParam(name = "refreshToken", required = true) String refreshToken
    ) {

        // 토큰 유효성 검사 및 재발급 수행
        ServiceResultModel result = userService.excuteRetoken(accessToken);
        Map<String, Object> newToken = new HashMap<>();

        // 토큰 재발급 성공
        if (result.getFlag()) {
            newToken.put("accessToken", result.getMsg());
            return new ResponseEntity<>(newToken, HttpStatus.OK);
        }
        // 토큰 재발급 실패
        else {
            newToken.put("error", result.getMsg());
            return new ResponseEntity<>(newToken, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 로그아웃 시 받은 accessToken을 레디스 블랙리스트에 추가, 일치하는 refreshToken은 삭제
     *
     * @param accessToken 만료된 accessToken
     * @return newToken
     */
    @ApiOperation(
            value = "로그아웃"
            , notes = "로그아웃"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> logout(
            @ApiParam(value = "access_token", required = true) @RequestParam(name = "access_token") String accessToken
    ) {

        ServiceResultModel result = userService.excuteLogout(accessToken);

        // 인증 성공
        if (result.getFlag()) {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 인증 실패
        else {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 아이디를 찾기 위해 다시 이메일 인증. 가입 시 입력한 이메일로 다시 인증코드 보내기.
     *
     * @param email 이메일
     * @return OK if success, else BAD_REQUEST
     */
    @ApiOperation(
            value = "아이디 찾기를 위해 이메일 인증 -> 이미 db에 저장돼있던 코드 업데이트"
            , notes = "아이디 찾기"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/sendEmail_searchId", method = RequestMethod.PATCH, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> sendEmail_searchId(
            @ApiParam(value = "email", required = true) @RequestParam(name = "email") String email
    ) {

        UserDto userDto = userService.selectUserEmail(email);
        if (userDto == null) {
            String msg = "user does not exist";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        } else {

            if (userService.sendEmail(email)) {
                String msg = email + ": 인증 이메일 발신 성공";
                return new ResponseEntity<>(msg, HttpStatus.OK);
            } else {
                String msg = email + ": 인증 이메일 발신 실패";
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * 인증코드 입력으로 아이디 찾기. 인증코드가 일치할 시 loginId 리턴
     * @param code 인증코드
     * @param email 이메일
     * @return OK if Success, else BAD_REQUEST
     * @throws Exception
     */
//    @ApiOperation(
//            value = "아이디 찾기"
//            , notes = "아이디 찾기"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "complete")
//    })
//    @RequestMapping(value = "/getId", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
//    public ResponseEntity<String> getId(
//            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code") String code,
//            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email") String email
//    ) throws Exception {
//
//        long now_time = Long.parseLong(userService.time_now());
//        CodeInfoModel codeInfoModel = userService.selectCodeInfo(code);
//        long created_time = Long.parseLong(codeInfoModel.getCreatedTime());
//
//        if (codeInfoModel.getEmail().equals(email) && sshzUtil.isValidCode(now_time, created_time)) {
//            log.info("인증 성공");
//            return new ResponseEntity<>(userService.selectUserEmail(email).getLoginId(), HttpStatus.OK);
//        } else {
//            log.info("인증 실패");
//            String msg = "인증 실패";
//            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
//        }
//    }

    /**
     * 비밀번호 분실 시 기존 비밀번호를 알 수 없고 비밀번호 변경만 가능. 이메일과 loginId를 입력하여 변경 가능
     *
     * @param email 이메일
     * @param newPw 새 비밀번호
     * @return OK if true, else BAD_REQUEST
     */
    @ApiOperation(
            value = "비밀번호 변경"
            , notes = "비밀번호 변경"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/searchPw", method = RequestMethod.PATCH, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> searchPw(
            @ApiParam(value = "email", required = true) @RequestParam(name = "email") String email,
            @ApiParam(value = "newpw", required = true) @RequestParam(name = "newqw") String newPw
    ) {

        UserDto userDto = userService.loadUserByUsername(email);

        // 유저가 존재하지 않는 경우
        if (userDto == null) {
            return new ResponseEntity<>("user does not exist", HttpStatus.BAD_REQUEST);
        } else {

            userService.updateUserPassword(email, passwordEncoder.encode(newPw));
            String msg = "비밀번호가 변경되었습니다.";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
    }

    /**
     * @param userHeaderModel 유저 토큰
     * @return OK if true, else BAD_REQUEST
     */
    @ApiOperation(
            value = "회원 탈퇴"
            , notes = "회원 탈퇴"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/searchPw", method = RequestMethod.DELETE, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> withdrawalId(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) {

        ServiceResultModel result = userService.deleteUserId(userHeaderModel);

        // 정상적으로 회원탈퇴 된 경우
        if (result.getFlag()) {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 회원탈퇴 로직에서 오류가 생긴 부분
        else {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }
    }
}

