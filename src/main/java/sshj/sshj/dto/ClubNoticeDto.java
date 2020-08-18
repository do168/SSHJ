package sshj.sshj.dto;

import lombok.Data;

@Data
public class ClubNoticeDto {
    private int noticeId;
    private int clubId;
    private String title;
    private String content;
    private String createdTime;
}
