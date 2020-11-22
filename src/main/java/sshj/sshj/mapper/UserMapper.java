package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.CodeInfoModel;
import sshj.sshj.dto.UserDto;

@Repository
@Mapper
public interface UserMapper {

    UserDto selectUserInfo(String email);
    int selectUserEmail(long userId);
    UserDto selectUserEmail(String email);
    void insertUser(UserDto userDto);
    String selectCode(String email);
    CodeInfoModel selectCodeInfo(String code);
    void insertCodeEmail(String code, String email, String time);
    void updateCodeEmail(String code, String email, String time);
    int selectUserNicknameIsOk(String nickname);
    void updateUserNickname(long userId, String nickname);
    void updateUserPassword(long userId, String password);
    void updateDeviceToken(long userId, String deviceToken);
    String selectUserDeviceToken(long userId);
    String selectEmail(long userId);
    String selectUserNickname(long userId);
}
