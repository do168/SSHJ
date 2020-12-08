package sshj.sshj.service;

import io.github.jav.exposerversdk.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sshj.sshj.dto.ServiceResultModel;
import sshj.sshj.mapper.ClubMapper;
import sshj.sshj.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExpoPushServiceImpl implements ExpoPushService {
    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 유저 expoPushToken 저장
     *
     * @param userId        유저Id
     * @param expoPushToken 유저Token
     * @return Bool
     */
    @Override
    public ServiceResultModel createPushToken(long userId, String expoPushToken) {

        // 파라미터로 넘어온 userId가 DB에 존재하지 않는 경우
        if (userMapper.selectUserInfoById(userId) == null) {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("user doesn't exit")
                    .build();
        }

        //
        try {
            int cnt = userMapper.selectUserPushToken(userId, expoPushToken);
            if (cnt < 1) {
                userMapper.insertPushToken(userId, expoPushToken);
            }

        } catch (Exception e) {
            log.error("", e);
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("insert error")
                    .build();
        }
        return ServiceResultModel.builder()
                .flag(true)
                .msg("expoPush token saved success")
                .build();
    }

    /**
     * 구독 중인 동아리가 새 모임을 등록했을 때 유저에게 푸시 알림
     *
     * @param clubId
     * @throws Exception
     */
    @Override
    public void excuteSendingPushMeetingCreated(long clubId) throws Exception {
        List<Long> clubSubscribeUsers = clubMapper.selectClubSubsUserList(clubId);
        String clubName = userMapper.selectEmail(clubId);
        for (long userId : clubSubscribeUsers) {
            List<String> pushTokenList = userMapper.selectUserPushTokenList(userId);
            for (String pushToken : pushTokenList) {
                String title = "모임 등록!";
                String message = clubName + "의 새 모임이 등록되었습니다!";

                expoPush(pushToken, title, message);
                try {
                    if (pushToken == null) {
                        log.error("deviceToken is null [{}]", userId);
                        return;
                    } else
                        expoPush(pushToken, title, message);

                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
    }

    /**
     * 구독 중인 동아리의 새 공지가 올라왔을 경우
     *
     * @param clubId
     * @throws Exception
     */
    @Override
    public void excuteSendingPushClubNoticeCreated(long clubId) throws Exception {
        List<Long> clubSubscribeUsers = clubMapper.selectClubSubsUserList(clubId);
        String clubName = userMapper.selectEmail(clubId);
        for (long userId : clubSubscribeUsers) {
            List<String> pushTokenList = userMapper.selectUserPushTokenList(userId);
            for (String pushToken : pushTokenList) {
                String title = "공지 등록!";
                String message = clubName + "의 새 공지가 등록되었습니다!";
                try {
                    if (pushToken == null) {
                        log.error("deviceToken is null [{}]", userId);
                        return;
                    } else
                        expoPush(pushToken, title, message);

                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
    }

    @Override
    public ServiceResultModel excuteSendingPushNoteReceived(long sender, long receviver, String message) {
        String senderNickname = userMapper.selectUserNickname(sender);
        List<String> pushTokenList = userMapper.selectUserPushTokenList(receviver);
        if (pushTokenList == null) {
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg("PushToken is null [" + receviver + "]")
                    .build();
        }

        try {
            for (String pushToken : pushTokenList) {
                String title = senderNickname + "(으)로부터 쪽지 도착!";
                expoPush(pushToken, title, message);
            }

        } catch(Exception e) {
            log.error("", e);
            return ServiceResultModel.builder()
                    .flag(false)
                    .msg(e.toString())
                    .build();
        } finally {
            return ServiceResultModel.builder()
                    .flag(true)
                    .msg("발신 성공")
                    .build();
        }
    }


    private void expoPush(String deviceToken, String title, String message) throws PushClientException, InterruptedException {
        if (!PushClient.isExponentPushToken(deviceToken))
            throw new Error("Token:" + deviceToken + " is not a valid token");

        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.getTo().add(deviceToken);
        expoPushMessage.setTitle(title);
        expoPushMessage.setBody(message);

        List<ExpoPushMessage> expoPushMessages = new ArrayList<>();
        expoPushMessages.add(expoPushMessage);

        PushClient client = new PushClient();
        List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(expoPushMessages);

        List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();

        for (List<ExpoPushMessage> chunk : chunks) {
            messageRepliesFutures.add(client.sendPushNotificationsAsync(chunk));
        }

        // Wait for each completable future to finish
        List<ExpoPushTicket> allTickets = new ArrayList<>();
        for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
            try {
                for (ExpoPushTicket ticket : messageReplyFuture.get()) {
                    allTickets.add(ticket);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        List<ExpoPushMessageTicketPair<ExpoPushMessage>> zippedMessagesTickets = client.zipMessagesTickets(expoPushMessages, allTickets);

        List<ExpoPushMessageTicketPair<ExpoPushMessage>> okTicketMessages = client.filterAllSuccessfulMessages(zippedMessagesTickets);
        String okTicketMessagesString = okTicketMessages.stream().map(
                p -> "Title: " + p.message.getTitle() + ", Id:" + p.ticket.getId()
        ).collect(Collectors.joining(","));
        System.out.println(
                "Received OK ticket for " +
                        okTicketMessages.size() +
                        " messages: " + okTicketMessagesString
        );

        List<ExpoPushMessageTicketPair<ExpoPushMessage>> errorTicketMessages = client.filterAllMessagesWithError(zippedMessagesTickets);
        String errorTicketMessagesString = errorTicketMessages.stream().map(
                p -> "Title: " + p.message.getTitle() + ", Error: " + p.ticket.getDetails().getError()
        ).collect(Collectors.joining(","));
        System.out.println(
                "Received ERROR ticket for " +
                        errorTicketMessages.size() +
                        " messages: " +
                        errorTicketMessagesString
        );

        Thread.sleep(1000);
        System.out.println("Fetching receipts...");

        List<String> ticketIds = (client.getTicketIdsFromPairs(okTicketMessages));
        CompletableFuture<List<ExpoPushReceipt>> receiptFutures = client.getPushNotificationReceiptsAsync(ticketIds);

        List<ExpoPushReceipt> receipts = new ArrayList<>();
        try {
            receipts = receiptFutures.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(
                "Received " + receipts.size() + " receipts:");

        for (ExpoPushReceipt reciept : receipts) {
            System.out.println(
                    "Receipt for id: " +
                            reciept.getId() +
                            " had status: " +
                            reciept.getStatus());

        }
    }
}
