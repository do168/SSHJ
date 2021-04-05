package sshj.sshj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import sshj.sshj.common.exception.NotExistException;
import sshj.sshj.dto.ClubDescriptionDto;
import sshj.sshj.dto.ClubInfoDto;
import sshj.sshj.dto.ClubNoticeDto;
import sshj.sshj.mapper.ClubMapper;
import sshj.sshj.model.Club;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService{
    @Autowired
    private ClubMapper clubMapper;

    //TODO: clubMapper에 해당 함수 구현해야함
    /**
     * 클럽 생성
     * @param clubParam
     * @return
     */
    @Override
    public Club create(Club clubParam) {
        // create club
        long clubId = clubMapper.create(clubParam);
        // find club by created club Id
        Club createdClub = clubMapper.find(clubId);
        if (ObjectUtils.isEmpty(createdClub)) {
            throw new NotExistException("club didn't created");
        }
        return createdClub;
    }

    /**
     * 클럽 업데이트
     * @param clubParam
     * @return
     */
    @Override
    public Club update(Club clubParam) {
        // update club
        long clubId = clubMapper.update(clubParam);
        // find club by updated club Id
        Club updatedclub = clubMapper.find(clubId);
        if (ObjectUtils.isEmpty(updatedclub)) {
            throw new NotExistException("club didn't updated");
        }
        return updatedclub;
    }

    @Override
    public void delete(long id) {
        // check if club exists
        boolean isClubExists = clubMapper.find(id) == null ? true : false;
        if (!isClubExists) {
            throw new NotExistException("Club");
        }

        // check if delete success
        boolean isDeleted = clubMapper.delete(id) == 0 ? false : true;
        if(!isDeleted) {
            throw new NotExistException("Club not deleted");
        }

    }

    @Override
    public Club find(long id){
        // fiod club
        Club club = clubMapper.find(id);
        if (ObjectUtils.isEmpty(club)) {
            throw new NotExistException("Club don't exist");
        }
        return club;
    }

    @Override
    public List<Club> getList(long ids) {
        List<Club> clubList = clubMapper.findList(ids);
        return clubList;
    }

    @Override
    // 공지사항 갑입
    public void insertClubNotice(ClubNoticeDto dto) throws Exception {
        clubMapper.insertClubNotice(dto);
    }

    @Override
    // 공지사항 선택
    public ClubNoticeDto selectClubNotice(long noticeId) throws Exception {
        return clubMapper.selectClubNotice(noticeId);
    }

    @Override
    // 클럽 공지사항 리스트
    public List<ClubNoticeDto> selectClubNoticeList(long clubId) throws Exception {
        return clubMapper.selectClubNoticeList(clubId);
    }

    @Override
    // 공지사항 갱신
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
    // 현재 유저가 해당 동아리 구독 여부
    public boolean selectIsSubClub(long userId, long clubId) throws Exception {
        return clubMapper.selectIsSubClub(userId, clubId) == 1;
    }

    @Override
    // 동아리 구독 유저 리스트
    public List<Long> selectSubClubList(long userId) throws Exception {
        return clubMapper.selectSubClubList(userId);
    }

    @Override
    // 동아리 구독 수
    public int selectClubSubscribeCnt(long clubId) throws Exception {
        return clubMapper.selectClubSubscribeCnt(clubId);
    }

    @Override
    // 클럽id, 클럽 이름 리턴
    public List<ClubInfoDto> selectClubIdAndClubName() throws Exception {
        return clubMapper.selectClubIdAndClubName();
    }

}
