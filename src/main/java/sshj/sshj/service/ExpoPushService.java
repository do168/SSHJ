package sshj.sshj.service;

import org.springframework.stereotype.Service;

@Service
public interface ExpoPushService {
    void sendingPushMeetingCreated(long clubId) throws Exception;
    void sendingPushClubNoticeCreated(long clubId) throws Exception;
}
