package sshj.sshj.service;


import sshj.sshj.dto.NoteDto;

import java.util.List;


public interface NoteService {

    void executeSendMessage(String sender, String receiver, String msg);

    List<NoteDto> selectPersonList(String loginId);

    List<NoteDto> selectMessages(String loginId, String other);

    int selectCountOfReceiveMessage(String Id);
}
