package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sshj.sshj.dto.ClubDescriptionDto;
import sshj.sshj.dto.ClubInfoDto;
import sshj.sshj.dto.ClubNoticeDto;

import java.util.List;

@Repository
@Mapper
public interface ClubMapper {
    void insertClubNotice(ClubNoticeDto dto) throws Exception;
    ClubNoticeDto selectClubNotice(long noticeId) throws Exception;
    List<ClubNoticeDto> selectClubNoticeList(long clubId) throws Exception;
    void updateClubNotice(ClubNoticeDto dto) throws Exception;
    void deleteClubNotice(long noticeId) throws Exception;
    void insertClubNoticeLike(long userId,long noticeId) throws Exception;
    void deleteClubNoticeLike(long userId,long noticeId) throws Exception;
    long selectClubNoticeCnt(long noticeId) throws Exception;
    void insertClubSubs(long userId,long clubId) throws Exception;
    void deleteClubSubs(long userId,long clubId) throws Exception;
    List<Long> selectClubSubsUserList(long clubId) throws Exception;


    void insertClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception;
    String selectClubDescription(long clubId) throws Exception;
    void updateClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception;
    void deleteClubDescription(long clubId) throws Exception;

    int selectIsSubClub(long userId, long clubId) throws Exception;
    List<Long> selectSubClubList(long userId) throws Exception;
    int selectClubSubscribeCnt(long clubId) throws Exception;

    List<ClubInfoDto> selectClubIdAndClubName() throws Exception;
}
