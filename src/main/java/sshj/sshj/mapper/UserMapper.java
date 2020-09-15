package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.CodeInfoModel;
import sshj.sshj.dto.UserDto;

@Repository
@Mapper
public interface UserMapper {

    UserDto selectUserInfo(String loginId);
    int selectUserLoginId(String loginId);
    UserDto selectUserEmail(String email);
    void insertUser(UserDto userDto);
    CodeInfoModel selectCode(String code);
    void insertCodeEmail(String code, String email, String time);
    void updateCodeEmail(String code, String email, String time);
    int selectUserNickname(String nickname);
    void updateUserNickname(String loginId, String nickname);
    void updateUserPassword(String loginId, String password);
}
