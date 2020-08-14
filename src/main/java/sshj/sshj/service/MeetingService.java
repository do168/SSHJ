package sshj.sshj.service;

import java.util.List;

import sshj.sshj.dto.MeetingDto;

public interface MeetingService {
    void insertMeeting(MeetingDto dto) throws Exception;
    MeetingDto selectMeeting(int meetingId) throws Exception;
    void updateMeeting(MeetingDto dto) throws Exception;
    void deleteMeeting(int meetingId) throws Exception;
    List<MeetingDto> selectMeetingList() throws Exception;
    List<MeetingDto> selectClubByMeetingList(int clubId) throws Exception;
    List<MeetingDto> selectUserByMeetingList(int userId) throws Exception;
}
