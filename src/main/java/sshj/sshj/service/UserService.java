package sshj.sshj.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sshj.sshj.component.CodeCompo;
import sshj.sshj.component.SenderCompo;
import sshj.sshj.dto.SenderDto;
import sshj.sshj.dto.User;
import sshj.sshj.dto.UserInfoModel;
import sshj.sshj.mapper.UserMapper;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private String sender = "daedocrew@gmail.com";
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

    public int selectUserId(String id) throws Exception {
        return userMapper.selectUserId(id);
    }

    public String sendEmail(String email) {
        String code = codeCompo.excuteGenerate();
        String time = time_now();

        SenderDto senderDto = SenderDto.builder()
                .from(sender)
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
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        User user = modelMapper.map(userInfoModel, User.class);
        this.userMapper.insertUserInfo(User.builder()
            .id(userInfoModel.getId())
            .password(passwordEncoder.encode(userInfoModel.getPassword()))
            .roles(Collections.singletonList("ROLE_USER"))
            .nickname(userInfoModel.getNickname())
            .created_time(time)
            .build()); //getId() 없앴는데 뭐가 달라지나?
        return;
    }

    public String selectCode(String code) {
        return userMapper.selectCode(code);
    }

    public String time_now() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String dtime = formatter.format(systemTime);
        return dtime;
    }
}

