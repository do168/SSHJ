package sshj.sshj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
