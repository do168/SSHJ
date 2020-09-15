package sshj.sshj.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
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
    
    @Value("${spring.profiles.active}")
	private String activeProfile;

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
    @RequestMapping(value = "/signup/idcheck", method = RequestMethod.GET)
    public ResponseEntity<Boolean> idCheck(
            @ApiParam(value = "입력 아이디", required = true) @RequestParam(name = "loginId", required = true) String loginId) throws Exception {

        if(!loginId.matches("^[a-zA-Z0-9]*$")) {
            log.info("아이디는 영문 혹은 숫자로만 가능합니다.");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        if (userService.selectUserLoginId(loginId) == 0) {
            log.info("사용 가능한 아이디입니다.");
            return new ResponseEntity<>(true, HttpStatus.OK);
        }


        else {
            log.info("중복된 아이디입니다");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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
    @RequestMapping(value = "/signup/nicknamecheck", method = RequestMethod.GET)
    public ResponseEntity<Boolean> nicknameCheck(
            @ApiParam(value = "입력 닉네임", required = true) @RequestParam(name = "nickname", required = true) String nickname) throws Exception {

        if (userService.selectUserNickname(nickname) == 0) {
            log.info("사용 가능한 닉네임입니다.");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("중복된 닉네임입니다");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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
    @RequestMapping(value = "/signup/sendEmail", method = RequestMethod.GET)
    public ResponseEntity<Boolean> sendEmail(
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "email", required = true) String email) throws Exception {

        if (userService.selectUserEmail(email) != null) {
            log.info("it is used email, please use other email");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        try {
            userService.sendEmail(email);
            log.info("success");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            log.info("fail");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 인증코드와 이메일을 비교하여 DB와 일치하는지 확인
     * @param insert_code
     * @param insert_email
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
    @RequestMapping(value = "/signup/certificate", method = RequestMethod.GET)
    public ResponseEntity<Boolean> certificate(
            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code", required = true) String insert_code,
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email", required = true) String insert_email) throws Exception {
        long now_time = Long.parseLong(userService.time_now());
        CodeInfoModel codeInfoModel = userService.selectCode(insert_code);
        long created_time = Long.parseLong(codeInfoModel.getCreatedTime());

        if (codeInfoModel.getEmail().equals(insert_email) && now_time - created_time <= 3000) {
            log.info("인증 성공");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("인증 실패");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Void> signUp(
            @ModelAttribute final UserInfoModel userInfoModel) throws Exception {
        String loginId = userInfoModel.getLoginId();
        if(userService.selectUser(loginId)!=null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.insertUser(userInfoModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 입력받은 아이디와 비밀번호로 로그인, 성공 시 Map형태로 Access Token과 Refres Token 리턴
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
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> singIn(
            @ApiParam(value = "아이디", required = true) @RequestParam(name = "loginId", required = true) String loginId,
            @ApiParam(value = "패스워드", required = true) @RequestParam(name = "password", required = true) String password) throws Exception {

        UserDto userDto = userService.selectUser(loginId);
        if (userDto == null || !passwordEncoder.matches(password, userDto.getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "id or password is not valid");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        Map<String, String> token = new HashMap<>();
        String access_token = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getNickname(), userDto.getRole());
        String refresh_token = jwtTokenProvider.createRefreshToken();
        token.put("accessToken", access_token);
        token.put("refreshToken", refresh_token);
        log.info(userDto.getUsername());
//        if("dev".equals(activeProfile))
        	redisTemplate.opsForValue().set(userDto.getUsername(), refresh_token); // refresh_token은 따로 redis에 저장
        
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * 입력받은 refresh_token이 레디스에서 access_token에서 추출한 id를 키값으로 하는 refresh_token 존재 시 accessToken 재발급
     * @param access_token  만료된 accessToken
     * @param refresh_token 안전한 저장소에 저장된 refreshToken
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
    @RequestMapping(value = "/retoken", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> reToken(
            @ApiParam(value = "access_token", required = true) @RequestParam(name = "access_token", required = true) String access_token,
            @ApiParam(value = "refresh_token", required = true) @RequestParam(name = "refresh_token", required = true) String refresh_token
    ) throws Exception {

        String accessToken = null;
        String refreshToken = null;
        String refreshTokenFromDb = null;
        String loginId = null;

        Map<String, Object> map = new HashMap<>();
        try {
            accessToken = access_token;
            refreshToken = refresh_token;
            log.info("access token in param: " + accessToken);
            try {
                loginId = jwtTokenProvider.getUserPk(accessToken);
            } catch (IllegalArgumentException e) {

            } catch (ExpiredJwtException e) { //expire됐을 때
                loginId = e.getClaims().getSubject();
                log.info("loginId from expired access token: " + loginId);
            }

            if (refreshToken != null) { //refresh를 같이 보냈으면.
                try {

                    refreshTokenFromDb = (String) redisTemplate.opsForValue().get(loginId);
                    log.info("rtfrom db: " + refreshTokenFromDb);
                } catch (IllegalArgumentException e) {
                    log.warn("illegal argument!!");
                }
                //둘이 일치하고 만료도 안됐으면 재발급 해주기.
                if (refreshToken.equals(refreshTokenFromDb) && jwtTokenProvider.validateToken(refreshToken)) {
                    UserDto userDto = jwtTokenProvider.getUserDto(accessToken);

                    String newtok = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getNickname(), userDto.getRole());
                    map.put("success", true);
                    map.put("accessToken", newtok);
                } else {
                    map.put("success", false);
                    map.put("msg", "refresh token is expired.");
                }
            } else { //refresh token이 없으면
                map.put("success", false);
                map.put("msg", "your refresh token does not exist.");
            }

        } catch (Exception e) {
            throw e;
        }
//        log.info("m: " + m);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 로그아웃 시 받은 accessToken을 레디스 블랙리스트에 추가, 일치하는 refreshToken은 삭제
     * @param access_token 만료된 accessToken
     * @return
     */
    @ApiOperation(
            value = "로그아웃"
            , notes = "로그아웃"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<Void> logout(
            @ApiParam(value = "access_token", required = true) @RequestParam(name = "access_token", required = true) String access_token
    ) throws Exception {

        String loginId = null;
        String accessToken = access_token;
        try {
            loginId = jwtTokenProvider.getUserPk(accessToken);
        } catch (IllegalArgumentException e) {
        } catch (ExpiredJwtException e) { //expire됐을 때
            loginId = e.getClaims().getSubject();
            log.info("loginId from expired access token: " + loginId);
        }

        try {
            if (redisTemplate.opsForValue().get(loginId) != null) {
                //delete refresh token
                redisTemplate.delete(loginId);
            }
        } catch (IllegalArgumentException e) {
            log.warn("user does not exist");
        }
        //accessToken은 30분 후에 블랙리스트에서 파기
        log.info(" logout ing : " + accessToken);
        redisTemplate.opsForValue().set(accessToken, true);
        redisTemplate.expire(accessToken, 30 * 60 * 1000L, TimeUnit.MILLISECONDS);

        return new ResponseEntity(HttpStatus.OK);
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
    @RequestMapping(value = "/sendEmail_searchId", method = RequestMethod.GET)
    public ResponseEntity<Boolean> sendEmail_searchId(
            @ApiParam(value = "email", required = true) @RequestParam(name = "email", required = true) String email
    ) throws Exception {

        UserDto userDto = userService.selectUserEmail(email);
        if (userDto == null) {
            return new ResponseEntity("user does not exist", HttpStatus.BAD_REQUEST);
        } else {

            try {
                userService.sendEmail(email);
                log.info("success");
                return new ResponseEntity<>(true, HttpStatus.OK);
            } catch (Exception e) {
                log.info("fail");
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * 인증코드 입력으로 아이디 찾기. 인증코드가 일치할 시 loginId 리턴
     * @param insert_code
     * @param insert_email
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
    @RequestMapping(value = "/getId", method = RequestMethod.GET)
    public ResponseEntity<String> getId(
            @ApiParam(value = "입력 코드", required = true) @RequestParam(name = "insert_code", required = true) String insert_code,
            @ApiParam(value = "입력 이메일", required = true) @RequestParam(name = "insert_email", required = true) String insert_email
    ) throws Exception {

        long now_time = Long.parseLong(userService.time_now());
        CodeInfoModel codeInfoModel = userService.selectCode(insert_code);
        long created_time = Long.parseLong(codeInfoModel.getCreatedTime());

        if (codeInfoModel.getEmail().equals(insert_email) && now_time - created_time <= 3000) {
            log.info("인증 성공");
            return new ResponseEntity<>(userService.selectUserEmail(insert_email).getLoginId(), HttpStatus.OK);
        } else {
            log.info("인증 실패");
            return new ResponseEntity<>("인증 실패", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 비밀번호 분실 시 기존 비밀번호를 알 수 없고 비밀번호 변경만 가능. 이메일과 loginId를 입력하여 변경 가능
     * @param email
     * @param loginId
     * @param newpw
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
    @RequestMapping(value = "/searchPw", method = RequestMethod.GET)
    public ResponseEntity<String> searchPw(
            @ApiParam(value = "email", required = true) @RequestParam(name = "email", required = true) String email,
            @ApiParam(value = "loginId", required = true) @RequestParam(name = "loginId", required = true) String loginId,
            @ApiParam(value = "newpw", required = true) @RequestParam(name = "newqw", required = true) String newpw
    ) throws Exception {

        UserDto userDto = userService.selectUser(loginId);
        if (userDto == null || !userDto.getEmail().equals(email)) {
            return new ResponseEntity("user does not exist", HttpStatus.BAD_REQUEST);
        } else {

            if (userDto.getEmail().equals(email)) {
                try {
                    userService.updateUserPassword(loginId, passwordEncoder.encode(newpw));
                }
                catch (Exception e){
                    log.error(e.getMessage());
                }
                return new ResponseEntity("password changed", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("id and email is not matched", HttpStatus.BAD_REQUEST);
            }
        }
    }
}

