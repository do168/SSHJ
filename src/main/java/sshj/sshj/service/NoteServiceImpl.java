package sshj.sshj.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sshj.sshj.dto.NoteDto;
import sshj.sshj.dto.ServiceResultModel;
import sshj.sshj.mapper.NoteMapper;
import sshj.sshj.mapper.UserMapper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private ExpoPushServiceImpl expoPushService;

    @Override
    public void insertSendMessage(long sender, long receiver, String msg) {
        String time = time_now();

        NoteDto noteDto = NoteDto.builder()
                .sender(sender)
                .receiver(receiver)
                .sendTime(time)
                .message(msg)
                .build();

        noteMapper.insertMessage(noteDto);

    }
    
    public String time_now() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String dtime = formatter.format(systemTime);
        return dtime;
    }
    
//	TODO: 도우찬 쪽지 수정시 고칠 것
//    @Override
//    public List<NoteDto> selectPersonList(String loginId) {
//        return noteMapper.selectPerson(loginId);
//    }
//
//    @Override
//    public List<NoteDto> selectMessages(String loginId, String other) {
//        return noteMapper.selectMessage(loginId, other);
//    }
//
//    @Override
//    public int selectCountOfReceiveMessage(String Id) {
//        return noteMapper.selectCountOfReceiveMessage(Id);
//    }
//
//

}