package sshj.sshj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sshj.sshj.dto.ClubDescriptionDto;
import sshj.sshj.dto.ClubInfoDto;
import sshj.sshj.dto.ClubNoticeDto;
import sshj.sshj.mapper.ClubMapper;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService{
    @Autowired
    private ClubMapper clubMapper;

    @Override
    public void insertClubNotice(ClubNoticeDto dto) throws Exception {
        clubMapper.insertClubNotice(dto);
    }

    @Override
    public ClubNoticeDto selectClubNotice(long noticeId) throws Exception {
        return clubMapper.selectClubNotice(noticeId);
    }

    @Override
    public List<ClubNoticeDto> selectClubNoticeList(long clubId) throws Exception {
        return clubMapper.selectClubNoticeList(clubId);
    }

    @Override
    public void updateClubNotice(ClubNoticeDto dto) throws Exception {
        clubMapper.updateClubNotice(dto);
    }

    @Override
    public void deleteClubNotice(long noticeId) throws Exception {
        clubMapper.deleteClubNotice(noticeId);
    }

    @Override
    public void insertClubNoticeLike(long userId, long noticeId) throws Exception {
        clubMapper.insertClubNoticeLike(userId,noticeId);
    }

    @Override
    public void deleteClubNoticeLike(long userId, long noticeId) throws Exception {
        clubMapper.deleteClubNoticeLike(userId,noticeId);
    }

    @Override
    public long selectClubNoticeCnt(long noticeId) throws Exception {
        return clubMapper.selectClubNoticeCnt(noticeId);
    }

    @Override
    public void insertClubSubs(long userId, long clubId) throws Exception {
        clubMapper.insertClubSubs(userId,clubId);
    }

    @Override
    public void deleteClubSubs(long userId, long clubId) throws Exception {
        clubMapper.deleteClubSubs(userId,clubId);
    }

    @Override
    public void insertClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception {
        clubMapper.insertClubDescription(clubDescriptionDto);
    }

    @Override
    public String selectClubDescription(long clubId) throws Exception{
        return clubMapper.selectClubDescription(clubId);
    }

    @Override
    public void updateClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception{
        clubMapper.updateClubDescription(clubDescriptionDto);
    }

    @Override
    public void deleteClubDescription(long clubId) throws Exception{
        clubMapper.deleteClubDescription(clubId);
    }

    @Override
    public boolean selectIsSubClub(long userId, long clubId) throws Exception {
        return clubMapper.selectIsSubClub(userId, clubId) == 1;
    }

    @Override
    public List<Long> selectSubClubList(long userId) throws Exception {
        return clubMapper.selectSubClubList(userId);
    }

    @Override
    public int selectClubSubscribeCnt(long clubId) throws Exception {
        return clubMapper.selectClubSubscribeCnt(clubId);
    }

    @Override
    public List<ClubInfoDto> selectClubIdAndClubName() throws Exception {
        return clubMapper.selectClubIdAndClubName();
    }
}
