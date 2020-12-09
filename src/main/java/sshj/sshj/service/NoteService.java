package sshj.sshj.service;


import sshj.sshj.dto.NoteDto;
import sshj.sshj.dto.ServiceResultModel;

import java.util.List;


public interface NoteService {

    void insertSendMessage(long sender, long receiver, String msg);

    List<NoteDto> selectPersonList(long userId);
    List<NoteDto> selectMessages(long loginId, long other);
    
//    int selectCountOfReceiveMessage(String Id);
}
