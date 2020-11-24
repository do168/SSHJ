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

        this.insertUser(userInfoModel);
        return ServiceResultModel.builder()
                .flag(true)
                .msg(msg)
                .build();
    }

    public UserDto selectUserEmail(String email) {
        return userMapper.selectUserEmail(email);
    }

    public String selectUserNickname(long userId) {
        return userMapper.selectUserNickname(userId);
    }

    public int selectUserNicknameIsOk(String nickname) {
        return userMapper.selectUserNicknameIsOk(nickname);
    }

    public UserDto selectUser(long userId) {
        return userMapper.selectUserInfoById(userId);
    }

    public boolean sendEmail(String email) {
        String code = codeCompo.excuteGenerate();
        String time = time_now();

        SenderDto senderDto = SenderDto.builder()
                .from("daedocrew@gmail.com")
                .to(email)
                .subject("sshj 인증 이메일입니다.")
                .content(code)
                .build();
        try{
            senderCompo.send(senderDto);
            insertCodeEmail(code, email, time);
            log.info("success");
            return true;
        } catch(Exception e) {
            log.info(e.toString());
            return false;
        }
    }

    public boolean sendEmail_findId(String email) {
        String code = codeCompo.excuteGenerate();
        String time = time_now();

        SenderDto senderDto = SenderDto.builder()
                .from("daedocrew@gmail.com")
                .to(email)
                .subject("sshj 아이디 찾기 인증 이메일입니다.")
                .content(code)
                .build();

        try{
            senderCompo.send(senderDto);
            insertCodeEmail(code, email, time);
            log.info("success");
            return true;
        } catch(Exception e) {
            log.info(e.toString());
            return false;
        }

    }

    public void insertCodeEmail(String code, String email, String time) {
        userMapper.insertCodeEmail(code, email, time);
    }

    public void updateCodeEmail(String code, String email, String time) {
        userMapper.updateCodeEmail(code, email, time);
    }

    // TODO: 현재 사용되는 컨트롤러가 없음 과연 이함수가 따로 있을 필요가 있을까? (맞다면 public으로 전환)
    private void insertUser(UserInfoModel userInfoModel) {
        String time = time_now();
        UserDto userDto = UserDto.builder()
		        .email(userInfoModel.getEmail())
		        .password(passwordEncoder.encode(userInfoModel.getPassword()))
		        .email(userInfoModel.getEmail())
		        .role("ROLE_USER")
		        .nickname(userInfoModel.getNickname())
		        .createdTime(time)
		        .build();

        userMapper.insertUser(userDto); //getId() 없앴는데 뭐가 달라지나?
        return;
    }

    public CodeInfoModel selectCodeInfo(String code) {
        return userMapper.selectCodeInfo(code);
    }

    public String selectCode(String email) { return userMapper.selectCode(email); }

    public void updateUserNickname(long userId, String nickname) {
        userMapper.updateUserNickname(userId, nickname);
    }

    public void updateUserPassword(String email, String password) {
        userMapper.updateUserPassword(email, password);
    }

    public void updateDeviceToken(long userId, String deviceToken) {
        userMapper.updateDeviceToken(userId, deviceToken);
    }

    public String time_now() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        return formatter.format(systemTime);
    }


}

