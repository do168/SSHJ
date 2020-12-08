package sshj.sshj.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NoteDto {
    private int noteId;

    private long sender;

    private long receiver;

    private boolean isread;

    private String sendTime;

    private String message;
}
