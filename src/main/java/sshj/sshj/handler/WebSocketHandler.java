package sshj.sshj.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sshj.sshj.service.NoteService;

@Slf4j
@Repository
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private NoteService noteService;

//	TODO: 소켓 안쓸거라 도우찬이 수정할 예정
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
//        log.info(message.getPayload());
//        session.sendMessage(new TextMessage(Integer.toString(noteService.selectCountOfReceiveMessage(message.getPayload()))));
//    }

}
