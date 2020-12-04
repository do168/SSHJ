package sshj.sshj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import sshj.sshj.dto.MeetingDto;
import sshj.sshj.dto.MeetingSearchDto;
import sshj.sshj.dto.SimpleFileDto;
import sshj.sshj.dto.UserDto;
import sshj.sshj.dto.enums.FlagEnum;
import sshj.sshj.mapper.MeetingMapper;
import sshj.sshj.mapper.S3FileMapper;

@Slf4j
@Service
public class MeetingServiceImpl implements MeetingService {
	
	private static String S3_DOMAIN = "";

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private S3FileMapper s3FileMapper;
    
    @Override
    public MeetingDto insertMeeting(MeetingDto dto) throws Exception {
        
    	int cnt = meetingMapper.insertMeeting(dto);
    	if(cnt < 1) {
    		log.info("insert failed!");
    	}
    	
    	return dto;
    }

    @Override
    public MeetingDto selectMeeting(long userId, long meetingId) throws Exception {
        MeetingDto md = meetingMapper.selectMeeting(userId, meetingId);
        List<SimpleFileDto> imgList =  s3FileMapper.getMeetingFiles(md.getMeetingId());
        for(SimpleFileDto fileDto: imgList) {
        	fileDto.setFileUrl(S3_DOMAIN + fileDto.getFileUrl());
        }
        md.setImgList(imgList);
        return md;
    }

    @Override
    public void updateMeeting(MeetingDto dto) throws Exception {
        meetingMapper.updateMeeting(dto);
    }

    @Override
    public void deleteMeeting(long meetingId) throws Exception {
        meetingMapper.deleteMeeting(meetingId);
//      TODO 업로드 된 파일들을 삭제 해야하는가 정책적으로 살펴보자 -> 아카이브에는 남겨두고 관계테이블에서 chain 삭제
    }

    @Override
    public List<MeetingDto> selectMeetingList(MeetingSearchDto meetingSearchDto) throws Exception {
    	
    	/*
    	 * enum 데이터 null 체크
    	 */
    	if(meetingSearchDto.getIsApplied() == null)
    		meetingSearchDto.setIsApplied(FlagEnum.N);
    	if(meetingSearchDto.getIsSubscribed() == null)
    		meetingSearchDto.setIsSubscribed(FlagEnum.N);
    	
    	// 페이징 계산
    	meetingSearchDto.setOffset(meetingSearchDto.getOffset() * meetingSearchDto.getPageScale());
    	
    	List<MeetingDto> meetingList = meetingMapper.selectMeetingList(meetingSearchDto);
    	
    	for(MeetingDto md : meetingList) {
    		List<SimpleFileDto> imgList =  s3FileMapper.getMeetingFiles(md.getMeetingId());
            for(SimpleFileDto fileDto: imgList) {
            	fileDto.setFileUrl(S3_DOMAIN + fileDto.getFileUrl());
            }
            md.setImgList(imgList);
    	}
    	
    	return meetingList;
    }

    @Override
    public List<MeetingDto> selectClubByMeetingList(long clubId) throws Exception {

        List<MeetingDto> meetingList = meetingMapper.selectClubByMeetingList(clubId);

        for(MeetingDto md : meetingList) {
        	List<SimpleFileDto> imgList =  s3FileMapper.getMeetingFiles(md.getMeetingId());
            for(SimpleFileDto fileDto: imgList) {
            	fileDto.setFileUrl(S3_DOMAIN + fileDto.getFileUrl());
            }
            md.setImgList(imgList);
        }

        return meetingList;
    }

    @Override
    public List<MeetingDto> selectUserByMeetingList(long userId) throws Exception {

        List<MeetingDto> meetingList = meetingMapper.selectUserByMeetingList(userId);

        for(MeetingDto md : meetingList) {
        	List<SimpleFileDto> imgList =  s3FileMapper.getMeetingFiles(md.getMeetingId());
            for(SimpleFileDto fileDto: imgList) {
            	fileDto.setFileUrl(S3_DOMAIN + fileDto.getFileUrl());
            }
            md.setImgList(imgList);
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
    
    @Override
    public void registerUserApplied(long userId, long meetingId) throws Exception {
    	/*
    	 * TODO: 이미 참여한 모임인지체크
    	 * TODO: 신청 최대인원 넘겼는지 체크 
    	 */
    	int cnt = meetingMapper.registerUserApplied(userId, meetingId);
    	if(cnt != 1) {
    		log.error("registerUserApplied count ERROR");
    		throw new Exception();
    	}
    }
    
    @Override
    public void deleteUserApplied(long userId, long meetingId) throws Exception {
    	int cnt = meetingMapper.deleteUserApplied(userId, meetingId);
    	if(cnt != 1) {
    		log.error("deleteUserApplied count ERROR");
    		throw new Exception();	
    	}
    }

	@Override
	public List<UserDto> getMeetingUserList(long clubId, long meetingId) throws Exception {
		
		return meetingMapper.getMeetingUserList(clubId, meetingId);
	}
	
	@Override
	public String getMeetingChatUrl(long clubId, long meetingId) throws Exception {
		
		return meetingMapper.getMeetingChatUrl(clubId, meetingId);
	}
}
