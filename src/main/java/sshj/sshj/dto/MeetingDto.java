package sshj.sshj.dto;

import lombok.Data;

@Data
public class MeetingDto {
    private int meetingId;
    private String meetingName;
    private String deadline;
    private String startDate;
    private String endDate;
    private String category;
    private String explanationTitle;
    private String explanationContent;
    private String createdTime;
    private int maxParticipant;
    private int clubId;
}
