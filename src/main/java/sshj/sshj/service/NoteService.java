package sshj.sshj.service;


import sshj.sshj.dto.NoteDto;

import java.util.List;


public interface NoteService {

    void executeSendMessage(String sender, String receiver, String msg);

//	TODO: 도우찬 쪽지 수정시 고칠 것
//    List<NoteDto> selectPersonList(String loginId);
//
//    List<NoteDto> selectMessages(String loginId, String other);
//
//    int selectCountOfReceiveMessage(String Id);
}
