package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.User;

@Repository
@Mapper
public interface UserMapper {

    User selectUserInfo(String id);
    void insertUserInfo(User user);
}
