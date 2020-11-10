package sshj.sshj.service;

import sshj.sshj.dto.MeetingDto;

import java.util.List;

public interface MeetingService {
	
	MeetingDto insertMeeting(MeetingDto dto) throws Exception;

    MeetingDto selectMeeting(long userId, long meetingId) throws Exception;

    void updateMeeting(MeetingDto dto) throws Exception;

    void deleteMeeting(long meetingId) throws Exception;

    List<MeetingDto> selectMeetingList() throws Exception;

    List<MeetingDto> selectClubByMeetingList(long clubId) throws Exception;

    List<MeetingDto> selectUserByMeetingList(long userId) throws Exception;

    void insertMeetingLike(long userId,long meetingId) throws Exception;

    void deleteMeetingLike(long userId,long meetingId) throws Exception;
    
    void registerUserApplied(long userId, long meetingId) throws Exception;
    
    void deleteUserApplied(long userId, long meetingId) throws Exception;
}
