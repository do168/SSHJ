package sshj.sshj.service;

import sshj.sshj.dto.ClubDescriptionDto;
import sshj.sshj.dto.ClubNoticeDto;

import java.util.List;

public interface ClubService {
    void insertClubNotice(ClubNoticeDto dto) throws Exception;
    ClubNoticeDto selectClubNotice(int noticeId) throws Exception;
    List<ClubNoticeDto> selectClubNoticeList(int clubId) throws Exception;
    void updateClubNotice(ClubNoticeDto dto) throws Exception;
    void deleteClubNotice(int noticeId) throws Exception;
    void insertClubNoticeLike(int userId,int noticeId) throws Exception;
    void deleteClubNoticeLike(int userId,int noticeId) throws Exception;
    int selectClubNoticeCnt(int noticeId) throws Exception;
    void insertClubSubs(int userId,int clubId) throws Exception;
    void deleteClubSubs(int userId,int clubId) throws Exception;

    void insertClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception;
    String selectClubDescription(int clubId) throws Exception;
    void updateClubDescription(ClubDescriptionDto clubDescriptionDto) throws Exception;
    void deleteClubDescription(int clubId) throws Exception;
}
