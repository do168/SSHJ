package sshj.sshj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sshj.sshj.dto.MeetingDto;
import sshj.sshj.mapper.MeetingMapper;
import sshj.sshj.mapper.S3FileMapper;

@Service
public class MeetingServiceImpl implements MeetingService {
	
	private static String S3_DOMAIN = "";

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private S3FileMapper s3FileMapper;
    
    @Override
    public int insertMeeting(MeetingDto dto) throws Exception {
        return meetingMapper.insertMeeting(dto);
    }

    @Override
    public MeetingDto selectMeeting(long userId, long meetingId) throws Exception {
        MeetingDto md = meetingMapper.selectMeeting(userId, meetingId);
        List<String> imgUrlList =  s3FileMapper.getMeetingFiles(md.getMeetingId());
        imgUrlList.stream().map(url -> url = S3_DOMAIN + url);
        md.setImgUrlList(imgUrlList);
        return md;
    }

    @Override
    public void updateMeeting(MeetingDto dto) throws Exception {
        meetingMapper.updateMeeting(dto);
    }

    @Override
    public void deleteMeeting(long meetingId) throws Exception {
        meetingMapper.deleteMeeting(meetingId);
    }

    @Override
    public List<MeetingDto> selectMeetingList() throws Exception {
    	
    	List<MeetingDto> meetingList = meetingMapper.selectMeetingList();
    	
    	for(MeetingDto md : meetingList) {
    		List<String> imgUrlList =  s3FileMapper.getMeetingFiles(md.getMeetingId());
    		imgUrlList.stream().map(url -> url = S3_DOMAIN + url);
    		md.setImgUrlList(imgUrlList);
    	}
    	
    	return meetingList;
    }

    @Override
    public List<MeetingDto> selectClubByMeetingList(long clubId) throws Exception {

        List<MeetingDto> meetingList = meetingMapper.selectClubByMeetingList(clubId);

        for(MeetingDto md : meetingList) {
            List<String> imgUrlList = s3FileMapper.getMeetingFiles(md.getMeetingId());
            imgUrlList.stream().map(url-> url = S3_DOMAIN + url);
            md.setImgUrlList(imgUrlList);
        }

        return meetingList;
    }

    @Override
    public List<MeetingDto> selectUserByMeetingList(long userId) throws Exception {

        List<MeetingDto> meetingList = meetingMapper.selectUserByMeetingList(userId);

        for(MeetingDto md : meetingList) {
            List<String> imgUrlList = s3FileMapper.getMeetingFiles(md.getMeetingId());
            imgUrlList.stream().map(url-> url = S3_DOMAIN + url);
            md.setImgUrlList(imgUrlList);
        }

        return meetingList;
    }

    @Override
    public void insertMeetingLike(long userId, long meetingId) throws Exception {
        meetingMapper.insertMeetingLike(userId,meetingId);
    }

    @Override
    public void deleteMeetingLike(long userId, long meetingId) throws Exception {
        meetingMapper.deleteMeetingLike(userId,meetingId);
    }
}
