package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.MeetingDto;

import java.util.List;

@Mapper
@Repository
public interface MeetingMapper {
    void insertMeeting(MeetingDto dto) throws Exception;
    MeetingDto selectMeeting(int meetingId) throws Exception;
    void updateMeeting(MeetingDto dto) throws Exception;
    void deleteMeeting(int meetingId) throws Exception;
    List<MeetingDto> selectMeetingList() throws Exception;
    List<MeetingDto> selectClubByMeetingList(int clubId) throws Exception;
    List<MeetingDto> selectUserByMeetingList(int userId) throws Exception;
    void insertMeetingLike(int userId,int meetingId) throws Exception;
    void deleteMeetingLike(int userId,int meetingId) throws Exception;
}
