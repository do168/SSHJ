package sshj.sshj.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sshj.sshj.component.CodeCompo;
import sshj.sshj.component.SSHZUtil;
import sshj.sshj.component.SenderCompo;
import sshj.sshj.configuration.JwtTokenProvider;
import sshj.sshj.dto.*;
import sshj.sshj.mapper.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:aws.yml")
public class UserService implements UserDetailsService {

	@Value("${cloud.aws.sender")
    private String sender;

	@Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SSHZUtil sshzUtil;

    @Autowired
    private SenderCompo senderCompo;

    @Autowired
    private CodeCompo codeCompo;

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        return userMapper.selectUserInfo(email);
    }

//    public int selectUserEmail(long userId) throws Exception {
//        return userMapper.selectUserEmail(userId);
//    }
    //////////////////////////////////////////////////////////////////////////////// 회원가입, 로그인, 로그아웃, 회원탈퇴

    public ServiceResultModel executeSignUp(UserInfoModel userInfoModel) {
    	String email = userInfoModel.getEmail();
    	String msg = "회원가입 성공";

    	// 이메일 도메인 확인
        if (!sshzUtil.isEmailReg(email)) {
            msg = "해당 대학교의 이메일이 아닙니다";
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }

    	// 이미 가입한 경우
        if(userMapper.selectUserInfo(email)!=null) {
            msg = "이미 존재하는 아이디입니다";
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }

        // 인증코드가 일치하지 않는 경우
        if (!userMapper.selectCode(userInfoModel.getEmail()).equals(userInfoModel.getCode())) {
            log.info("[{}] [{}]", userMapper.selectCode(userInfoModel.getEmail()), userInfoModel.getCode());
            msg = "인증코드가 일치하지 않습니다.";
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }

        try {
            this.insertUser(userInfoModel);
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg(msg)
                    .build();
        } catch (Exception e) {
            log.error("", e);
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg("DB 삽입 실패")
                    .build();
        }
    }

    public ServiceResultModel deleteUserId(UserHeaderModel userHeaderModel) {

        if (userMapper.selectUserId(userHeaderModel.getUserId()) == 0) {
            String msg = "해당 아이디가 존재하지 않습니다.";
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }

        try{
            userMapper.deleteUserId(userHeaderModel.getUserId());
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg("회원탈퇴 완료")
                    .build();
        } catch(Exception e) {
            log.error("", e);
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("회원탈퇴 실패")
                    .build();
        }
    }

    public ServiceResultModel excuteLogIn(String email, String password) {

        UserDto userDto = userMapper.selectUserInfo(email);


        // 회원가입되지 않은 이메일이거나, 패스워드가 다른 경우
        if (userDto == null || !passwordEncoder.matches(password, userDto.getPassword())) {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("email or password is not valid")
                    .build();
        }

        // 정상적인 경우 토큰을 발급한다
        return getServiceResultModel(userDto);
//        log.info(userDto.getEmail());
//        String refresh_token = jwtTokenProvider.createRefreshToken();
//        token.put("refreshToken", refresh_token);
//        redisTemplate.opsForValue().set(userDto.getUsername(), refresh_token); // refresh_token은 따로 redis에 저장
    }

    public ServiceResultModel excuteLogInForClub(String email, String password) {

        UserDto userDto = userMapper.selectUserInfo(email);


        // 회원가입되지 않은 이메일이거나, 패스워드가 다른 경우
        if (userDto == null || !passwordEncoder.matches(password, userDto.getPassword())) {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("email or password is not valid")
                    .build();
        }

        // 동아리나 관리자 계정이 아닌 경우 접근 불허
        if (userDto.getRole().equals("ROLE_USER")) {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("ROLE_USER can't login this page")
                    .build();
        }

        // 정상적인 경우 토큰을 발급한다
        return getServiceResultModel(userDto);
//        log.info(userDto.getEmail());
//        String refresh_token = jwtTokenProvider.createRefreshToken();
//        token.put("refreshToken", refresh_token);
//        redisTemplate.opsForValue().set(userDto.getUsername(), refresh_token); // refresh_token은 따로 redis에 저장
    }

    public ServiceResultModel excuteRetoken(String accessToken) {

        String loginId;
//        String newRefreshToken = null;
//        String refreshTokenFromDb = null;

//            newRefreshToken = refreshToken;

        // 토큰 유효성 검사 ( 현재 서비스에서 쓰이는 토큰인지 판별위해 DB에 해당 유저 검색 )
        loginId = jwtTokenProvider.getUserPk(accessToken);
        if (loginId == null) {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("token is not valid")
                    .build();
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
        // 현재 토큰을 통해 유저 정보를 받은 뒤 새로운 토큰 재발급
        UserDto userDto = jwtTokenProvider.getUserDto(accessToken);
        return getServiceResultModel(userDto);
//                } else {
//                    map.put("success", false);
//                    map.put("msg", "refresh token is expired.");
//                }
//            } else { //refresh token이 없으면
//                map.put("success", false);
//                map.put("msg", "your refresh token does not exist.");
//            }
    }

    public ServiceResultModel excuteLogout(String accessToken){

        String loginId;
        loginId = jwtTokenProvider.getUserPk(accessToken);
        // 토큰 유효성 판단
        if (loginId == null) {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("토큰 유효하지 않음 - 로그아웃 실패")
                    .build();
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
        try{
            // 해당 토큰 블랙리스트 추가
            sshzUtil.afterLogoutToken(accessToken);
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg("로그아웃 성공")
                    .build();
        } catch(Exception e){
            log.error("",e);
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("블랙리스트 추가 실패 - 로그아웃 실패")
                    .build();
        }

    }
    //////////////////////////////////////////////////////////////////////////////////// select

    public UserDto selectUserEmail(String email) {
        return userMapper.selectUserEmail(email);
    }

    public String selectUserNickname(long userId) {
        return userMapper.selectUserNickname(userId);
    }

    public ServiceResultModel selectUserNicknameIsOk(String nickname) {

        // 바꿀 닉네임이 DB에 존재하는지 체크
        if (userMapper.selectUserNicknameIsOk(nickname) == 0) {
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg("사용 가능한 닉네임입니다.")
                    .build();
        }
        else {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("중복된 닉네임입니다")
                    .build();
        }
    }

    public UserDto selectUser(long userId) {
        return userMapper.selectUserInfoById(userId);
    }

//    public CodeInfoModel selectCodeInfo(String code) {
//        return userMapper.selectCodeInfo(code);
//    }
//
//    public String selectCode(String email) { return userMapper.selectCode(email); }


    ////////////////////////////////////////////////////////////////////// update

    public void updateUserNickname(long userId, String nickname) {
        userMapper.updateUserNickname(userId, nickname);
    }

    public void updateUserPassword(String email, String password) {
        userMapper.updateUserPassword(email, password);
    }

    public void updateDeviceToken(long userId, String deviceToken) {
        userMapper.updateDeviceToken(userId, deviceToken);
    }

    public void updateCodeEmail(String code, String email, String time) {
        userMapper.updateCodeEmail(code, email, time);
    }


    ///////////////////////////////////////////////////////////////////////////////////이메일 관련 메소드

    public ServiceResultModel excuteSendEmail(String email) {

        // 이메일 도메인 확인
        if (!sshzUtil.isEmailReg(email)) {
            String msg = "해당 대학교의 이메일이 아닙니다";
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }
        // 이미 가입된 이메일 처리
        if (userMapper.selectUserEmail(email) != null) {
            String msg = "이미 인증에 사용된 이메일입니다. 다른 이메일을 사용해주세요";

            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }
        // 인증 이메일 발신
        try {
            this.sendEmail(email);
            String msg = email+": 인증 이메일 발신 성공";
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg(msg)
                    .build();
        } catch (Exception e) {
            log.error("",e);
            String msg = email+": 인증 이메일 발신 실패";
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }
    }

    public ServiceResultModel executeValidateCode(String code, String email) {
        // 지금 시간
        long now_time = Long.parseLong(this.time_now());

        // 인증코드 보낸 시간
        CodeInfoModel codeInfoModel = userMapper.selectCodeInfo(code);
        long created_time = Long.parseLong(codeInfoModel.getCreatedTime());

        // request로 넘어온 이메일과 저장된 이메일이 같으며, 정해진 시간 내에 코드를 인증했다면
        if (codeInfoModel.getEmail().equals(email) && sshzUtil.isValidCode(now_time, created_time)) {
            String msg = "인증 성공";
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg(msg)
                    .build();
        } else {
            log.info("인증 실패");
            String msg = "인증 실패";
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(msg)
                    .build();
        }
    }

    public void insertCodeEmail(String code, String email, String time) {
        userMapper.insertCodeEmail(code, email, time);
    }

    public boolean sendEmail(String email) {
        String code = codeCompo.excuteGenerate();
        String time = time_now();
        log.info(sender);
        SenderDto senderDto = SenderDto.builder()
                .from("daedocrew@gmail.com")
                .to(email)
                .subject("sshj 인증 이메일입니다.")
                .content(code)
                .build();
        try{
            senderCompo.send(senderDto);
            if (userMapper.selectCode(email) == null) {
                insertCodeEmail(code, email, time);
            }
            else{
                updateCodeEmail(code, email, time);
            }
            log.info("success");
            return true;
        } catch(Exception e) {
            log.info(e.toString());
            return false;
        }
    }

//    public boolean sendEmail_findId(String email) {
//        String code = codeCompo.excuteGenerate();
//        String time = time_now();
//
//        SenderDto senderDto = SenderDto.builder()
//                .from("daedocrew@gmail.com")
//                .to(email)
//                .subject("sshj 아이디 찾기 인증 이메일입니다.")
//                .content(code)
//                .build();
//
//        try{
//            senderCompo.send(senderDto);
//            if (userMapper.selectCode(email) == null) {
//                insertCodeEmail(code, email, time);
//            }
//            else{
//                updateCodeEmail(code, email, time);
//            }
//            log.info("success");
//            return true;
//        } catch(Exception e) {
//            log.info(e.toString());
//            return false;
//        }
//
//    }




    /////////////////////////////////////////////////////////////////////////////////// 단순 작업 메소드

    // TODO: 현재 사용되는 컨트롤러가 없음 과연 이함수가 따로 있을 필요가 있을까? (맞다면 public으로 전환)
    private void insertUser(UserInfoModel userInfoModel) {
        String time = time_now();
        UserDto userDto = UserDto.builder()
                .email(userInfoModel.getEmail())
                .password(passwordEncoder.encode(userInfoModel.getPassword()))
                .role("ROLE_USER")
                .nickname(userInfoModel.getNickname())
                .createdTime(time)
                .build();

        userMapper.insertUser(userDto); //getId() 없앴는데 뭐가 달라지나?
        return;
    }


    public String time_now() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        return formatter.format(systemTime);
    }


    private ServiceResultModel getServiceResultModel(UserDto userDto) {
        try {
            String access_token = jwtTokenProvider.createAccessToken(userDto.getEmail(), userDto.getUserId(), userDto.getNickname(), userDto.getRole());
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg(access_token)
                    .build();
        } catch(Exception e) {
            log.error("",e);
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("token creation error")
                    .build();
        }
    }

}

