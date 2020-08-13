package sshj.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sshj.domain.User;
import sshj.dto.UserInfoModel;
import sshj.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }


    public void insertUser(UserInfoModel userInfoModel) {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String dtime = formatter.format(systemTime);
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        User user = modelMapper.map(userInfoModel, User.class);
        this.userRepository.save(User.builder()
            .id(userInfoModel.getId())
            .password(passwordEncoder.encode(userInfoModel.getPassword()))
            .roles(Collections.singletonList("ROLE_USER"))
            .nickname(userInfoModel.getNickname())
            .created_time(dtime)
            .build()).getId();
    }
}