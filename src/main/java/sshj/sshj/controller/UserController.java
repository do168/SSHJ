package sshj.sshj.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import sshj.sshj.configuration.JwtTokenProvider;
import sshj.sshj.dto.CodeInfoModel;
import sshj.sshj.dto.UserDto;
import sshj.sshj.dto.UserInfoModel;
import sshj.sshj.service.UserService;

@RestController
@Api(value = "Oauth-controller", description = "Oauth controller")
@Slf4j
@RequestMapping("/member")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 생성할 아이디 정규식 체크 & 중복 체크
     * @param loginId
     * @return HttpStatus.Ok if 사용 가능
     *         else HttpStatus.BAD_REQUEST
     * @throws Exception
     */
    @ApiOperation(
            value = "아이디 중복 확인"
            , notes = "아이디 중복 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/idcheck", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> idCheck(
            @ApiParam(value = "입력 아이디", required = true) @RequestParam(name = "loginId", required = true) String loginId) throws Exception {

        if(!loginId.matches("^[a-zA-Z0-9]*$")) {
            log.info("아이디는 영문 혹은 숫자로만 가능합니다.");
            String msg = "아이디는 영문 혹은 숫자로만 가능합니다.";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        if (userService.selectUserLoginId(loginId) == 0) {
            log.info("사용 가능한 아이디입니다.");
            String msg = "사용 가능한 아이디입니다.";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }


        else {
            log.info("중복된 아이디입니다");
            String msg = "중복된 아이디입니다";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 생성할 닉네임 중복 체크
     * @param nickname
     * @return HttpStatus.Ok if 사용 가능
     *         else HttpStatus.BAD_REQUEST
     * @throws Exception
     */
    @ApiOperation(
            value = "닉네임 중복 확인"
            , notes = "닉네임 중복 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/nicknamecheck", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> nicknameCheck(
            @ApiParam(value = "입력 닉네임", required = true) @RequestParam(name = "nickname", required = true) String nickname) throws Exception {

        if (userService.selectUserNicknameIsOk(nickname) == 0) {
            log.info("사용 가능한 닉네임입니다.");
            String msg = "사용 가능한 닉네임입니다.";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } else {
            log.info("중복된 닉네임입니다");
            String msg = "중복된 닉네임입니다";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 입력한 이메일 중복 검사 후 인증코드 발신
     * @param email
     * @return true if 이메일 발신 성공
     *         else false
     * @throws Exception
     */
    @ApiOperation(
            value = "인증 이메일 발신"
            , notes = "인증 이메일 발신"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/sendEmail", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> sendEmail(
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "email", required = true) String email) throws Exception {
//        if (email.matches())
        if (userService.selectUserEmail(email) != null) {
            log.info(email+":  it is used email, please use other email");
            String msg = "이미 인증에 사용된 이메일입니다. 다른 이메일을 사용해주세요";

            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        try {
            userService.sendEmail(email);
            log.info("success");
            String msg = email+": 인증 이메일 발신 성공";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            String msg = email+": 인증 이메일 발신 실패";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 인증코드와 이메일을 비교하여 DB와 일치하는지 확인
     * @param code
     * @param email
     * @return true if 인증코드 일치
     *         else false
     * @throws Exception
     */
    @ApiOperation(
            value = "인증 확인"
            , notes = "인증 확인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup/certificate", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> certificate(
            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code", required = true) String code,
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email", required = true) String email) throws Exception {
        long now_time = Long.parseLong(userService.time_now());
        CodeInfoModel codeInfoModel = userService.selectCodeInfo(code);
        long created_time = Long.parseLong(codeInfoModel.getCreatedTime());

        if (codeInfoModel.getEmail().equals(email) && now_time - created_time <= 3000) {
            log.info("인증 성공");
            String msg = "인증 성공";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } else {
            log.info("인증 실패");
            String msg = "인증 실패";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 입력받은 정보들 DB에 저장, 이미 아아디가 존재하는지 한번 더 체크
     * @param userInfoModel
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "회원가입"
            , notes = "회원가입"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signup", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> signUp(
            @ModelAttribute final UserInfoModel userInfoModel) throws Exception {
        String loginId = userInfoModel.getLoginId();
        if(userService.selectUser(loginId)!=null) {
            String msg = "이미 존재하는 아이디입니다";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        if (!userService.selectCode(userInfoModel.getEmail()).equals(userInfoModel.getCode())) {
            log.info(userService.selectCode(userInfoModel.getEmail())+" "+userInfoModel.getCode());
            String msg = "인증코드가 일치하지 않습니다.";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        userService.insertUser(userInfoModel);
        String msg = "회원가입 성공";
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    /**
     * 입력받은 아이디와 비밀번호로 로그인, 성공 시 Map형태로 Access Token과 Refresh Token 리턴
     * @param loginId
     * @param password
     * @return Map {
     *              "accessToken" : access_token ,
     *              "refreshToken" : refresh_token
     *             }
     * @throws Exception
     */
    @ApiOperation(
            value = "로그인"
            , notes = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signin", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> singIn(
            @ApiParam(value = "아이디", required = true) @RequestParam(name = "loginId", required = true) String loginId,
            @ApiParam(value = "패스워드", required = true) @RequestParam(name = "password", required = true) String password) throws Exception {

        UserDto userDto = userService.selectUser(loginId);
        log.info("test");
        if (userDto == null || !passwordEncoder.matches(password, userDto.getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "id or password is not valid");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        Map<String, String> token = new HashMap<>();
        String access_token = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getNickname(), userDto.getRole());
//        String refresh_token = jwtTokenProvider.createRefreshToken();
        token.put("accessToken", access_token);
//        token.put("refreshToken", refresh_token);
        log.info(userDto.getUsername());
//        redisTemplate.opsForValue().set(userDto.getUsername(), refresh_token); // refresh_token은 따로 redis에 저장

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * 동아리사이트 전용 로그인 API
     * 입력받은 아이디와 비밀번호로 로그인, 성공 시 Map형태로 Access Token과 Refresh Token 리턴
     * @param loginId
     * @param password
     * @return Map {
     *              "accessToken" : access_token ,
     *              "refreshToken" : refresh_token
     *             }
     * @throws Exception
     */
    @ApiOperation(
            value ="로그인 for 동아리 사이트"
            , notes = "로그인 for 동아리 사이트"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/signInforClub", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> singInForClub(
            @ApiParam(value = "아이디", required = true) @RequestParam(name = "loginId", required = true) String loginId,
            @ApiParam(value = "패스워드", required = true) @RequestParam(name = "password", required = true) String password) throws Exception {

        UserDto userDto = userService.selectUser(loginId);
        log.info("test");
        if (userDto == null || !passwordEncoder.matches(password, userDto.getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "id or password is not valid");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (userDto.getRole().equals("ROLE_USER")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "ROLE_USER can't login this page");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        Map<String, String> token = new HashMap<>();
        String access_token = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getNickname(), userDto.getRole());
//        String refresh_token = jwtTokenProvider.createRefreshToken();
        token.put("accessToken", access_token);
//        token.put("refreshToken", refresh_token);
//        redisTemplate.opsForValue().set(userDto.getUsername(), refresh_token); // refresh_token은 따로 redis에 저장

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * 입력받은 refresh_token이 레디스에서 access_token에서 추출한 id를 키값으로 하는 refresh_token 존재 시 accessToken 재발급
     * @param accessToken  만료된 accessToken
//     * @param refreshToken 안전한 저장소에 저장된 refreshToken
     * @return      if (refreshToken이 우효할 시)
     *              Map {
     *                 "success" : true,
     *                 "accessToken" : newtok
     *              }
     *
     *              else if( refreshToken expired date를 지났을 시)
     *              Map {
     *                  "success" : false,
     *                  "msg" : "refresh token is expired."
     *              }
     *
     *              else (refreshToken이 존재하지 않을 시)
     *              Map {
     *                  "success" : false,
     *                  "msg" : "your refresh token does not exist."
     *              }
     */
    @ApiOperation(
            value = "토큰 재발급 요청"
            , notes = "토큰 재발급 요청"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/retoken", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> reToken(
            @ApiParam(value = "accessToken", required = true) @RequestParam(name = "accessToken", required = true) String accessToken
//            ,@ApiParam(value = "refreshToken", required = true) @RequestParam(name = "refreshToken", required = true) String refreshToken
    ) throws Exception {

        String newAccessToken = null;
//        String newRefreshToken = null;
//        String refreshTokenFromDb = null;
        String loginId = null;

        Map<String, Object> map = new HashMap<>();
        try {
            newAccessToken = accessToken;
//            newRefreshToken = refreshToken;
            log.info("access token in param: " + newAccessToken);
            try {
                loginId = jwtTokenProvider.getUserPk(newAccessToken);
            } catch (IllegalArgumentException e) {

            } catch (ExpiredJwtException e) { //expire됐을 때
                loginId = e.getClaims().getSubject();
                log.info("loginId from expired access token: " + loginId);
            }

//            if (refreshToken != null) { //refresh를 같이 보냈으면.
//                try {
//
//                    refreshTokenFromDb = (String) redisTemplate.opsForValue().get(loginId);
//                    log.info("rtfrom db: " + refreshTokenFromDb);
//                } catch (IllegalArgumentException e) {
//                    log.warn("illegal argument!!");
//                }
//                //둘이 일치하고 만료도 안됐으면 재발급 해주기.
//                if (newRefreshToken.equals(refreshTokenFromDb) && jwtTokenProvider.validateToken(newRefreshToken)) {
            UserDto userDto = jwtTokenProvider.getUserDto(newAccessToken);
            String newtok = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getNickname(), userDto.getRole());
            map.put("success", true);
            map.put("accessToken", newtok);
//                } else {
//                    map.put("success", false);
//                    map.put("msg", "refresh token is expired.");
//                }
//            } else { //refresh token이 없으면
//                map.put("success", false);
//                map.put("msg", "your refresh token does not exist.");
//            }

        } catch (Exception e) {
            throw e;
        }
//        log.info("m: " + m);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 로그아웃 시 받은 accessToken을 레디스 블랙리스트에 추가, 일치하는 refreshToken은 삭제
     * @param accessToken 만료된 accessToken
     * @return
     */
    @ApiOperation(
            value = "로그아웃"
            , notes = "로그아웃"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> logout(
            @ApiParam(value = "access_token", required = true) @RequestParam(name = "access_token", required = true) String accessToken
    ) throws Exception {

        String loginId = null;
        String at = accessToken;
        try {
            loginId = jwtTokenProvider.getUserPk(accessToken);
        } catch (IllegalArgumentException e) {
        } catch (ExpiredJwtException e) { //expire됐을 때
            loginId = e.getClaims().getSubject();
            log.info("loginId from expired access token: " + loginId);
        }

//        try {
//            if (redisTemplate.opsForValue().get(loginId) != null) {
//                //delete refresh token
//                redisTemplate.delete(loginId);
//            }
//        } catch (IllegalArgumentException e) {
//            log.warn("user does not exist");
//        }
        //accessToken은 30분 후에 블랙리스트에서 파기
        log.info(" logout ing : " + accessToken);
        redisTemplate.opsForValue().set(at, true);
        redisTemplate.expire(at, 30 * 60 * 1000L, TimeUnit.MILLISECONDS);

        return new ResponseEntity("로그아웃 성공", HttpStatus.OK);
    }

    /**
     * 아이디를 찾기 위해 다시 이메일 인증. 가입 시 입력한 이메일로 다시 인증코드 보내기.
     * @param email
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "아이디 찾기를 위해 이메일 인증 -> 이미 db에 저장돼있던 코드 업데이트"
            , notes = "아이디 찾기"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/sendEmail_searchId", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> sendEmail_searchId(
            @ApiParam(value = "email", required = true) @RequestParam(name = "email", required = true) String email
    ) throws Exception {

        UserDto userDto = userService.selectUserEmail(email);
        if (userDto == null) {
            return new ResponseEntity(email+": user does not exist", HttpStatus.BAD_REQUEST);
        } else {

            try {
                userService.sendEmail_findId(email);
                log.info(email+ " : success");
                String msg = email+": 인증 이메일 발신 성공";
                return new ResponseEntity<>(msg, HttpStatus.OK);
            } catch (Exception e) {
                log.info(e.toString());
                String msg = email+": 인증 이메일 발신 실패";
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * 인증코드 입력으로 아이디 찾기. 인증코드가 일치할 시 loginId 리턴
     * @param code
     * @param email
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "아이디 찾기"
            , notes = "아이디 찾기"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/getId", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> getId(
            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code", required = true) String code,
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email", required = true) String email
    ) throws Exception {

        long now_time = Long.parseLong(userService.time_now());
        CodeInfoModel codeInfoModel = userService.selectCodeInfo(code);
        long created_time = Long.parseLong(codeInfoModel.getCreatedTime());

        if (codeInfoModel.getEmail().equals(email) && now_time - created_time <= 3000) {
            log.info("인증 성공");
            return new ResponseEntity<>(userService.selectUserEmail(email).getLoginId(), HttpStatus.OK);
        } else {
            log.info("인증 실패");
            String msg = "인증 실패";
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 비밀번호 분실 시 기존 비밀번호를 알 수 없고 비밀번호 변경만 가능. 이메일과 loginId를 입력하여 변경 가능
     * @param email
     * @param loginId
     * @param newPw
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "비밀번호 변경"
            , notes = "비밀번호 변경"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/searchPw", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> searchPw(
            @ApiParam(value = "email", required = true) @RequestParam(name = "email", required = true) String email,
            @ApiParam(value = "loginId", required = true) @RequestParam(name = "loginId", required = true) String loginId,
            @ApiParam(value = "newpw", required = true) @RequestParam(name = "newqw", required = true) String newPw
    ) throws Exception {

        UserDto userDto = userService.selectUser(loginId);
        if (userDto == null || !userDto.getEmail().equals(email)) {
            return new ResponseEntity("user does not exist", HttpStatus.BAD_REQUEST);
        } else {

            if (userDto.getEmail().equals(email)) {
                try {
                    userService.updateUserPassword(loginId, passwordEncoder.encode(newPw));
                }
                catch (Exception e){
                    log.error(e.getMessage());
                }
                String msg = "비밀번호가 변경되었습니다.";
                return new ResponseEntity(msg, HttpStatus.OK);
            } else {
                String msg = "아이디와 이메일이 일치하지 않습니다";
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
        }
    }
}

