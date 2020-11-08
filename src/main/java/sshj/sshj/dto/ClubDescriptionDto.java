package sshj.sshj.dto;

import lombok.Data;

@Data
public class ClubDescriptionDto {
    private long clubId;

    private String description;

    public ClubDescriptionDto(long clubId, String description) {
        this.clubId = clubId;
        this.description = description;
    }
}
