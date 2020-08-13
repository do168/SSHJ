package sshj.sshj.service;

import sshj.sshj.dto.MeetingDto;

import java.util.List;

public interface MeetingService {
    void insertMeeting(MeetingDto dto) throws Exception;
    MeetingDto selectMeeting(int meetingId) throws Exception;
    void updateMeeting(MeetingDto dto) throws Exception;
    void deleteMeeting(int meetingId) throws Exception;
    List<MeetingDto> selectMeetingList() throws Exception;
}
