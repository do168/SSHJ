package sshj.sshj.service;

import sshj.sshj.dto.MeetingDto;
import sshj.sshj.dto.UserByMeetingDto;

import java.util.List;

public interface MeetingService {
    void insertMeeting(MeetingDto dto) throws Exception;
    MeetingDto selectMeeting(int meetingId) throws Exception;
    void updateMeeting(MeetingDto dto) throws Exception;
    void deleteMeeting(int meetingId) throws Exception;
    List<MeetingDto> selectMeetingList() throws Exception;
    List<MeetingDto> selectClubByMeetingList(int clubId) throws Exception;
    List<UserByMeetingDto> selectUserByMeetingList(int userId) throws Exception;
}
