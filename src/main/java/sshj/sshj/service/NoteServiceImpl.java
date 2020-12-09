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

    @Override
    public List<NoteDto> selectPersonList(long userId) {
        return noteMapper.selectPerson(userId);
    }

    public String time_now() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String dtime = formatter.format(systemTime);
        return dtime;
    }
    

    @Override
    public List<NoteDto> selectMessages(long loginId, long other) {
        try {
            return noteMapper.selectMessage(loginId, other);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
//
//    @Override
//    public int selectCountOfReceiveMessage(String Id) {
//        return noteMapper.selectCountOfReceiveMessage(Id);
//    }
//
//

}