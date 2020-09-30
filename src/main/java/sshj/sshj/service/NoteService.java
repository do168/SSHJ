package sshj.sshj.service;


import sshj.sshj.dto.NoteDto;

import java.util.List;


public interface NoteService {

    void sendMessage(String sender, String receiver, String msg);

    List<String> selectPersonList(String loginId);

    List<NoteDto> selectMessages(String loginId, String other);

    int selectCountOfReceiveMessage(String Id);
}
