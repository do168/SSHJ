package sshj.sshj.dto;

import lombok.Data;

@Data
public class UserByMeetingDto {
    private int userId;
    private int meetingId;
    private String createdTime;
}
