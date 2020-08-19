package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.CodeInfoModel;
import sshj.sshj.dto.UserDto;

@Repository
@Mapper
public interface UserMapper {

    UserDto selectUserInfo(String id);
    int selectUserId(String id);
    int selectUserEmail(String email);
    void insertUser(UserDto userDto);
    CodeInfoModel selectCode(String code);
    void insertCodeEmail(String code, String email, String time);
    int selectUserNickname(String nickname);
}
