package sshj.sshj.mapper;

import org.apache.ibatis.annotations.Mapper;
import sshj.sshj.dto.ClubNoticeDto;

import java.util.List;

@Mapper
public interface ClubMapper {
    void insertClubNotice(ClubNoticeDto dto) throws Exception;
    ClubNoticeDto selectClubNotice(int noticeId) throws Exception;
    List<ClubNoticeDto> selectClubNoticeList(int clubId) throws Exception;
    void updateClubNotice(ClubNoticeDto dto) throws Exception;
    void deleteClubNotice(int noticeId) throws Exception;
    void insertClubNoticeLike(int userId,int noticeId) throws Exception;
    void deleteClubNoticeLike(int userId,int noticeId) throws Exception;
    int selectClubNoticeCnt(int noticeId) throws Exception;
}
