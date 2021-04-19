package sshj.sshj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import sshj.sshj.dto.MeetingDto;
import sshj.sshj.dto.UserDto;

@Mapper
@Repository
public interface MeetingMapper {
	
	int insertMeeting(MeetingDto dto) throws Exception;

	MeetingDto selectMeeting(long userId, long meetingId) throws Exception;

	void updateMeeting(MeetingDto dto) throws Exception;

	void deleteMeeting(long meetingId) throws Exception;

	List<MeetingDto> selectMeetingList(MeetingSearchDto meetingSearchDto) throws Exception;

	List<MeetingDto> selectClubByMeetingList(long clubId) throws Exception;

	List<MeetingDto> selectUserByMeetingList(long userId) throws Exception;

	void insertMeetingLike(long userId, long meetingId) throws Exception;

	void deleteMeetingLike(long userId, long meetingId) throws Exception;

	int registerUserApplied(long userId, long meetingId) throws Exception;

	int deleteUserApplied(long userId, long meetingId) throws Exception;
	
	List<UserDto> getMeetingUserList(long clubId, long meetingId) throws Exception;
    
	String getMeetingChatUrl(long userId, long meetingId) throws Exception;
    
}
