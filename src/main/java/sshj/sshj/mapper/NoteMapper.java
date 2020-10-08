package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.NoteDto;

import java.util.List;

@Mapper
@Repository
public interface NoteMapper {

    void insertMessage(NoteDto noteDto);

    List<String> selectPerson(String loginId);

    List<NoteDto> selectMessage(String loginId, String other);

    int selectCountOfReceiveMessage(String Id);
}
