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

        if (userService.selectUserLoginId(loginId) == 0) {
            log.info("사용 가능한 아이디입니다.");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("중복된 아이디입니다");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("중복된 닉네임입니다");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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

        if (userService.selectUserEmail(email) != null) {
            log.info("it is used email, please use other email");
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        try {
            userService.sendEmail(email);
            log.info("success");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            log.info("fail");
            return new ResponseEntity<>(false, HttpStatus.OK);
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
        long created_time = Long.parseLong(codeInfoModel.getCreatedTime());

        if (codeInfoModel.getEmail().equals(insert_email) && now_time - created_time <= 3000) {
            log.info("인증 성공");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("인증 실패");
            return new ResponseEntity<>(false, HttpStatus.OK);
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
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
            throw new IllegalArgumentException("id or password is not valid");
        }
        log.info("in controller : "+userDto.getUserId());
        Map<String, String> token = new HashMap<>();
        String access_token = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getNickname(), userDto.getRole());
        String refresh_token = jwtTokenProvider.createRefreshToken();
        token.put("accessToken", access_token);
        token.put("refreshToken", refresh_token);
        log.info(userDto.getUsername());
        if("dev".equals(activeProfile))
        	redisTemplate.opsForValue().set(userDto.getUsername(), refresh_token); // refresh_token은 따로 redis에 저장
        
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * @param access_token  만료된 accessToken
     * @param refresh_token 안전한저장소에 저장된 refreshToken
     * @return 정상적 -> 새로 발급한 access_token
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

    @ApiOperation(
            value = "아이디 찾기"
            , notes = "아이디 찾기"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/searchId", method = RequestMethod.GET)
    public ResponseEntity<String> searchId(
            @ApiParam(value = "email", required = true) @RequestParam(name = "email", required = true) String email
    ) throws Exception {

        UserDto userDto = userService.selectUserEmail(email);
        if (userDto == null) {
            return new ResponseEntity("user does not exist", HttpStatus.BAD_REQUEST);
        } else {
            log.info(userDto.getLoginId());
            return new ResponseEntity(userDto.getLoginId(), HttpStatus.OK);
        }
    }

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
        if (userDto == null) {
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

