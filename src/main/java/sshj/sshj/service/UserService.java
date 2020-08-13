package sshj.sshj.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sshj.sshj.dto.User;
import sshj.sshj.dto.UserInfoModel;
import sshj.sshj.mapper.UserMapper;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.selectUserInfo(username);
    }

    public User selectUserInfo(String id) throws Exception {
        return userMapper.selectUserInfo(id);
    }


    public void insertUser(UserInfoModel userInfoModel) {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String dtime = formatter.format(systemTime);
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        User user = modelMapper.map(userInfoModel, User.class);
        this.userMapper.insertUserInfo(User.builder()
            .id(userInfoModel.getId())
            .password(passwordEncoder.encode(userInfoModel.getPassword()))
            .roles(Collections.singletonList("ROLE_USER"))
            .nickname(userInfoModel.getNickname())
            .created_time(dtime)
            .build()); //getId() 없앴는데 뭐가 달라지나?
    }
}