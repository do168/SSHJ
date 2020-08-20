package sshj.sshj.controller;


import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api(value = "Oauth-controller", description = "Oauth controller")
@AllArgsConstructor
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

        if (userService.selectUserEmail(email) == 1) {
            log.info("이미 인증된 이메일입니다. 다른 이메일을 입력해주세요");
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        try {
            userService.sendEmail(email);
            log.info("이메일 발신 성공");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            log.info("이메일 발신 실패");
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
        long created_time = Long.parseLong(codeInfoModel.getCreated_time());

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
            @ApiParam(value = "아이디", required = true) @RequestParam(name = "id", required = true) String id,
            @ApiParam(value = "패스워드", required = true) @RequestParam(name = "password", required = true) String password) throws Exception {

        UserDto userDto = userService.selectUser(id);
        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        Map<String, String> token = new HashMap<>();
        String access_token = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getRole());
        String refresh_token = jwtTokenProvider.createRefreshToken();
        token.put("accessToken", access_token);
        token.put("refreshToken", refresh_token);
        redisTemplate.opsForValue().set(userDto.getUsername(), refresh_token); // refresh_token은 따로 redis에 저장

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * 회원가입 시 생성하려는 아이디 중복 체크
     * @param access_token 만료된 accessToken
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
        String id = null;

        Map<String, Object> map = new HashMap<>();
        try {
            accessToken = access_token;
            refreshToken = refresh_token;
            log.info("access token in param: " + accessToken);
            try {
                id = jwtTokenProvider.getUserPk(accessToken);
            } catch (IllegalArgumentException e) {

            } catch (ExpiredJwtException e) { //expire됐을 때
                id = e.getClaims().getSubject();
                log.info("id from expired access token: " + id);
            }

            if (refreshToken != null) { //refresh를 같이 보냈으면.
                try {

                    refreshTokenFromDb = (String) redisTemplate.opsForValue().get(id);
                    log.info("rtfrom db: " + refreshTokenFromDb);
                } catch (IllegalArgumentException e) {
                    log.warn("illegal argument!!");
                }
                //둘이 일치하고 만료도 안됐으면 재발급 해주기.
                if (refreshToken.equals(refreshTokenFromDb) && jwtTokenProvider.validateToken(refreshToken)) {
                    UserDto userDto = jwtTokenProvider.getUserDto(accessToken);

                    String newtok = jwtTokenProvider.createAccessToken(userDto.getUsername(), userDto.getUserId(), userDto.getRole());
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
     * 회원가입 시 생성하려는 아이디 중복 체크
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

        String id = null;
        String accessToken = access_token;
        try {
            id = jwtTokenProvider.getUserPk(accessToken);
        } catch (IllegalArgumentException e) {
        } catch (ExpiredJwtException e) { //expire됐을 때
            id = e.getClaims().getSubject();
            log.info("id from expired access token: " + id);
        }

        try {
            if (redisTemplate.opsForValue().get(id) != null) {
                //delete refresh token
                redisTemplate.delete(id);
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
}


