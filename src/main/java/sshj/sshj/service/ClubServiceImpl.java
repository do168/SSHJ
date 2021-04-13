package sshj.sshj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sshj.sshj.common.exception.NotExistException;
import sshj.sshj.model.Club;
import sshj.sshj.repository.ClubRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
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


}
