package sshj.sshj.service;

import java.util.List;

import sshj.sshj.dto.MeetingDto;
import sshj.sshj.dto.MeetingSearchDto;

public interface MeetingService {
	
	MeetingDto insertMeeting(MeetingDto dto) throws Exception;

    MeetingDto selectMeeting(long userId, long meetingId) throws Exception;

    void updateMeeting(MeetingDto dto) throws Exception;

    void deleteMeeting(long meetingId) throws Exception;

    List<MeetingDto> selectMeetingList(MeetingSearchDto meetingSearchDto) throws Exception;

    List<MeetingDto> selectClubByMeetingList(long clubId) throws Exception;

    List<MeetingDto> selectUserByMeetingList(long userId) throws Exception;

    void insertMeetingLike(long userId,long meetingId) throws Exception;

    void deleteMeetingLike(long userId,long meetingId) throws Exception;
    
    void registerUserApplied(long userId, long meetingId) throws Exception;
    
    void deleteUserApplied(long userId, long meetingId) throws Exception;
    
    /**
     * 모임에 참여중인 유저 정보 리스트
     * @param clubId
     * @param meetingId
     * @throws Exception
     */
    void getMeetingUserList(long clubId, long meetingId) throws Exception;
}
