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
import sshj.sshj.repository.ClubRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClubServiceImpl implements ClubService{
    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubRepository clubRepository;

    //TODO: clubMapper에 해당 함수 구현해야함
    /**
     * 클럽 생성
     * @param clubParam
     * @return
     */
    @Override
    public Club create(Club clubParam) {
        // create club
//        long clubId = clubMapper.create(clubParam);
        Club club = clubRepository.save(clubParam);
        // find club by created club Id
        return clubRepository.findById(club.getId()).orElseThrow(() -> new NotExistException("Club don't exist"));
    }

    /**
     * 클럽 업데이트
     * @param clubParam
     * @return
     */
    @Override
    @Transactional
    public Club update(Club clubParam, long id) {
        // find club
        Club club = clubRepository.findById(id).orElseThrow(() -> new NotExistException("Club don't exist"));
        club.setName(clubParam.getName());
        club.setDescription(clubParam.getDescription());
        // update club
        return club;
    }

    @Override
    public void delete(long id) {

        // delete
        clubRepository.deleteById(id);


    }

    @Override
    public Club find(long id){
        // find club
        Optional<Club> club = clubRepository.findById(id);
        System.out.println("test");
        if (!club.isPresent()) {
            throw new NotExistException("Club don't exist");
        }
        Club foundClub = club.get();
        System.out.println(foundClub.toString());
        return foundClub;
    }

    @Override
    public List<Club> findAll() {
        List<Club> clubs = new ArrayList<>();
        clubRepository.findAll().forEach(club -> clubs.add(club));
        return clubs;
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
