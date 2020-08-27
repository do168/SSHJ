package sshj.sshj.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sshj.sshj.component.CodeCompo;
import sshj.sshj.component.SenderCompo;
import sshj.sshj.dto.CodeInfoModel;
import sshj.sshj.dto.SenderDto;
import sshj.sshj.dto.UserDto;
import sshj.sshj.dto.UserInfoModel;
import sshj.sshj.mapper.UserMapper;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
    private SenderCompo senderCompo;

    @Autowired
    private CodeCompo codeCompo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.selectUserInfo(username);
    }

    public int selectUserLoginId(String loginId) throws Exception {
        return userMapper.selectUserLoginId(loginId);
    }

    public UserDto selectUserEmail(String email) throws Exception {
        return userMapper.selectUserEmail(email);
    }

    public int selectUserNickname(String nickname) throws Exception {
        return userMapper.selectUserNickname(nickname);
    }

    public UserDto selectUser(String loginId) throws Exception {
        return userMapper.selectUserInfo(loginId);
    }

    public String sendEmail(String email) {
        String code = codeCompo.excuteGenerate();
        String time = time_now();

        SenderDto senderDto = SenderDto.builder()
                .from("daedocrew@gmail.com")
                .to(email)
                .subject("sshj 인증 이메일입니다.")
                .content(code)
                .build();

        senderCompo.send(senderDto);
        insertCodeEmail(code, email, time);

        return code;
    }

    public void insertCodeEmail(String code, String email, String time) {
        userMapper.insertCodeEmail(code, email, time);
    }

    public void insertUser(UserInfoModel userInfoModel) {
        String time = time_now();
        this.userMapper.insertUser(UserDto.builder()
            .loginId(userInfoModel.getLoginId())
            .password(passwordEncoder.encode(userInfoModel.getPassword()))
            .email(userInfoModel.getEmail())
            .role("ROLE_USER")
            .nickname(userInfoModel.getNickname())
            .createdTime(time)
            .build()); //getId() 없앴는데 뭐가 달라지나?
        return;
    }

    public CodeInfoModel selectCode(String code) {
        return userMapper.selectCode(code);
    }

    public void updateUserNickname(String loginId, String nickname) {
        userMapper.updateUserNickname(loginId, nickname);
    }

    public void updateUserPassword(String loginId, String password) {
        userMapper.updateUserPassword(loginId, password);
    }

    public String time_now() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String dtime = formatter.format(systemTime);
        return dtime;
    }


}

