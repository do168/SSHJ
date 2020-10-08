package sshj.sshj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sshj.sshj.dto.NoteDto;
import sshj.sshj.mapper.NoteMapper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public void sendMessage(String sender, String receiver, String msg) {
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
    public List<String> selectPersonList(String loginId) {
        return noteMapper.selectPerson(loginId);
    }

    @Override
    public List<NoteDto> selectMessages(String loginId, String other) {
        return noteMapper.selectMessage(loginId, other);
    }

    @Override
    public int selectCountOfReceiveMessage(String Id) {
        return noteMapper.selectCountOfReceiveMessage(Id);
    }


    public String time_now() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String dtime = formatter.format(systemTime);
        return dtime;
    }
}