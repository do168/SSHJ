package sshj.sshj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sshj.sshj.dto.MeetingDto;
import sshj.sshj.mapper.MeetingMapper;

import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingMapper meetingMapper;

    @Override
    public void insertMeeting(MeetingDto dto) throws Exception {
        meetingMapper.insertMeeting(dto);
    }

    @Override
    public MeetingDto selectMeeting(int meetingId) throws Exception {
        return meetingMapper.selectMeeting(meetingId);
    }

    @Override
    public void updateMeeting(MeetingDto dto) throws Exception {
        meetingMapper.updateMeeting(dto);
    }

    @Override
    public void deleteMeeting(int meetingId) throws Exception {
        meetingMapper.deleteMeeting(meetingId);
    }

    @Override
    public List<MeetingDto> selectMeetingList() throws Exception {
        return meetingMapper.selectMeetingList();
    }
}
