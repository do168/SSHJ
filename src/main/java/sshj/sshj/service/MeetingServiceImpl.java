package sshj.sshj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sshj.sshj.dto.MeetingDto;
import sshj.sshj.mapper.MeetingMapper;
import sshj.sshj.mapper.S3FileMapper;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private S3FileMapper s3FileMapper;
    
    @Override
    public void insertMeeting(MeetingDto dto) throws Exception {
        meetingMapper.insertMeeting(dto);
    }

    @Override
    public MeetingDto selectMeeting(int meetingId) throws Exception {
        return meetingMapper.selectMeeting(meetingId);
    }

    @Override
    public void updateMeeting(MeetingDto dto) throws Exception {
        meetingMapper.updateMeeting(dto);
    }

    @Override
    public void deleteMeeting(int meetingId) throws Exception {
        meetingMapper.deleteMeeting(meetingId);
    }

    @Override
    public List<MeetingDto> selectMeetingList() throws Exception {
    	
    	List<MeetingDto> meetingList = meetingMapper.selectMeetingList();
    	
    	for(MeetingDto it : meetingList) {
    		
    		List<String> imgUrlList = s3FileMapper.getMeetingFiles(it.getMeetingId());
    		it.setImgUrlList(imgUrlList);
    	}
    	
    	return meetingList;
    }

    @Override
    public List<MeetingDto> selectClubByMeetingList(int clubId) throws Exception {
        return meetingMapper.selectClubByMeetingList(clubId);
    }

    @Override
    public List<MeetingDto> selectUserByMeetingList(int userId) throws Exception {
        return meetingMapper.selectUserByMeetingList(userId);
    }

    @Override
    public void insertMeetingLike(int userId, int meetingId) throws Exception {
        meetingMapper.insertMeetingLike(userId,meetingId);
    }

    @Override
    public void deleteMeetingLike(int userId, int meetingId) throws Exception {
        meetingMapper.deleteMeetingLike(userId,meetingId);
    }
}
