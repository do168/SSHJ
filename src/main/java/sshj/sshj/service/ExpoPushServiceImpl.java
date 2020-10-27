package sshj.sshj.service;

import io.github.jav.exposerversdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import sshj.sshj.mapper.ClubMapper;
import sshj.sshj.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ExpoPushServiceImpl implements ExpoPushService {
    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 구독 중인 동아리가 새 모임을 등록했을 때 유저에게 푸시 알림
     * @param clubId
     * @throws Exception
     */
    @Override
    public void sendingPushMeetingCreated(int clubId) throws Exception {
        List<Integer> clubSubscribeUsers = clubMapper.selectClubSubs(clubId);
        String clubName = userMapper.selectLoginId(clubId);
        for (int userId : clubSubscribeUsers) {
            String deviceToken = userMapper.selectUserDeviceToken(userId);
            String title = "모임 등록!";
            String message = clubName+"의 새 모임이 등록되었습니다!";

            expoPush(deviceToken, title, message);
        }
    }

    @Override
    public void sendingPushClubNoticeCreated(int clubId) throws Exception {
        List<Integer> clubSubscribeUsers = clubMapper.selectClubSubs(clubId);
        String clubName = userMapper.selectLoginId(clubId);
        for (int userId : clubSubscribeUsers) {
            String deviceToken = userMapper.selectUserDeviceToken(userId);
            String title = "공지 등록!";
            String message = clubName+"의 새 공지가 등록되었습니다!";

            expoPush(deviceToken, title, message);
        }
    }


    public void expoPush(String deviceToken, String title, String message) throws PushClientException, InterruptedException {
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
                "Recieved OK ticket for " +
                        okTicketMessages.size() +
                        " messages: " + okTicketMessagesString
        );

        List<ExpoPushMessageTicketPair<ExpoPushMessage>> errorTicketMessages = client.filterAllMessagesWithError(zippedMessagesTickets);
        String errorTicketMessagesString = errorTicketMessages.stream().map(
                p -> "Title: " + p.message.getTitle() + ", Error: " + p.ticket.getDetails().getError()
        ).collect(Collectors.joining(","));
        System.out.println(
                "Recieved ERROR ticket for " +
                        errorTicketMessages.size() +
                        " messages: " +
                        errorTicketMessagesString
        );

        // Countdown 30s
        int wait = 30;
        for (int i = wait; i >= 0; i--) {
            System.out.print("Waiting for " + wait + " seconds. " + i + "s\r");
            Thread.sleep(1000);
        }
        System.out.println("Fetching reciepts...");

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
                "Recieved " + receipts.size() + " receipts:");

        for (ExpoPushReceipt reciept : receipts) {
            System.out.println(
                    "Receipt for id: " +
                            reciept.getId() +
                            " had status: " +
                            reciept.getStatus());

        }
    }
}
