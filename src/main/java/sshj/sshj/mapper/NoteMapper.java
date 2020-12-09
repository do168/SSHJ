package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.NoteDto;

import java.util.List;

@Mapper
@Repository
public interface NoteMapper {

    void insertMessage(NoteDto noteDto);

    List<NoteDto> selectPerson(long userId);

    List<NoteDto> selectMessage(long userId, long other);

    int selectCountOfReceiveMessage(String Id);
}
