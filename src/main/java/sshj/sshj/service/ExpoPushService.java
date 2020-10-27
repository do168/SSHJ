package sshj.sshj.service;

import org.springframework.stereotype.Service;

@Service
public interface ExpoPushService {
    void sendingPushMeetingCreated(int clubId) throws Exception;
    void sendingPushClubNoticeCreated(int clubId) throws Exception;
}
