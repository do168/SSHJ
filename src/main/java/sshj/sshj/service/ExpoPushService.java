package sshj.sshj.service;

import org.springframework.stereotype.Service;
import sshj.sshj.dto.ServiceResultModel;

@Service
public interface ExpoPushService {
    // 로그인 시 토큰 저장 함수
    ServiceResultModel createPushToken(long userId, String expoPushToken);
    // 구독 중인 동아리 모임 등록 시 알림
    void excuteSendingPushMeetingCreated(long clubId) throws Exception;
    // 구독 중인 동아리 공지 생성 시 알림
    void excuteSendingPushClubNoticeCreated(long clubId) throws Exception;
    // 쪽지 수신 시 알림
    ServiceResultModel excuteSendingPushNoteReceived(long sender, long receviver, String message);
}
