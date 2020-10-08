package sshj.sshj.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NoteDto {
    private int noteId;

    private String sender;

    private String receiver;

    private boolean isread;

    private String sendTime;

    private String message;
}
