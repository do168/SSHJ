package sshj.sshj.service;


import sshj.sshj.dto.ServiceResultModel;



public interface NoteService {

    void insertSendMessage(long sender, long receiver, String msg);

//	TODO: 도우찬 쪽지 수정시 고칠 것
//    List<NoteDto> selectPersonList(String loginId);
//
//    List<NoteDto> selectMessages(String loginId, String other);
//
//    int selectCountOfReceiveMessage(String Id);
}
