package sshj.sshj.service;

import org.springframework.stereotype.Service;
import sshj.sshj.dto.ServiceResultModel;

@Service
public interface ExpoPushService {
    // 로그인 시 토큰 저장 함수
    ServiceResultModel createPushToken(long userId, String expoPushToken);
    void sendingPushMeetingCreated(long clubId) throws Exception;
    void sendingPushClubNoticeCreated(long clubId) throws Exception;
}
