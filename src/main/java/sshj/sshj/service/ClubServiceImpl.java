package sshj.sshj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sshj.sshj.dto.ClubDescriptionDto;
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
    public ClubNoticeDto selectClubNotice(int noticeId) throws Exception {
        return clubMapper.selectClubNotice(noticeId);
    }

    @Override
    public List<ClubNoticeDto> selectClubNoticeList(int clubId) throws Exception {
        return clubMapper.selectClubNoticeList(clubId);
    }

    @Override
    public void updateClubNotice(ClubNoticeDto dto) throws Exception {
        clubMapper.updateClubNotice(dto);
    }

    @Override
    public void deleteClubNotice(int noticeId) throws Exception {
        clubMapper.deleteClubNotice(noticeId);
    }

    @Override
    public void insertClubNoticeLike(int userId, int noticeId) throws Exception {
        clubMapper.insertClubNoticeLike(userId,noticeId);
    }

    @Override
    public void deleteClubNoticeLike(int userId, int noticeId) throws Exception {
        clubMapper.deleteClubNoticeLike(userId,noticeId);
    }

    @Override
    public int selectClubNoticeCnt(int noticeId) throws Exception {
        return clubMapper.selectClubNoticeCnt(noticeId);
    }

    @Override
    public void insertClubSubs(int userId, int clubId) throws Exception {
        clubMapper.insertClubSubs(userId,clubId);
    }

    @Override
    public void deleteClubSubs(int userId, int clubId) throws Exception {
        clubMapper.deleteClubSubs(userId,clubId);
    }

    @Override
    public void insertClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception {
        clubMapper.insertClubDescription(clubDescriptionDto);
    }

    @Override
    public String selectClubDescription(int clubId) throws Exception{
        return clubMapper.selectClubDescription(clubId);
    }

    @Override
    public void updateClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception{
        clubMapper.updateClubDescription(clubDescriptionDto);
    }

    @Override
    public void deleteClubDescription(int clubId) throws Exception{
        clubMapper.deleteClubDescription(clubId);
    }

    @Override
    public boolean selectIsSubClub(int userId, int clubId) throws Exception {
        return clubMapper.selectIsSubClub(userId, clubId) == 1;
    }

    @Override
    public List<Integer> selectSubClubList(int userId) throws Exception {
        return clubMapper.selectSubClubList(userId);
    }
}
